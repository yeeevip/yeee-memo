package vip.yeee.memo.demo.simpletool.my.temp;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/12/16 14:51
 */
public class TempHandle22 {


    public static void main(String[] args) {


        String aa = "| 328 | 南湖公务员小区                 | c74970e570684a72ae146dd36981ee9a |\n" +
                "| 327 | 明秀小区                       | 0d36f414bb4f4120a54d8816f4f1a34c |\n" +
                "| 326 | 桂宏达·翰林府                  | 3831a8d5110b42b3bb7d30ba3a640072 |\n" +
                "| 325 | 桂宏达·西江首府                | bbc51fe3b73e48468b0da9ad640e444a |\n" +
                "| 322 | 新希望锦官城                   | b66125e358914b698e5ffc8a8bef19d6 |\n" +
                "| 317 | 兆信禧悦湾                     | 75bc1281f2dd4869b8ad950a3fd509ea |\n" +
                "| 316 | 龙湖盛天双珑原著小区           | 250911bfc1f849a3ba94018462fc0760 |\n" +
                "| 313 | 瑞和家园                       | e923965406b849708c6babdc9f113aba |\n" +
                "| 310 | 南宁市聚宝苑小区               | 9906071f7eaa46d9a6366d7541a82601 |\n" +
                "| 307 | 荣和·天誉                      | 0bd874330c884a43b69e25eb04d32bec |\n" +
                "| 301 | 凤祥名居                       | 719ecc3e73da4bc88cd4d83d3283765d |\n" +
                "| 296 | 联发·君悦天御                  | 5513d5871ec54be983f9556fba1f4fbc |\n" +
                "| 294 | 联发臻境                       | f22ba6abefd54c5bb829bc47d2002211 |\n" +
                "| 285 | 鹿寨碧桂园翡翠湾               | c5f97cff23604fd5bbecc5cba905a0c2 |\n" +
                "| 284 | 横县碧桂园                     | be31e52e583b4aaf8fff7bf0d14b2113 |\n" +
                "| 100 | 和实·水榭花都                  | 520133c0078044f0ac1dd1b4882459e3 |\n" +
                "|  99 | 佳得鑫·水晶城                  | 7220a1f35251454ca7c24501cffae3ca |\n" +
                "|  97 | 葛村路石油小区                 | 8da5c4da39c44e62aef7d9d8bf76b291 |\n" +
                "|  70 | 中海悦公馆                     | 964fb0dbcc744e8297590ff84e181f3c |\n" +
                "|  69 | 中海九玺                       | cc0253895bf14a358cf2ec885dcf248b |\n" +
                "|  68 | 招商雍景湾                     | 148b1ac7e22548538d6ef6b9858cb178 |\n" +
                "|  67 | 东盟李宁中心                   | 5ccf15f701fc4fdda947e2c54b939dad |\n" +
                "|  66 | 南宁彰泰城                     | d23d9a50e6e54790a82e5cb97426b781 |\n" +
                "|  65 | 南宁青溪府                     | 0dbef31de0bc44a294fd9f0b4e1a7b1b |\n" +
                "|  64 | 荣和·五象学府                  | bcc966c2e6f24197b2ddd2ecae384087 |\n" +
                "|  63 | 荣和·澜山府                    | b93108f08d754f0a821a11db636accd4 |\n" +
                "|  62 | 未来城市                       | 722761a20e1641478519c31c69646e37 |\n" +
                "|  61 | 华润置地西园                   | d00a520ba03f435db18409a57a5bf19d |\n" +
                "|  60 | 华润置地江南中心               | ec98da1de7c84b45ac7cd5fddef66ae9 |\n" +
                "|  59 | 柳州静兰湾·熙悦山              | c3dd2a2688e544f599499226b31af064 |\n" +
                "|  50 | 保利·领秀前城领秀府            | 2e61b14c447c4fedbd953d56cd546207 |";


        String[] split = aa.split("\n");
        for (int i = 0; i < split.length; i++) {
            String[] split1 = split[i].split("\\|");
            System.out.println(StrUtil.trim(split1[3]));
        }

    }
}
