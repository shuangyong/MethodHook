package com.neil;

import java.util.Stack;

/**
 * MethodHook的context类
 * 
 * @author neil.zhang
 */
public class MethodHookContext
{
    private static ThreadLocal<Stack<MethodHook>> hooks = new ThreadLocal<Stack<MethodHook>>();
    
    public static void initEnv()
    {
        if (hooks.get() == null)
        {
            hooks.set(new Stack<MethodHook>());
        }
    }
    
    /**
     * 直接调用hook方法，不通过Spring的调用方式
     */
    public static void hookDirectly(MethodHook methodHook)
    {
        if (hooks.get() == null || hooks.get().isEmpty())
        {
            methodHook.hook();
            return;
        }
        // 如果在spring中配置了代理，但是调用了该方法，给注册的hook一个执行的机会
        
        hookByProxy(methodHook);
        
    }
    
    /**
     * 通过spring方法拦截的手段去调用methodHook，但是直接调用的方法就没有执行的机会了
     * 
     */
    public static void hookByProxy(MethodHook methodHook)
    {
        if (hooks.get() == null)
        {
            throw new RuntimeException("Hook Env Not Initlizing!");
        }
        
        hooks.get().add(methodHook);
    }
    
    public static Stack<MethodHook> popHooks()
    {
        return hooks.get();
    }
    
    public static void clearHooks()
    {
        hooks.set(null);
        initEnv();
    }
    
}
