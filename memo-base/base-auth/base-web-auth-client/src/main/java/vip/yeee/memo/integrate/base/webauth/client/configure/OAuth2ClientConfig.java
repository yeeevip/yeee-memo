package vip.yeee.memo.integrate.base.webauth.client.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import vip.yeee.memo.integrate.common.model.exception.BizException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
//@EnableOAuth2Client
public class OAuth2ClientConfig {

	@ConfigurationProperties(prefix = "security.oauth2.client")
	@Bean
	public OAuth2ProtectedResourceDetails resourceOwnerPasswordResourceDetails() {
		return new ResourceOwnerPasswordResourceDetails();
	}

	@Bean("oAuth2LbRestTemplate")
	@LoadBalanced
	public RestTemplate oAuth2LbRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
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