package vip.yeee.memo.integrate.thirdsdk.aliyun.nls.kit;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.OutputFormatEnum;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizer;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerListener;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.thirdsdk.aliyun.nls.properties.AliyunNlsProperties;

import javax.annotation.Resource;
import java.io.*;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 阿里云智能语音服务 https://help.aliyun.com/product/30413.html
 */
@Slf4j
@Component
public class AliyunNlsKit implements InitializingBean, DisposableBean {

    @Resource
    private AliyunNlsProperties nlsProperties;
    private NlsClient client = null;
    private AccessToken token = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            token = new AccessToken(nlsProperties.getAccessKeyId(), nlsProperties.getAccessKeySecret());
            token.apply();
            if (StrUtil.isBlank(token.getToken())) {
                throw new RuntimeException("Token is blank");
            }
            log.info("INIT -> accessToken = {}, expireTime = {}", token.getToken(), Date.from(Instant.ofEpochSecond(token.getExpireTime())));
            client = new NlsClient(token.getToken());
        } catch (Exception e) {
            log.info("ali nls init error!!!", e);
            // throw e;
        }
    }

    @Override
    public void destroy() throws Exception {
        shutdown();
    }

    public void shutdown() {
        if (client != null) {
            client.shutdown();
        }
    }

    /**
     * 将文本转成音频文件
     * @param localPath 本地保存音频文件地址
     * @param textArr 文本按大小分割后的数组
     * @param objectId 标识
     * @param isWav
     * @param voice
     * @throws IOException
     */
    public void text2LocalWavFile(String localPath, List<String> textArr, Object objectId, Integer isWav, String voice) throws IOException {
        this.text2LocalWavFile(localPath, Integer.valueOf(1).equals(isWav),
                out -> this.splitSpeechSynthesizer(textArr, objectId, isWav, voice, out));
    }

    /**
     * 长文本转成音频文件
     * @param localPath 本地保存音频文件地址
     * @param content 文本按大小分割后的数组
     * @param objectId 标识
     * @param isWav
     * @param voice
     * @throws IOException
     */
    public void longText2LocalWavFile(String localPath, String content, Object objectId, Integer isWav, String voice) throws IOException {
        this.text2LocalWavFile(localPath, Integer.valueOf(1).equals(isWav),
                out -> this.longTestSpeechSynthesizer(content, objectId, isWav, voice, out));
    }

    /**
     * 语音合成
     */
    public void splitSpeechSynthesizer(List<String> textArr, Object objectId, Integer isWav, String voice, final OutputStream out) throws RuntimeException {
        SpeechSynthesizer synthesizer = null;
        try {
            // 刷新token
            this.checkAndRefreshToken();
            //创建实例,建立连接
            synthesizer = new SpeechSynthesizer(client, getSynthesizerListener(objectId, out));
            synthesizer.setAppKey(nlsProperties.getAppKey());
            synthesizer.setFormat(Integer.valueOf(1).equals(isWav) ? OutputFormatEnum.PCM : OutputFormatEnum.MP3);
            synthesizer.setVoice(StrUtil.emptyToDefault(voice, "aida"));
            //设置返回音频的采样率
            synthesizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            for (String s : textArr) {
                if (StrUtil.isBlank(s)) {
                    continue;
                }
                //设置用于语音合成的文本
                synthesizer.setText(s);
                //此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
                synthesizer.start();
                //等待语音合成结束
                synthesizer.waitForComplete();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //关闭连接
            if (null != synthesizer) {
                synthesizer.close();
            }
        }
    }

    /**
     * 语音合成
     */
    private void longTestSpeechSynthesizer(String content, Object objectId, Integer isWav, String voice, final OutputStream out) throws RuntimeException {
        SpeechSynthesizer synthesizer = null;
        try {
            // 刷新token
            this.checkAndRefreshToken();
            synthesizer = new SpeechSynthesizer(client, getSynthesizerListener(objectId, out));
            synthesizer.setAppKey(nlsProperties.getAppKey());
            synthesizer.setFormat(Integer.valueOf(1).equals(isWav) ? OutputFormatEnum.PCM : OutputFormatEnum.MP3);
            synthesizer.setVoice(StrUtil.emptyToDefault(voice, "aida"));
            synthesizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            synthesizer.setLongText(content);
            synthesizer.start();
            synthesizer.waitForComplete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //关闭连接
            if (null != synthesizer) {
                synthesizer.close();
            }
        }
    }

    private void checkAndRefreshToken() throws IOException {
        if (new Date().after(DateUtil.date(token.getExpireTime() * 1000 - TimeUnit.MINUTES.toMillis(20)))) {
            token.apply();
            log.info("REFRESH -> accessToken = {}, expireTime = {}", token.getToken(), Date.from(Instant.ofEpochSecond(token.getExpireTime())));
            client.setToken(token.getToken());
        }
    }

    private SpeechSynthesizerListener getSynthesizerListener(Object objId, final OutputStream out) {
        return new SpeechSynthesizerListener() {
            int totalSize = 0;
            //语音合成结束
            @Override
            public void onComplete(SpeechSynthesizerResponse response) {
                log.info("obj_id：{}, task_id: {}, name: {}, status: {}", objId, response.getTaskId(), response.getName(), response.getStatus());
                log.info("Cur part onComplete, totalsize = {}", totalSize);
            }

            //语音合成的语音二进制数据
            @Override
            public void onMessage(ByteBuffer message) {
                try {
                    byte[] bytesArray = new byte[message.remaining()];
                    message.get(bytesArray, 0, bytesArray.length);
                    totalSize += bytesArray.length;
                    out.write(bytesArray);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(SpeechSynthesizerResponse response) {
                // 重要提示： task_id很重要，是调用方和服务端通信的唯一ID标识，当遇到问题时，需要提供此task_id以便排查
                log.error("obj_id：{}, task_id: {}, status: {}, status_text: {}", objId, response.getTaskId(), response.getStatus(), response.getStatusText());
            }
        };
    }

    /**
     * 文本转音频文件
     */
    private void text2LocalWavFile(String path, boolean isWav, Consumer<OutputStream> consumer) throws IOException {
        try (BufferedOutputStream out = FileUtil.getOutputStream(path)) {
            WavHeader header = null;
            if (isWav) {
                // 初期并不知道wav文件实际长度，假设为0，最后再校正
                int pcmSize = 0;
                header = new WavHeader();
                // 长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
                header.fileLength = pcmSize + (44 - 8);
                header.fmtHdrLeth = 16;
                header.bitsPerSample = 16;
                header.channels = 1;
                header.formatTag = 0x0001;
                header.samplesPerSec = 16000;
                header.blockAlign = (short) (header.channels * header.bitsPerSample / 8);
                header.avgBytesPerSec = header.blockAlign * header.samplesPerSec;
                header.dataHdrLeth = pcmSize;
                byte[] h = header.getHeader();
                assert h.length == 44;
                // 先写入44字节的wav头，如果合成的不是wav，比如是pcm，则不需要此步骤
                out.write(h);
            }
            consumer.accept(out);
            if (isWav) {
                // 更新44字节的wav头，如果合成的不是wav，比如是pcm，则不需要此步骤
                try (RandomAccessFile wavFile = new RandomAccessFile(path, "rw")) {
                    int fileLength = (int) wavFile.length();
                    int dataSize = fileLength - 44;
                    log.info("fileLength = {}， dataSize = {}", fileLength, dataSize);
                    header.fileLength = fileLength - 8;
                    header.dataHdrLeth = fileLength - 44;
                    wavFile.write(header.getHeader());
                }
            }
        }
    }

    public static class WavHeader {
        /**
         * 4 资源交换文件标志（RIFF）
         */
        public final char[] fileID = {'R', 'I', 'F', 'F'};
        /**
         * 4 总字节数
         */
        public int fileLength;
        /**
         * 4 WAV文件标志（WAVE）
         */
        public char[] wavTag = {'W', 'A', 'V', 'E'};
        /**
         * 4 波形格式标志（fmt ），最后一位空格
         */
        public char[] fmtHdrID = {'f', 'm', 't', ' '};
        /**
         * 4 过滤字节（一般为00000010H），若为00000012H则说明数据头携带附加信息
         */
        public int fmtHdrLeth;
        /**
         * 2 格式种类（值为1时，表示数据为线性PCM编码）
         */
        public short formatTag;
        /**
         * 2 通道数，单声道为1，双声道为2
         */
        public short channels;
        /**
         * 4 采样频率
         */
        public int samplesPerSec;
        /**
         * 4 波形数据传输速率（每秒平均字节数）
         */
        public int avgBytesPerSec;
        /**
         * 2 DATA数据块长度，字节
         */
        public short blockAlign;
        /**
         * 2 PCM位宽
         */
        public short bitsPerSample;
        /**
         * 4 数据标志符（data）
         */
        public char[] dataHdrID = {'d', 'a', 't', 'a'};
        /**
         * 4 DATA总数据长度字节
         */
        public int dataHdrLeth;
        public byte[] getHeader() throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            WriteChar(bos, fileID);
            WriteInt(bos, fileLength);
            WriteChar(bos, wavTag);
            WriteChar(bos, fmtHdrID);
            WriteInt(bos, fmtHdrLeth);
            WriteShort(bos, formatTag);
            WriteShort(bos, channels);
            WriteInt(bos, samplesPerSec);
            WriteInt(bos, avgBytesPerSec);
            WriteShort(bos, blockAlign);
            WriteShort(bos, bitsPerSample);
            WriteChar(bos, dataHdrID);
            WriteInt(bos, dataHdrLeth);
            bos.flush();
            byte[] r = bos.toByteArray();
            bos.close();
            return r;
        }
        private void WriteShort(ByteArrayOutputStream bos, int s) throws IOException {
            byte[] mybyte = new byte[2];
            mybyte[1] = (byte) ((s << 16) >> 24);
            mybyte[0] = (byte) ((s << 24) >> 24);
            bos.write(mybyte);
        }
        private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException {
            byte[] buf = new byte[4];
            buf[3] = (byte) (n >> 24);
            buf[2] = (byte) ((n << 8) >> 24);
            buf[1] = (byte) ((n << 16) >> 24);
            buf[0] = (byte) ((n << 24) >> 24);
            bos.write(buf);
        }
        private void WriteChar(ByteArrayOutputStream bos, char[] id) {
            for (char c : id) {
                bos.write(c);
            }
        }
    }

}
