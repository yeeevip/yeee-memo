package vip.yeee.memo.base.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 14:09
 */
public class CodeImgUtil {

    public static void writeQrCode(OutputStream stream, String info, int width, int height) throws WriterException, IOException {
        String format="png";
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN,0);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(info,
                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
        bitMatrix = deleteWhite(bitMatrix);
        BufferedImage bi = MatrixToImageWriter.toBufferedImage(bitMatrix);
        bi = zoomInImage(bi,width,height);
        ImageIO.write(bi,format,stream);            // 输出图像
        //MatrixToImageWriter.writeToStream(bitMatrix, format, stream);// 输出图像
    }

    public static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height){
        BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage,0,0,width,height,null);
        g.dispose();
        return newImage;
    }

    /**
     * 去除白边
     * */
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

}
