# 工程简介
基于 Java 字节码增强库 ByteBuddy 实现 Mock 异常、修改返回值、性能监控、日志记录的研究

```text
1. 引言
   ByteBuddy 是一个强大的 Java 字节码操作库，它允许在不修改程序代码的情况下，动态地创建和修改 Java 类。ByteBuddy 提供了丰富的 API，可以用于实现 Mock 测试、修改方法返回值、性能监控和日志记录等功能。这些功能在软件开发和测试中具有重要的应用价值，可以显著提高开发效率和代码质量。

2. ByteBuddy 简介
   ByteBuddy 是一个代码生成和操作库，用于在 Java 应用程序运行时创建和修改 Java 类，而无需编译器的帮助。它提供了方便的 API，可以使用 Java 代理或在构建过程中手动更改类。ByteBuddy 的主要特点包括：

无需理解字节码指令：使用简单的 API 即可操作字节码，控制类和方法。
支持 Java 任何版本：库轻量，仅依赖于 Java 字节代码解析器库 ASM 的访问者 API，不需要其他依赖项。
性能优势：相比 JDK 动态代理、cglib、Javassist，ByteBuddy 在性能上具有一定的优势。

3. Mock 异常
   在测试中，Mock 异常是一种常见的需求。ByteBuddy 可以通过拦截方法调用并抛出指定的异常来实现这一功能。
[MockExceptionAdvice.java]
[MockExceptionPlugin.java]

4. 修改返回值
ByteBuddy 可以轻松地修改方法的返回值。
[FastjsonNameHashCode64Intercept.java]
[FastjsonNameHashCode64Plugin.java]

5. 性能监控
ByteBuddy 可以用于监控方法的执行耗时。
[SpringTimeConsumeAdvice.java]
[SpringTimeConsumePlugin.java]

6. 日志记录
ByteBuddy 可以用于在方法调用前后插入日志记录逻辑。
参考 ByteBuddy 可以用于监控方法的执行耗时。
```