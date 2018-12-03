package com.java_sdk.log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Auther: lyd
 * @Date: 2018/8/15 09:47
 * @Description:后台收集日志的sdk
 */
public class LogEngineSdk {
    //获取日志打印对象
    private final static Logger logger = Logger.getGlobal();

    private final static String accuess_url = "http://192.168.243.130/index.html";
    private final static String ver = "1.0";
    private final static String platformName = "java_server";
    private final static String sdkName = "java_sdk";

    /**
     * 支付成功或者退款成功事件，
     *
     * @param oid
     * @param mid
     * @param flag cs:支付成功，默认使用该类型    cr：退款成功
     * @return 返回true：支付成功，false:支付失败
     */
    public static boolean Charge(String oid, String mid, String flag) {
        try {
            if (isEmpty(oid) || isEmpty(mid)) {
                logger.log(Level.WARNING, "oid&mid may be null.oid:" + oid + " mid:" + mid);
                return false;
            }
            //正常支付   构建url：http://192.168.216.111/index.html?en=e_cs&ver=1.0&pl=java_server
            Map<String, String> info = new HashMap<String, String>();
            //将需要发送的参数添加到info中
            info.put("u_mid", mid);
            info.put("oid", oid);
            info.put("ver", ver);
            if (flag.equals("cr")) {
                info.put("en", "e_cr");
            } else {
                info.put("en", "e_cs");
            }

            info.put("pl", platformName);
            info.put("sdk", sdkName);
            //构建url
            String url = buildUrl(info);

            //将构建好的url添加到发送队列中
            SendUrl.getSendUrl().addUrlToQueue(url);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 构建url：http://192.168.216.111/index.html?en=e_cs&ver=1.0&pl=java_server
     *
     * @param info
     * @return
     */
    private static String buildUrl(Map<String, String> info) {
        StringBuffer sb = new StringBuffer();
        sb.append(accuess_url).append("?");
        if (!info.isEmpty()) {
            for (Map.Entry<String, String> en : info.entrySet()) {
                if (isNotEmpty(en.getKey())) {
                    try {
                        String value = URLEncoder.encode(en.getValue(), "utf-8");
                        sb.append(en.getKey()).append("=").append(value).append("&");
                    } catch (UnsupportedEncodingException e) {
                        logger.log(Level.WARNING, "编码异常");
                    }
                }
            }
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    /**
     * 判断是否为空
     *
     * @param intput
     * @return
     */
    private static boolean isEmpty(String intput) {
        return intput == null || intput.trim().isEmpty();
    }

    //判断是否非空
    private static boolean isNotEmpty(String intput) {
        return !isEmpty(intput);
    }
}
