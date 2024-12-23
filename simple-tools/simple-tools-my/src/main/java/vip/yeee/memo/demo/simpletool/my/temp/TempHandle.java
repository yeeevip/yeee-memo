package vip.yeee.memo.demo.simpletool.my.temp;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/12/16 14:51
 */
public class TempHandle {


    public static void main(String[] args) {


        String aa = "c74970e570684a72ae146dd36981ee9a\n" +
                "0d36f414bb4f4120a54d8816f4f1a34c\n" +
                "3831a8d5110b42b3bb7d30ba3a640072\n" +
                "bbc51fe3b73e48468b0da9ad640e444a\n" +
                "b66125e358914b698e5ffc8a8bef19d6\n" +
                "75bc1281f2dd4869b8ad950a3fd509ea\n" +
                "250911bfc1f849a3ba94018462fc0760\n" +
                "e923965406b849708c6babdc9f113aba\n" +
                "9906071f7eaa46d9a6366d7541a82601\n" +
                "0bd874330c884a43b69e25eb04d32bec\n" +
                "719ecc3e73da4bc88cd4d83d3283765d\n" +
                "5513d5871ec54be983f9556fba1f4fbc\n" +
                "f22ba6abefd54c5bb829bc47d2002211\n" +
                "c5f97cff23604fd5bbecc5cba905a0c2\n" +
                "be31e52e583b4aaf8fff7bf0d14b2113\n" +
                "520133c0078044f0ac1dd1b4882459e3\n" +
                "7220a1f35251454ca7c24501cffae3ca\n" +
                "8da5c4da39c44e62aef7d9d8bf76b291\n" +
                "964fb0dbcc744e8297590ff84e181f3c\n" +
                "cc0253895bf14a358cf2ec885dcf248b\n" +
                "148b1ac7e22548538d6ef6b9858cb178\n" +
                "5ccf15f701fc4fdda947e2c54b939dad\n" +
                "d23d9a50e6e54790a82e5cb97426b781\n" +
                "0dbef31de0bc44a294fd9f0b4e1a7b1b\n" +
                "bcc966c2e6f24197b2ddd2ecae384087\n" +
                "b93108f08d754f0a821a11db636accd4\n" +
                "722761a20e1641478519c31c69646e37\n" +
                "d00a520ba03f435db18409a57a5bf19d\n" +
                "ec98da1de7c84b45ac7cd5fddef66ae9\n" +
                "c3dd2a2688e544f599499226b31af064\n" +
                "2e61b14c447c4fedbd953d56cd546207\n" +
                "d7a98ce0702a488cbbd14937192650e9\n" +
                "55da706378874f4b94a50a9f38bdfcc8";
        String bb = "60722\n" +
                "4467\n" +
                "3420\n" +
                "2870\n" +
                "28050\n" +
                "3210\n" +
                "5166\n" +
                "25469\n" +
                "2892\n" +
                "63821\n" +
                "62689\n" +
                "17377\n" +
                "23590\n" +
                "47517\n" +
                "325738\n" +
                "12880\n" +
                "15342\n" +
                "28370\n" +
                "7990\n" +
                "4880\n" +
                "87024\n" +
                "82184\n" +
                "308607\n" +
                "105394\n" +
                "68233\n" +
                "111605\n" +
                "5301\n" +
                "5259\n" +
                "2950\n" +
                "28744\n" +
                "15148\n" +
                "6113\n" +
                "3598";

        Map<String, Integer> map = new HashMap<>();


        String[] aaArr = aa.split("\n");
        String[] bbArr = bb.split("\n");

        for (int i = 0; i < aaArr.length; i++) {
            map.put(aaArr[i], Integer.valueOf(bbArr[i]));
        }

        System.out.println(JSON.toJSONString(map));

    }
}
