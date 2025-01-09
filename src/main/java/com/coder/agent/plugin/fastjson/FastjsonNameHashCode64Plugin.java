package com.coder.agent.plugin.fastjson;

import com.coder.agent.plugin.AgentPlugin;
import com.coder.agent.plugin.InterceptPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;


import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company:  Co., Ltd.
 * Create Time: 2022/5/23 15:55
 *
 * @author coderLee23
 */
public class FastjsonNameHashCode64Plugin implements AgentPlugin {

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                // jpa
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return named("com.alibaba.fastjson.util.FieldInfo");
                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return named("nameHashCode64");
                    }
                }
        };
    }

    @Override
    public Class<?> getAdviceClass() {
        return FastjsonNameHashCode64Intercept.class;
    }
}
