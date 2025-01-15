package com.sametp.els;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.util.Properties;


public final class ESConfig {
    private static final Properties properties = new Properties();
    static {
        try (InputStream input = Application.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.clear();
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find application.properties");
            }
            properties.load(input);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static ESConnectionInfo getESConnectionInfo(){
        String hostname = properties.getProperty("es.hostname");
        Integer port = Integer.valueOf(properties.getProperty("es.port"));
        String username = properties.getProperty("es.username");
        String password = properties.getProperty("es.password");
        return new ESConnectionInfo(hostname, port, username, password);
    }
    public static ElasticsearchClient getEsRestClient() {
       try{
           ESConnectionInfo connectionInfo = getESConnectionInfo();
           BasicCredentialsProvider credentialsProvider = new  BasicCredentialsProvider();
           UsernamePasswordCredentials credentials =  new UsernamePasswordCredentials(connectionInfo.getUsername(), connectionInfo.getPassword());
           SSLContext sslContext = SSLContexts.custom()
                   .loadTrustMaterial(new TrustAllStrategy())
                   .build();
           credentialsProvider.setCredentials(AuthScope.ANY,credentials);
           RestClientBuilder builder = RestClient.builder(new HttpHost(connectionInfo.getHostname(),connectionInfo.getPort(),"http"))
                   .setHttpClientConfigCallback(httpAsyncClientBuilder ->
                       httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                               .setSSLContext(sslContext)
                               .setSSLHostnameVerifier(new NoopHostnameVerifier())
                               .setDefaultIOReactorConfig(
                                       IOReactorConfig.custom()
                                               .setIoThreadCount(1)
                                               .build()
                               )
                   );
           return new ElasticsearchClient(
                   new RestClientTransport(builder.build(), new JacksonJsonpMapper())
           );
       }catch (Exception e){
           throw new RuntimeException(e);
       }

    }
    public static class ESConnectionInfo{
        private String hostname;
        private Integer port;
        private String username;
        private String password;
        private ESConnectionInfo(){}
        public ESConnectionInfo(String hostname,Integer port, String username, String password){
            this.hostname = hostname;
            this.port = port;
            this.username = username;
            this.password = password;
        }

        public String getHostname() {
            return hostname;
        }

        public Integer getPort() {
            return port;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
