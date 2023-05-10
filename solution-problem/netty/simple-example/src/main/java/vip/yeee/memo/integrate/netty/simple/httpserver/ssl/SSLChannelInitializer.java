package vip.yeee.memo.integrate.netty.simple.httpserver.ssl;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import vip.yeee.memo.integrate.netty.simple.httpserver.HttpHandler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 *
 * @author yeeee
 * @since 2021/12/13 18:28
 */
public class SSLChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslContext;

    public SSLChannelInitializer() {
        String keyStoreFilePath = "/root/.ssl/test.pkcs12";
        String keyStorePassword = "Password@123";

        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(keyStoreFilePath), keyStorePassword.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

            sslContext = SslContextBuilder.forServer(keyManagerFactory).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
        pipeline
                .addLast(new SslHandler(sslEngine))
                .addLast("decoder", new HttpRequestDecoder())
                .addLast("encoder", new HttpResponseEncoder())
                .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                .addLast("handler", new HttpHandler());
        ;
    }
}
