package com.neil;

import java.util.Stack;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MethodHookInterceptor implements MethodInterceptor
{
    
    /**
     * 配置spring代理之后调用的方法
     */
    public Object invoke(MethodInvocation invocation)
        throws Throwable
    {
        MethodHookContext.initEnv();
        
        Object val = null;
        
        //要是能感知方法正确执行了，就非常有价值!
        try
        {
            val = invocation.proceed();
        }
        catch (Exception e)
        {
            MethodHookContext.clearHooks();
            throw e;
        }
        
        // 暂时只能捕获到运行时异常，但是不能感知hook方法是否正确执行完成
        
        Stack<MethodHook> hooks = MethodHookContext.popHooks();
        try
        {
            for (MethodHook methodHook : hooks)
            {
                methodHook.hook();
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        
        return val;
    }
    
}
