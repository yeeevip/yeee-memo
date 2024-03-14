package vip.yeee.memo.common.redisson.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/3/14 14:57
 */
public class Md5Util {

    public static String md5(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(md5("111111"));;
    }
}
