package com.coder.agent.plugin;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company:  Co., Ltd.
 * Create Time: 2022/5/23 15:19
 *
 * @author coderLee23
 */
public interface AgentPlugin {

    /**
     * 监控点
     *
     * @return InterceptPoint[]
     */
    InterceptPoint[] buildInterceptPoint();

    /**
     * AdviceClass
     *
     * @return Class
     */
    Class<?> getAdviceClass();
}
