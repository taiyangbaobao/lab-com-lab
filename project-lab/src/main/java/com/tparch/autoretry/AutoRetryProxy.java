package com.tparch.autoretry;

import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

@Data
public class AutoRetryProxy implements MethodInterceptor {
    private static final Logger log	= LoggerFactory.getLogger(AutoRetryProxy.class);

    private Map<String,ServiceConfig> services;
    private String appName;
    private String umpKeyPrefix;
    private String group;
    private IConfigCenter configCenter;

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        String interfaceName = mi.getMethod().getDeclaringClass().getName();
        String methodName = mi.getMethod().getName();
        String key = appName + "." +umpKeyPrefix + "." + group + "." + interfaceName + "." + methodName;
        ServiceConfig config = services.get(interfaceName);
        try{
            Object service = null;
            String mainKey = configCenter.getConsumerKey(config.getConfigId(), group, false);
            if(mainKey == null || (service = config.getConsumers().get(mainKey)) == null){ //未配置或者配置了错误的mainKey
                log.error("rpc first invoke mainKey is null or getConsumer by mainKey:[{}] is null, defaultKey used!", mainKey);
                String defaultKey = config.getDefaultConsumerKey();
                if(defaultKey == null || (service = config.getConsumers().get(defaultKey)) == null){ //未配置或者配置了错误的defaultKey
                    log.error("rpc first invoke defaultKey is null or getConsumer by defaultKey:[{}] is null!", defaultKey);
                    throw new Exception("can not get consumer!");
                }
            }
            return mi.getMethod().invoke(service, mi.getArguments());
        }catch(Exception e){
            Throwable cause = e;
            if(e instanceof InvocationTargetException){
                cause = e.getCause();
            }
            if(configCenter.needPrintErrorLog(config.getConfigId(), group, false)){
                log.error("rpc first invoke error:", cause);
            }
            if(!(cause instanceof Exception)){ //只在发生rpc异常时重试，其他异常不重试直接抛出
                throw cause;
            }

            Set<String> retryMethods = config.getRetryMethods();
            if(retryMethods != null && !retryMethods.isEmpty() && !retryMethods.contains(methodName)){
                throw cause;
            }

            //未配置重试key，则不需要重试调用
            String bakKey = configCenter.getConsumerKey(config.getConfigId(), group, true);
            if(bakKey == null || bakKey.isEmpty()){
                throw cause;
            }
            Object service = config.getConsumers().get(bakKey);
            if(service == null){ //配置了错误的bakKey
                log.error("rpc second inovke getConsumer by bakKey:[{}] is null!", bakKey);
                throw cause;
            }

            try{
                return mi.getMethod().invoke(service, mi.getArguments());
            }catch(Exception eRetry){
                Throwable causeRetry = eRetry;
                if(eRetry instanceof InvocationTargetException){
                    causeRetry = eRetry.getCause();
                }
                if(configCenter.needPrintErrorLog(config.getConfigId(), group, true)){
                    log.error("rpc second invoke error:", causeRetry);
                }
                throw causeRetry;
            } finally {
            }
        }finally{
        }
    }
}
