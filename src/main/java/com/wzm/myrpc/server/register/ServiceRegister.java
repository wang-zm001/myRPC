package com.wzm.myrpc.server.register;

public interface ServiceRegister {
    void register(ServiceObject serviceObject) throws Exception;

    ServiceObject getServiceObject(String name) throws Exception;
}
