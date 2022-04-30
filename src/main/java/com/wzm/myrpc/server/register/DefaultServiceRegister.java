package com.wzm.myrpc.server.register;

import java.util.HashMap;
import java.util.Map;

public class DefaultServiceRegister implements ServiceRegister{
    private Map<String,ServiceObject> serviceMap = new HashMap<>();
    protected String protocol;
    protected Integer port;

    @Override
    public void register(ServiceObject serviceObject) throws Exception {
        if(serviceObject == null){
            throw new IllegalAccessException("parameter cannot be empty");
        }
        this.serviceMap.put(serviceObject.getName(),serviceObject);
    }

    @Override
    public ServiceObject getServiceObject(String name) throws Exception {
        return this.serviceMap.get(name);
    }
}
