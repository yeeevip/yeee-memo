package vip.yeee.memo.integrate.stools.kit.configure;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.yeee.memo.integrate.stools.kit.property.OkhttpProperties;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/5 19:49
 */
@Configuration
public class KitConfig {

    @Resource
    private OkhttpProperties okhttpProperties;

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory(), x509TrustManager())
                .retryOnConnectionFailure(false) // 是否开启缓存
                .connectionPool(pool())
                .connectTimeout(okhttpProperties.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(okhttpProperties.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(okhttpProperties.getWriteTimeout(), TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }

    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            // 信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] {x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(okhttpProperties.getMaxIdleConnections(), okhttpProperties.getKeepAliveDuration(),
                TimeUnit.SECONDS);
    }

}
