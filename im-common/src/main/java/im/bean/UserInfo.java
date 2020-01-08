package im.bean;

import im.proto.IMMsg;
import lombok.Data;

/**
 * @author : ybyao
 * @Create : 2020-01-08 11:26
 */
@Data
public class UserInfo {

    private String uid;

    private String token;

    private String deviceId;

    private String appVersion;

    private String platform;

    public UserInfo(String uid, String token, String deviceId, String appVersion, String platform) {
        this.uid = uid;
        this.token = token;
        this.deviceId = deviceId;
        this.appVersion = appVersion;
        this.platform = platform;
    }

}
