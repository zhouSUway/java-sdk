package com.java_sdk.log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Auther: lyd
 * @Date: 2018/8/15 10:23
 * @Description: 将队列中的url发送出去的类
 */
public class SendUrl {
    //获取日志打印对象
    private final static Logger logger = Logger.getGlobal();
    //获取一个队列   用于存储url
    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    //创建单利对象
    private static SendUrl sendUrl = null;

    //私有构造器
    private SendUrl() {

    }

    //共有的方法
    public static SendUrl getSendUrl() {
        if (sendUrl == null) {
            //添加同步代码块，防止两个线程同时获取对象
            synchronized (SendUrl.class) {
                if (sendUrl == null) {
                    sendUrl = new SendUrl();

                    //TODO 创建独立线程去发送url
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SendUrl.sendUrl.consumeUrl(); //线程独立消费url
                        }
                    });
                    //启动线程
//                    th.setDaemon(true);  //是否挂载在后台启动,当成守护线程，一般在服务器中可以使用
                    th.start();
                }
            }
        }
        return sendUrl;
    }

    /**
     * 添加url到队列中
     *
     * @param url
     */
    public static void addUrlToQueue(String url) {
        try {
            getSendUrl().queue.put(url);
//            getSendUrl().queue.add(url);
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "添加url到队列异常");
        }
    }

    /**
     * 从队列中获取url,并将url发送出去
     */
    public static void consumeUrl() {
        while (true) {
            try {
                String url = getSendUrl().queue.take();
                //调用发送
                HttpRequestUtil.requestUrl(url);
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "从队列中获取url异常");
            }
        }
    }


    /**
     * 用于发送url的类
     */
    public static class HttpRequestUtil {

        /**
         * 真正发送url
         *
         * @param url
         */
        public static void requestUrl(String url) {
            HttpURLConnection conn = null;
            InputStream is = null;

            try {
                //获取url
                URL u = new URL(url);
                conn = (HttpURLConnection) u.openConnection();
                //设置conn的属性
                conn.setRequestMethod("GET");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                //conn请求
                is = conn.getInputStream();
            } catch (Exception e) {
                logger.log(Level.WARNING, "真正发送url异常");
            } finally {
                conn.disconnect();
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        //do nothing
                    }
                }
            }

        }
    }

}
