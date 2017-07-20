package com.heitian.ssm.controller;

import org.springframework.stereotype.Component;

/**
 * Created by buj on 2017/6/29.
 */
@Component
public class timing {

//定时任务
    public void job1() {
        System.out.println("===============开始=================");
        System.out.println("===============结束=================");
    }
}
