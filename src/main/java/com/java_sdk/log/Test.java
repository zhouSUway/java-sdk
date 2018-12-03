package com.java_sdk.log;

import java.util.UUID;

/**
 * @ClassName Test
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description //TODO $
 **/
public class Test {
    public static void main(String[] args) {
        String memberId =  UUID.randomUUID().toString();
        System.out.println("aaa");
        System.out.println(memberId);
        System.out.println(LogEngineSdk.Charge("123460", memberId, "cs"));
        System.out.println(LogEngineSdk.Charge("123460", memberId, "cr"));
    }
}