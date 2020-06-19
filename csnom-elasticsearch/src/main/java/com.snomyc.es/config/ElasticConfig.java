package com.snomyc.es.config;


import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

	@Value("${spring.elasticsearch.cluster.port}")
    public int port;

    @Value("${spring.elasticsearch.cluster.host}")
    private String clusterHost;

    @Value("${spring.elasticsearch.cluster.username}")
    private String userName;
    @Value("${spring.elasticsearch.cluster.password}")
    private String password;


	 
	@Value("${spring.elasticsearch.cluster.scheme}")
    public String scheme;




    public static int CONNECT_TIMEOUT_MILLIS = 1000;
    public static int SOCKET_TIMEOUT_MILLIS = 30000;
    public static int CONNECTION_REQUEST_TIMEOUT_MILLIS = 1000;
    public static int MAX_CONN_PER_ROUTE = 0;
    public static int MAX_CONN_TOTAL = 0;


    @Bean
    public RestClientBuilder restClientBuilder() {
        return RestClient.builder(makeHttpHost());
    }

    @Bean
    public RestClient elasticsearchRestClient(){

        RestClientBuilder clientBuilder = restClientBuilder();
        return clientBuilder.build();
    }

    private HttpHost[] makeHttpHost() {
        String[] cHost = clusterHost.split(",");
        //分割集群节点
        HttpHost[] httpHostArray = new HttpHost[cHost.length];
        for (int i=0;i<cHost.length;i++) {
            httpHostArray[i] = new HttpHost(cHost[i],port, scheme);
        }
        return  httpHostArray;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(@Autowired RestClientBuilder clientBuilder){
// 添加其他配置，都是可选，比如设置请求头，每个请求都会带上这个请求头
        Header[] defaultHeaders = {new BasicHeader("header", "value")};
        clientBuilder.setDefaultHeaders(defaultHeaders);
        /*
            配置请求超时，将连接超时（默认为1秒）和套接字超时（默认为30秒）增加，
            这里配置完应该相应地调整最大重试超时（默认为30秒），即上面的setMaxRetryTimeoutMillis，一般于最大的那个值一致即60000
         */

        // 配置连接时间延时
        clientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
            return requestConfigBuilder;
        });

               /*
       如果ES设置了密码，那这里也提供了一个基本的认证机制，下面设置了ES需要基本身份验证的默认凭据提供程序
        */
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(userName, password));
        // 使用异步httpclient时设置并发连接数
        clientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });

        return new RestHighLevelClient(clientBuilder);
    }
}
