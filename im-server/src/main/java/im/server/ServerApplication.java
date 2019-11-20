package im.server;

import im.server.nettyserver.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import sun.rmi.runtime.Log;

/**
 * @author : ybyao
 * @Create : 2019-11-19 10:23
 */

@Slf4j
@SpringBootApplication(scanBasePackages = "im")
public class ServerApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(ServerApplication.class, args);

        log.info("test");

        NettyServer server = context.getBean(NettyServer.class);
        server.startServer();
    }
}
