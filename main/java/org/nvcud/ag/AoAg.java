package org.nvcud.ag;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import java.lang.instrument.Instrumentation;
import java.util.Arrays;

public class AoAg {

    static {
        try {
            Instrumentation inst = ByteBuddyAgent.install();
            new AgentBuilder.Default()
                    .type(ElementMatchers.nameStartsWith("com.mowan.circle.service.processor"))
                    .transform((builder, typeDescription, classLoader, module) ->
                            builder.method(ElementMatchers.any())
                                    .intercept(Advice.to(MethodInterceptor.class))
                    )
                    .installOn(inst);

        } catch (Exception e) {

        }
    }

    // 拦截器
    public static class MethodInterceptor {

        @Advice.OnMethodEnter
        public static void onEnter(@Advice.Origin String method,
                                   @Advice.AllArguments Object[] args) {
            System.out.println("Enter method: " + method);
            System.out.println("Arguments: " + Arrays.toString(args));
        }

        @Advice.OnMethodExit(onThrowable = Throwable.class)
        public static void onExit(@Advice.Origin String method,
                                  @Advice.Return Object ret,
                                  @Advice.Thrown Throwable throwable) {
            if (throwable != null) {
                System.out.println("Method " + method + " threw: " + throwable);
            } else {
                System.out.println("Exit method: " + method + ", Return: " + ret);
            }
        }
    }
}

