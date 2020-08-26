package com.tparch.autoretry;

public interface IConfigCenter {
    /**
     * 从配置中心获取调用key，每次rpc都调用此方法，需保证性能<br>
     * 如果不需要重试，useBak为true时，需返回null<br>
     * @param configId
     * @param groupName
     * @param useBak
     * @return jsf consumer实例的key
     */
     String getConsumerKey(String configId, String groupName, boolean useBak);

    /**
     * 是否需要打印错误日志<br>
     * @param configId
     * @param groupName
     * @param useBak
     * @return jsf consumer实例的key
     */
     boolean needPrintErrorLog(String configId, String groupName, boolean useBak);
}
