package cn.izis.util;//package cn.izis.util;
//
//import java.util.Locale;
//import java.util.MissingResourceException;
//import java.util.ResourceBundle;
//
//public final class ResourceBundleUtil {
//    private static final ResourceBundle resource;
//
//    static {
//        resource = ResourceBundle.getBundle("string.string", Locale.CHINESE);
//    }
//
//    private ResourceBundleUtil() {
//
//    }
//
//    public static String getStringValue(String key) {
//        try {
//            return resource.getString(key);
//        } catch (MissingResourceException e) {
//            e.printStackTrace();
//            return "null";
//        }
//    }
//
//    public static Integer getIntegerValue(String key) {
//        try {
//            return Integer.valueOf(resource.getString(key));
//        } catch (MissingResourceException e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }
//
//
//}
