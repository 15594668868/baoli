package utils;

import java.security.MessageDigest;

public class Md5Util {
    public static String md5(String source) {
        StringBuffer sb = new StringBuffer(32);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(source.getBytes("utf-8"));

            for(int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString(array[i] & 255 | 256).toUpperCase().substring(1, 3));
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }

        return sb.toString();
    }
}
