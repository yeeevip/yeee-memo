package vip.yeee.memo.integrate.common.platformauth.client.configure;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.base.util.LogUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
//@EnableOAuth2Client
public class OAuth2ClientConfig {

	private final static Logger log = LogUtils.commonAuthLog();
	@Resource
	private OkHttpClient okHttpClient;

	@ConfigurationProperties(prefix = "security.oauth2.client")
	@Bean
	public OAuth2ProtectedResourceDetails resourceOwnerPasswordResourceDetails() {
		return new ResourceOwnerPasswordResourceDetails();
	}

	@Bean("oAuth2LbRestTemplate")
	@LoadBalanced
	public RestTemplate oAuth2LbRestTemplate() {
		OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				if(response.getRawStatusCode() != 400){
					super.handleError(response);
				} else {
					String body = new String(super.getResponseBody(response), StandardCharsets.UTF_8);
					log.info("body = {}", body);
					throw new BizException(body);
				}
			}
		});
		return restTemplate;
	}

	@Bean
	public OAuth2RestTemplate oAuth2RestTemplate() {
		OAuth2RestTemplate template = new OAuth2RestTemplate(resourceOwnerPasswordResourceDetails());
		ResourceOwnerPasswordAccessTokenProvider accessTokenProvider = new ResourceOwnerPasswordAccessTokenProvider() {
			@Override
			protected RestOperations getRestTemplate() {
				RestTemplate restTemplate = oAuth2LbRestTemplate();
				super.setMessageConverters(restTemplate.getMessageConverters());
				return restTemplate;
			}
		};
		template.setAccessTokenProvider(accessTokenProvider);
		return template;
	}
}