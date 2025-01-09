package com.coder.agent.plugin.exception;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Constructor;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company:  Co., Ltd.
 * Create Time: 2022/5/18 16:50
 *
 * @author coderLee23
 */
@Slf4j
public class MockExceptionAdvice {

    private MockExceptionAdvice() {
    }


    @Advice.OnMethodExit
    public static void exit() throws Throwable {
        Class<?> aClass = Class.forName("org.springframework.dao.IncorrectResultSizeDataAccessException");
        Constructor<?> constructor = aClass.getConstructor(String.class, int.class, int.class);
        Object o = constructor.newInstance("mock exception", 1, 2);
        throw (Throwable) o;
    }

}
