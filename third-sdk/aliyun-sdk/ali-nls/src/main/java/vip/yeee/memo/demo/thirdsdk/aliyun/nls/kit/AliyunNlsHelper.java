package vip.yeee.memo.demo.thirdsdk.aliyun.nls.kit;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.thirdsdk.aliyun.nls.bo.AudioGenBo;
import vip.yeee.memo.demo.thirdsdk.aliyun.nls.properties.GenNewsAudioConfig;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AliyunNlsHelper {

    @Resource
    private AliyunNlsKit aliyunNlsKit;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private GenNewsAudioConfig genNewsAudioConfig;
    private RPermitExpirableSemaphore semaphore = null;

    @PostConstruct
    public void init() {
        this.setSpeechSynthesizerSemaphore();
    }

    public void text2LocalFile(String localPath, AudioGenBo audioGenBo) throws Exception {
        Integer longMode = genNewsAudioConfig.getLongMode();
        Integer isWav = genNewsAudioConfig.getIsWav();
        String voice = genNewsAudioConfig.getVoice();
        Integer splitSize = Optional.ofNullable(genNewsAudioConfig.getSplitSize()).orElse(100);
        if (Integer.valueOf(1).equals(longMode)) {
            aliyunNlsKit.longText2LocalWavFile(localPath, audioGenBo.getContent(), audioGenBo.getObjectId(), isWav, voice);
        } else {
            String id;
            try {
                id = semaphore.acquire(20, TimeUnit.MINUTES);
                if (id == null) {
                    throw new Exception("permitId is null");
                }
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
            try {
                aliyunNlsKit.text2LocalWavFile(localPath, this.splitLongText(audioGenBo.getContent(), splitSize), audioGenBo.getObjectId(), isWav, voice);
            } finally {
                semaphore.release(id);
            }
        }
    }

    private void setSpeechSynthesizerSemaphore() {
        semaphore = redissonClient.getPermitExpirableSemaphore("lock:yeeejob:speechSynthesizer");
        semaphore.trySetPermits(genNewsAudioConfig.getConcurrentNum());
    }

    /**
     * 将长文本切分为每句字数不大于size数目的短句
     */
    private List<String> splitLongText(String text, int size) {
        //先按标点符号切分
        String[] texts = text.split("[、，。；？！,!?：:.;\"”'’/|`~&]");
        StringBuilder textPart = new StringBuilder();
        List<String> result = new ArrayList<>();
        int len = 0;
        //再按size merge,避免标点符号切分出来的太短
        for (String s : texts) {
            if (textPart.length() + s.length() + 1 > size) {
                result.add(textPart.toString());
                textPart.delete(0, textPart.length());
            }
            textPart.append(s);
            len += s.length();
            if (len < text.length()) {
                //System.out.println("at " + text.charAt(len));
                textPart.append(text.charAt(len));
                len += 1;
            }
        }
        if (textPart.length() > 0) {
            result.add(textPart.toString());
        }
        return result;
    }

}
