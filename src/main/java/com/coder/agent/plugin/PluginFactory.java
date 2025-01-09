package com.coder.agent.plugin;


import com.coder.agent.plugin.exception.MockExceptionPlugin;
import com.coder.agent.plugin.spring.SpringTimeConsumePlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company:  Co., Ltd.
 * Create Time: 2022/5/23 15:19
 * @author coderLee23
 */
public class PluginFactory {

    public static final List<AgentPlugin> PLUGIN_GROUP_LIST = new ArrayList<>();

    public static final List<AgentPlugin> PLUGIN_INTERCEPT_GROUP_LIST = new ArrayList<>();

    static {
        // 耗时监控
        PLUGIN_GROUP_LIST.add(new SpringTimeConsumePlugin());
//        PLUGIN_INTERCEPT_GROUP_LIST.add(new SpringTimeConsumePlugin());
        PLUGIN_GROUP_LIST.add(new MockExceptionPlugin());

    }

}
