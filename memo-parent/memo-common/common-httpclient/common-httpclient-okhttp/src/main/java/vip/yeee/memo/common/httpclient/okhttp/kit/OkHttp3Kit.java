package vip.yeee.memo.common.httpclient.okhttp.kit;

import cn.hutool.core.map.MapUtil;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.base.util.JacksonUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/5 19:47
 */
@Slf4j
@Component
public class OkHttp3Kit {

    private static final MediaType MT_JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient okHttpClient;
    private OkHttpClient tempOkHttpClient;
    private final ThreadLocal<Boolean> useTempClient = new ThreadLocal<>();

    public OkHttp3Kit(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        this.tempOkHttpClient = okHttpClient;
    }

    public String get(String url) {
        return get(url, null);
    }

    public String get(String url, Map<String, String> headers) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        log.info("Get请求：{}", url);
        Request.Builder builder = new Request.Builder();
        if (headers != null) {
            builder.headers(Headers.of(headers));
        }
        Request request = builder.url(url).get().build();
        String body = execute(request);
        log.info("Get请求耗时：{}ms，结果：{}", stopwatch.elapsed(TimeUnit.MILLISECONDS), body);
        return body;
    }

    public String postForm(String url, Map<String, String> paramMap) {
        return this.postForm(url, null, paramMap);
    }

    public String postForm(String url, Map<String, String> headerMap, Map<String, String> paramMap) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        log.info("Post请求：{}", url);
        Request.Builder requestBuilder = new Request.Builder();
        if (MapUtil.isNotEmpty(headerMap)) {
            headerMap.forEach(requestBuilder::addHeader);
        }
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (MapUtil.isNotEmpty(paramMap)) {
            paramMap.forEach(formBuilder::add);
        }
        Request request = requestBuilder.url(url).post(formBuilder.build()).build();
        String body = execute(request);
        log.info("Post请求耗时：{}ms，结果：{}", stopwatch.elapsed(TimeUnit.MILLISECONDS), body);
        return body;
    }

    public String postJson(String url, Object data) throws Exception {
        return this.postJson(url, null, data);
    }

    public String postJson(String url, Map<String, String> headerMap, Object data) throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        log.info("Post请求：{}", url);
        Request.Builder requestBuilder = new Request.Builder();
        if (MapUtil.isNotEmpty(headerMap)) {
            headerMap.forEach(requestBuilder::addHeader);
        }
        RequestBody requestBody = RequestBody.create(Objects.isNull(data) ? "{}" : JacksonUtils.toJsonString(data), MT_JSON);
        Request request = requestBuilder.url(url).post(requestBody).build();
        String body = execute(request);
        log.info("Post请求耗时：{}ms，结果：{}", stopwatch.elapsed(TimeUnit.MILLISECONDS), body);
        return body;
    }

    public OkHttp3Kit builderClient(Function<OkHttpClient.Builder, OkHttpClient.Builder> func) {
        this.tempOkHttpClient = func.apply(this.okHttpClient.newBuilder()).build();
        this.useTempClient.set(true);
        return this;
    }

    /**
     * 执行Http请求并接受响应体内容
     */
    private String execute(Request request) {
        Response response;
        try {
            try {
                response = ((this.useTempClient.get() != null && this.useTempClient.get()) ? tempOkHttpClient : okHttpClient).newCall(request).execute();
            } catch (IOException e) {
                log.error("Http请求异常：{} - {}，原因：{}", request.method(), request.url().url(), e.getMessage(), e);
                throw new BizException("Http请求异常：" + request.method() + " - " + request.url().url());
            }
            if (!response.isSuccessful()) {
                log.error("Http请求失败：{} - {}，code：{}，message：{}", request.method(), request.url().url(), response.code(), response.message());
                throw new BizException("Http请求失败：" + request.method() + " - " + request.url().url());
            }
            try {
                return Objects.requireNonNull(response.body()).string();
            } catch (IOException e) {
                log.error("Http请求响应体解析异常：{} - {}，原因：{}", request.method(), request.url().url(), e.getMessage(), e);
                throw new BizException("Http请求响应体解析异常：" + request.method() + " - " + request.url().url());
            }
        } finally {
            this.useTempClient.remove();
        }
    }

}
