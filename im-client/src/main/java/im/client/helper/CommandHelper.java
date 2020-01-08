package im.client.helper;

import im.client.ClientController;
import im.client.MsgSender;
import im.client.session.ClientSession;
import im.client.session.SessionManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Scanner;

/**
 * @author : ybyao
 * @Create : 2020-01-08 17:46
 */
@Service
public class CommandHelper {

    @Resource
    private ClientController clientController;

    @Resource
    private SessionManager sessionManager;

    @Resource
    private MsgSender msgSender;


    public synchronized void waitCommandThread() throws InterruptedException {
        this.wait();
    }

    public synchronized void notifyCommandThread(){
        this.notify();
    }

    public void startCommandReader() throws Exception {
        while (true) {
            //建立连接
            while (sessionManager.getSession() == null) {
                //开始连接
                clientController.connectAndLogin();
                waitCommandThread();
            }

            System.out.println("请输入消息(user:msg)");
            Scanner scanner = new Scanner(System.in);
            String text = scanner.next();

            msgSender.sendMsg(text);


        }
    }

}
