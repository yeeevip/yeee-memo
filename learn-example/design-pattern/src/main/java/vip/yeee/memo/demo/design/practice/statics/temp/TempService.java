package vip.yeee.memo.demo.design.practice.statics.temp;

import com.fasterxml.jackson.core.type.TypeReference;
import vip.yeee.memo.demo.design.practice.statics.vo.TCmsSite;
import vip.yeee.memo.demo.design.practice.statics.vo.RLock;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/1/29 13:25
 */
public class TempService {
    public static class RedissonClient {

        public RLock getLock(String s) {
            return null;
        }

    }

    public static class ConfigApiFeignAPI {

        public TCmsSite getSiteById(String siteId) {
            return null;
        }
    }

    public static class OssKit {

        public <T> T getStaticJson2Object(String staticPath, TypeReference<T> staticDataVoTypeReference) {
            return null;
        }

        public void uploadObject2StaticJson(String staticPath, Object obj) {
        }
    }
}
