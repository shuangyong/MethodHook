package com.neil;

import java.util.Stack;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MethodHookInterceptor implements MethodInterceptor
{
    
    /**
     * ����spring����֮����õķ���
     */
    public Object invoke(MethodInvocation invocation)
        throws Throwable
    {
        MethodHookContext.initEnv();
        
        Object val = null;
        
        //Ҫ���ܸ�֪������ȷִ���ˣ��ͷǳ��м�ֵ!
        try
        {
            val = invocation.proceed();
        }
        catch (Exception e)
        {
            MethodHookContext.clearHooks();
            throw e;
        }
        
        // ��ʱֻ�ܲ�������ʱ�쳣�����ǲ��ܸ�֪hook�����Ƿ���ȷִ�����
        
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
