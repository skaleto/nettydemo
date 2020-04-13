package im.client.helper;

import im.client.ClientController;
import im.client.MsgSender;
import im.client.session.ClientSession;
import im.client.session.SessionManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Scanner;

/**
 * 接收用户输入信息，转化成不同的命令
 *
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

    public synchronized void notifyCommandThread() {
        this.notify();
    }

    public void startCommandReader() throws Exception {
        while (true) {

            System.out.println("请输入指令:");
            Scanner scanner = new Scanner(System.in);
            String cmd = scanner.next();
            switch (cmd) {
                case "LOGIN":
                    System.out.println("请输入登录信息(ID:token)");
                    Scanner s = new Scanner(System.in);
                    String t = s.next();
                    String[] array=t.split(":");
                    //开始连接
                    clientController.connectAndLogin(array[0],array[1]);
                    waitCommandThread();
                    break;
                case "MSG":
                    if (sessionManager.getSession() == null
                            || "-1".equals(sessionManager.getSession().getSessionId())) {
                        System.out.println("未创建会话，请先登录");
                        break;
                    }
                    System.out.println("请输入消息(对方ID:消息)");
                    Scanner sc = new Scanner(System.in);
                    String text = sc.next();
                    msgSender.sendMsg(text);
                    break;
                default:
                    System.out.println("不支持的指令");
                    break;
            }

        }
    }

}
