package fe.common.alarm;


import fe.common.FastJson;
import fe.common.http.HttpClientUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fe on 2017/2/5.
 */
public class BearyChartAlarm implements Alarm {
    @Override
    public void alarm(String content, String... receiver) {
        String uri = "https://hook.bearychat.com/=bw9AN/incoming/93e48775ba7021c70e5207955a18c211";
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("text",content);
        try {
            HttpClientUtils.postParameters(uri, FastJson.toJson(paramMap),"application/json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BearyChartAlarm bearyChartAlarm = new BearyChartAlarm();
        bearyChartAlarm.alarm("测试测试123","","");
    }
}
