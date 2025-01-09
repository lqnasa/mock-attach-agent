package com.coder.agent.plugin.spring;

import com.coder.agent.plugin.AgentPlugin;
import com.coder.agent.plugin.InterceptPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company:  Co., Ltd.
 * Create Time: 2022/5/23 15:55
 *
 * @author coderLee23
 */
public class SpringTimeConsumePlugin implements AgentPlugin {

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return nameContains("com.*.module.web")
                                .and(not(isInterface())).and(isAnnotatedWith(named("org.springframework.stereotype.Controller"))
                                        .or(isAnnotatedWith(named("org.springframework.web.bind.annotation.RestController"))));
                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isPublic()
                                .and(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)))
                                .and(ElementMatchers.not(ElementMatchers.isConstructor()));
                    }
                }
        };
    }

    @Override
    public Class<?> getAdviceClass() {
        return SpringTimeConsumeAdvice.class;
    }
}
