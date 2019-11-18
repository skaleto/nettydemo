package im.client;

import im.client.nettyclient.NettyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication(scanBasePackages = "im.client")
public class ClientApplication {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = SpringApplication.run(ClientApplication.class, args);
        NettyClient client = context.getBean(NettyClient.class);
        client.connect();
    }
}
