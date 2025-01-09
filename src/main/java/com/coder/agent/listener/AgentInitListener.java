package com.coder.agent.listener;

import com.coder.agent.ByteBuddyPluginAgent;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@Log4j2
public class AgentInitListener implements ApplicationListener<ApplicationReadyEvent> {


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        // 1、统计url请求接口耗时 2.mock异常 3.修改返回值
        System.out.println("==============byte buddy plugin start =================");
        ByteBuddyPluginAgent.agentInit();
        log.info("finished byte buddy installation.");
        System.out.println("==============byte buddy plugin end=================");
    }


}