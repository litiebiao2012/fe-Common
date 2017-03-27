package fe.common;

import java.util.ResourceBundle;

/**
 * Created by fe on 2017/2/6.
 */
public class FeCommonConfig {

    public static ResourceBundle resourceBundle = ResourceBundle.getBundle("fe_common_config");

    public static final String BEARY_CHAT_WEB_HOOK_URL = resourceBundle.getString("beary_chat_web_hook_url");


}
