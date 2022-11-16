//package vip.yeee.memo.integrate.elasticsearch.config;
//
//import org.apache.http.HttpHost;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.stereotype.Component;
//
///**
// * ###不使用springboot-starter的情况###，构建配置类注入bean，手动构建elasticsearchRestTemplate
// *
// * @author yeeee
// * @since 2022/4/25 21:59
// */
//@Component
//public class ElasticsearchConfig {
//
//    @Value("${spring.elasticsearch.rest.uris}")
//    private String uris;
//    @Value("${spring.elasticsearch.rest.username}")
//    private String username;
//    @Value("${spring.elasticsearch.rest.password}")
//    private String password;
//
//    @Bean
//    public RestHighLevelClient restHighLevelClient() {
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
//        RestClientBuilder builder = RestClient.builder(HttpHost.create(uris))
//                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
//        return new RestHighLevelClient(builder);
//    }
//
//    @Bean
//    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
//        return new ElasticsearchRestTemplate(restHighLevelClient());
//    }
//
//}
