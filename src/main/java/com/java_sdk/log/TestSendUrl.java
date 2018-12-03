package com.java_sdk.log;

import java.util.UUID;

/**
 * @Auther: lyd
 * @Date: 2018/8/15 11:22
 * @Description:测试
 */
public class TestSendUrl {
    public static void main(String[] args) {
        String memberId =  UUID.randomUUID().toString();
        System.out.println("aaa");
        System.out.println(memberId);
        System.out.println(LogEngineSdk.Charge("123460", memberId, "cs"));
        System.out.println(LogEngineSdk.Charge("123460", memberId, "cr"));
    }
}
