package com.coder.agent;

import com.coder.agent.plugin.AgentPlugin;
import com.coder.agent.plugin.InterceptPoint;
import com.coder.agent.plugin.PluginFactory;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import static net.bytebuddy.matcher.ElementMatchers.isAbstract;
import static net.bytebuddy.matcher.ElementMatchers.isInterface;

@Log4j2
public class ByteBuddyPluginAgent {

    public static void agentInit() {
        ByteBuddyAgent.install();

        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .disableClassFormatChanges()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                //过滤接口
                .ignore(isInterface())
                //过滤抽象类
                .ignore(isAbstract())
                //过滤枚举类
                .ignore(ElementMatchers.isEnum())
//                .ignore(nameStartsWith("net.bytebuddy.")
//                        .or(nameStartsWith("org.slf4j."))
//                        .or(nameStartsWith("org.apache.logging."))
//                        .or(nameStartsWith("org.groovy."))
//                        .or(nameContains("javassist"))
//                        .or(nameContains(".asm."))
//                        .or(nameStartsWith("sun.reflect"))
//                        .or(isSynthetic()))

//                .ignore(any(), ClassLoaderNameMatcher.isReflectionClassLoader())
//                .or(nameStartsWith("org.aspectj."))
//                .or(nameStartsWith("org.groovy."))
//                .or(nameStartsWith("com.p6spy."))
//                .or(nameStartsWith("net.bytebuddy."))
//                .or(nameStartsWith("com.coder.cdc.com.coder.agent."))
//                // AppDynamics
//                .or(nameContains("javassist"))
//                .or(nameContains(".asm."))
//                .or(nameContains(".apache."))
//                .or(nameContains(".tomcat."))

                .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly()) //
                .with(AgentBuilder.InstallationListener.StreamWriting.toSystemError());


        for (AgentPlugin agentPlugin : PluginFactory.PLUGIN_GROUP_LIST) {

            for (InterceptPoint interceptPoint : agentPlugin.buildInterceptPoint()) {
                AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> {
                    log.info("PLUGIN_GROUP_LIST interceptPoint buildMethodsMatcher ============> {}", interceptPoint.buildMethodsMatcher());
                    builder = builder.visit(Advice.to(agentPlugin.getAdviceClass()).on(interceptPoint.buildMethodsMatcher()));
                    return builder;
                };
                log.info("PLUGIN_GROUP_LIST interceptPoint buildTypesMatcher ============> {}", interceptPoint.buildTypesMatcher());
                agentBuilder = agentBuilder.type(interceptPoint.buildTypesMatcher()).transform(transformer).asTerminalTransformation();
            }
        }

        for (AgentPlugin agentPlugin : PluginFactory.PLUGIN_INTERCEPT_GROUP_LIST) {
            for (InterceptPoint interceptPoint : agentPlugin.buildInterceptPoint()) {
                AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> {
                    log.info("PLUGIN_INTERCEPT_GROUP_LIST interceptPoint buildMethodsMatcher ============> {}", interceptPoint.buildMethodsMatcher());
                    builder = builder.method(interceptPoint.buildMethodsMatcher()) // 拦截任意方法
                            .intercept(MethodDelegation.to(agentPlugin.getAdviceClass())); // 委托
                    return builder;
                };
                log.info("PLUGIN_INTERCEPT_GROUP_LIST interceptPoint buildTypesMatcher ============> {}", interceptPoint.buildTypesMatcher());
                agentBuilder = agentBuilder.type(interceptPoint.buildTypesMatcher()).transform(transformer).asTerminalTransformation();
            }
        }


        AgentBuilder.Listener listener = new AgentBuilder.Listener() {

            @Override
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//                log.info("onDiscovery ==> {}", typeName);
            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded,
                                         DynamicType dynamicType) {
                log.info("onTransformation ==> {}", typeDescription.getSimpleName());
            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
//                log.info("onIgnored ==> {}", typeDescription.getName());
            }

            @Override
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
//                System.out.println("onError ==> " + typeName);
                log.error("onError " + typeName, throwable);
            }

            @Override
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

//                System.out.println("onComplete ==> " + typeName);
            }
        };

        //https://blog.51cto.com/u_15310381/3226215
        agentBuilder.with(listener).installOnByteBuddyAgent();
    }

}
