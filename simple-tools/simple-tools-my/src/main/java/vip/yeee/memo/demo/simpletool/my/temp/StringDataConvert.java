package vip.yeee.memo.demo.simpletool.my.temp;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2024/1/15 10:19
 */
public class StringDataConvert {

    public static void main(String[] args) {

        String aa = "";



        System.out.println(Stream.of(aa.replaceAll("【", "").replaceAll("】", "").split("[\n,]"))
                .map(s -> "'" + s + "'")
//                .map(s -> "'\"" + s + "\"'")
//                .map(s -> "DATA_CONVERTER:LAST:TRANSFER_CONTENT-RELATE:bfeb4822c0824bc9ac48a588dda9ee12:" + s)
                .distinct()
                .collect(Collectors.joining(",")));
//                .collect(Collectors.joining(" ")));

    }

}
