package im.client;

import im.client.helper.CommandHelper;
import im.client.nettyclient.NettyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication(scanBasePackages = "im")
public class ClientApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(ClientApplication.class, args);
        CommandHelper commandHelper = context.getBean(CommandHelper.class);
        commandHelper.startCommandReader();

    }
}
