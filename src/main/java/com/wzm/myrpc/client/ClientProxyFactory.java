package com.wzm.myrpc.client;

import com.wzm.myrpc.client.discovery.ServiceDiscovery;
import com.wzm.myrpc.client.net.NettyClient;
import com.wzm.myrpc.common.protocol.MessageProtocol;
import com.wzm.myrpc.common.protocol.MyRPCRequest;
import com.wzm.myrpc.common.protocol.MyRPCResponse;
import com.wzm.myrpc.common.service.Service;
import com.wzm.myrpc.exception.MyRPCException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * 客户端代理工厂：用于创建远程服务代理类
 * 封装编组请求、请求发送、编组响应等操作。
 *
 * @author 东方雨倾
 * @since 1.0.0
 */
public class ClientProxyFactory {
    private ServiceDiscovery serviceDiscovery;

    private Map<String, MessageProtocol> supportMessageProtocols;

    private NettyClient nettyClient;

    private Map<Class<?>,Object> objectCache = new HashMap<>();

    /**
     * 通过Java动态代理获取服务代理类
     *
     * @param clazz 被代理类Class
     * @param <T>   泛型
     * @return 服务代理类
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) this.objectCache.computeIfAbsent(clazz,
                cls -> newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, new ClientInvocationHandler(cls)));
    }

    public ServiceDiscovery getServiceDiscovery() {
        return serviceDiscovery;
    }

    public void setServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public Map<String, MessageProtocol> getSupportMessageProtocols() {
        return supportMessageProtocols;
    }

    public void setSupportMessageProtocols(Map<String, MessageProtocol> supportMessageProtocols) {
        this.supportMessageProtocols = supportMessageProtocols;
    }

    public NettyClient getNettyClient() {
        return nettyClient;
    }

    public void setNettyClient(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    private class ClientInvocationHandler implements InvocationHandler {
        private Class<?> clazz;

        private Random random = new Random();

        public ClientInvocationHandler(Class<?> clazz) {
            super();
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
            if(method.getName().equals("toString")) {
                return proxy.getClass().toString();
            }
            if(method.getName().equals("hashCode")) {
                return 0;
            }

            //1. 获取服务信息
            String serviceName = this.clazz.getName();
            List<Service> services = serviceDiscovery.getServices(serviceName);

            if(services == null || services.isEmpty()) {
                throw new MyRPCException("No provider available");
            }

            // 随机选择一个服务提供者（软负载均衡）
            Service service = services.get(random.nextInt(services.size()));

            //2. 构造request对象
            MyRPCRequest req = new MyRPCRequest();
            req.setServiceName(serviceName);
            req.setMethod(method.getName());
            req.setParameterTypes(method.getParameterTypes());
            req.setParameters(args);

            //3. 协议层编组
            // 获得该方法对应的协议
            MessageProtocol protocol = supportMessageProtocols.get(service.getProtocol());
            // 编组请求
            byte[] data = protocol.marshallingRequest(req);

            //4. 调用网络层发送请求
            byte[] reqData = nettyClient.sendRequest(data,service);

            //5. 解组响应消息
            MyRPCResponse rsp = protocol.unmarshallingResponse(reqData);

            //6. 结果处理
            if(rsp.getException() != null) {
                throw  rsp.getException();
            }
            return rsp.getReturnValue();
        }
    }
}
