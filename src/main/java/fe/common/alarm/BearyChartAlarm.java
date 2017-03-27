package fe.common.alarm;


import fe.common.Assert;
import fe.common.FastJson;
import fe.common.FeCommonConfig;
import fe.common.http.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fe on 2017/2/5.
 */
public class BearyChartAlarm implements Alarm {

    public static final Logger logger = LoggerFactory.getLogger(BearyChartAlarm.class);

    @Override
    public void alarm(String content, String... receiver) {
        Assert.assertNotNull(content);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("text",content);
        try {
            HttpClientUtils.postParameters(FeCommonConfig.BEARY_CHAT_WEB_HOOK_URL, FastJson.toJson(paramMap),"application/json");
        } catch (Exception e) {
            logger.error("alarm error , e : {}",e);
            throw new RuntimeException("alarm error!",e);
        }
    }
}
