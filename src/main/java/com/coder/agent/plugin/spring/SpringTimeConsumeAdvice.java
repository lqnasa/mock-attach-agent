package com.coder.agent.plugin.spring;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company:  Co., Ltd.
 * Create Time: 2022/5/18 16:50
 *
 * @author coderLee23
 */
@Slf4j
public class SpringTimeConsumeAdvice {

    private SpringTimeConsumeAdvice() {
    }

    @Advice.OnMethodEnter(inline = false)
    public static Long enter() {
        return System.nanoTime();
    }


    @Advice.OnMethodExit(inline = false, onThrowable = Throwable.class)
    public static void exit(@Advice.Origin Method method, @Advice.Enter Long l) {
        if (Objects.isNull(l)) {
            return;
        }
        double v = (System.nanoTime() - l) / 1000_000D;
        Class<?> declaringClass = method.getDeclaringClass();
        String basePath = declaringClass.getAnnotation(RequestMapping.class).value()[0];
        String requestPath = getPath(method);
        String path = String.format("%s/%s", basePath, requestPath);
        String name = method.getName();
        log.info(" 请求地址: {} 类名：{} 方法名称：{} 耗时 {} ms", path.replaceAll("/+", "/"), declaringClass.getName(), name, v);
    }

    private static String getPath(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (!Objects.isNull(requestMapping)) {
            return requestMapping.value()[0];
        }
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (!Objects.isNull(getMapping)) {
            return getMapping.value()[0];
        }
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        if (!Objects.isNull(putMapping)) {
            return putMapping.value()[0];
        }
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (!Objects.isNull(postMapping)) {
            return postMapping.value()[0];
        }
        PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
        if (!Objects.isNull(patchMapping)) {
            return patchMapping.value()[0];
        }
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if (!Objects.isNull(deleteMapping)) {
            return deleteMapping.value()[0];
        }
        return "";
    }
}
