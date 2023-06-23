package vip.yeee.memo.demo.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/17 14:04
 */
@Slf4j
public class HelloWorldServer {

    private int port = 50051;
    private Server server;

    private void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();
        log.info("Server started, listening on " + port);
        // 当程序正常退出、系统调用 System.exit方法或者虚拟机被关闭时才会执行系统中已经设置的所有钩子
        // 当系统执行完这些钩子后，JVM才会关闭。
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            HelloWorldServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    // block 一直到退出程序
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            HelloReply response = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            log.info("Message from gRPC-Client:" + request.getName());
            log.info("Message Response:" + response.getMessage());
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HelloWorldServer server = new HelloWorldServer();
        server.start();
        server.blockUntilShutdown();
    }

}
