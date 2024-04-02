package vip.yeee.memo.common.dingtalk.component;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import vip.yeee.memo.common.dingtalk.properties.DingtalkProperty;

import javax.annotation.Resource;
import java.util.Map;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/6/20 10:03
 */
@Slf4j
@Component("baseDingtalkService")
public class DingtalkService {

    @Resource
    protected DingtalkProperty dingtalkProperty;

    protected Map<String, String> chatGroup() {
        return dingtalkProperty.getChatGroup();
    }

    public void sendChatGroupMsg(String group, String msg) {
        if (!StringUtils.hasText(group) || !StringUtils.hasText(chatGroup().get(group)) || !StringUtils.hasText(msg)) {
            log.warn("【钉钉群机器人】参数缺失！");
        }
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=" + chatGroup().get(group));
        OapiRobotSendRequest req = new OapiRobotSendRequest();
        OapiRobotSendResponse rsp;
        String respMsg = null;
        try {
            req.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent(msg);
            req.setText(text);
            rsp = client.execute(req);
            respMsg = rsp != null ? String.format("errCode：%s，errMsg：%s，body：%s", rsp.getErrcode(), rsp.getErrmsg(), rsp.getBody()) : null;
            log.info("【钉钉群机器人】group = {}，response = {}", group, respMsg);
        } catch (Exception e) {
            log.error("【钉钉群机器人】group = {}，response = {}", group, respMsg, e);
        }
    }

}
