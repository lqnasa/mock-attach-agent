package com.coder.agent.plugin.spring;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company:  Co., Ltd.
 * Create Time: 2022/5/23 22:09
 *
 * @author coderLee23
 */
@Slf4j
public class SpringTimeConsumeIntercept {

    @RuntimeType
    public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        long start = System.nanoTime();
        try {
            // 原有函数执行
            return callable.call();
        } finally {
            log.info("方法 [{}] 耗时 {} ms", method, (System.nanoTime() - start) / 1000_000D);
        }
    }
}