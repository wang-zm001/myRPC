package com.wzm.myrpc.client.discovery;

import java.util.List;

/**
 * 服务发现接口，定义服务发现规范
 * @date 2022/4/24
 * @author xiaoming
 */
public interface ServiceDiscovery {
    List<String> getServices(String name);
}
