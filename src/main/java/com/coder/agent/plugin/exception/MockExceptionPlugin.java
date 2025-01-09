package com.coder.agent.plugin.exception;

import com.coder.agent.plugin.AgentPlugin;
import com.coder.agent.plugin.InterceptPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company:  Co., Ltd.
 * Create Time: 2023/11/09 11:35
 *
 * @author coderLee23
 */
public class MockExceptionPlugin implements AgentPlugin {

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return named("com.api.UserAPIImpl");
                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return named("createDomainUser");
                    }
                }
        };
    }

    @Override
    public Class<?> getAdviceClass() {
        return MockExceptionAdvice.class;
    }

}
