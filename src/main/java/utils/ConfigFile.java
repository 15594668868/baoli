package utils;



import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigFile {
    public static ResourceBundle bundle = ResourceBundle.getBundle("application", Locale.CHINA);

    /**
     * 传入name获取测试地址+值
     * @param name
     * @return
     */
    public static String getUrl(String name){
        String address = bundle.getString("platform.url");
        String uri = "";
        //测试地址
        String testUrl;
        uri = bundle.getString(name);
        testUrl = address + uri;
        return testUrl;
    }

    /**
     * 传入name获取值
     * @param name
     * @return
     */
    public static String getValue(String name){
        return bundle.getString(name);
    }
}
