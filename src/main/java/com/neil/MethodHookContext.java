package com.neil;

import java.util.Stack;

/**
 * MethodHook��context��
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
     * ֱ�ӵ���hook��������ͨ��Spring�ĵ��÷�ʽ
     */
    public static void hookDirectly(MethodHook methodHook)
    {
        if (hooks.get() == null || hooks.get().isEmpty())
        {
            methodHook.hook();
            return;
        }
        // �����spring�������˴������ǵ����˸÷�������ע���hookһ��ִ�еĻ���
        
        hookByProxy(methodHook);
        
    }
    
    /**
     * ͨ��spring�������ص��ֶ�ȥ����methodHook������ֱ�ӵ��õķ�����û��ִ�еĻ�����
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
