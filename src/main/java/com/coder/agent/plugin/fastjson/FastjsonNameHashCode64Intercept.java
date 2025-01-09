package com.coder.agent.plugin.fastjson;

import com.alibaba.fastjson.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company:  Co., Ltd.
 * Create Time: 2022/5/23 22:09
 *
 * @author coderLee23
 */
@Slf4j
public class FastjsonNameHashCode64Intercept {

    @RuntimeType
    public static Object intercept(@Advice.Argument(0) String name) throws Exception {
        log.info("拦截 nameHashCode64 ,参数 {}", name);
        return TypeUtils.fnv1a_64_extract(name);
    }

}