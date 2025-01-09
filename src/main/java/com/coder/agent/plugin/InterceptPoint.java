package com.coder.agent.plugin;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company:  Co., Ltd.
 * Create Time: 2022/5/23 15:19
 *
 * @author coderLee23
 */
public interface InterceptPoint {

    /**
     * 类匹配规则
     *
     * @return ElementMatcher<TypeDescription>
     */
    ElementMatcher<TypeDescription> buildTypesMatcher();

    /**
     * 方法匹配规则
     *
     * @return ElementMatcher<MethodDescription>
     */
    ElementMatcher<MethodDescription> buildMethodsMatcher();

}
