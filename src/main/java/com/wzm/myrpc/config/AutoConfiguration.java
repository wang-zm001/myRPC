package com.wzm.myrpc.config;

import com.wzm.myrpc.properties.MyRPCProperty;
import com.wzm.myrpc.client.ClientProxyFactory;
import com.wzm.myrpc.client.discovery.ZookeeperServiceDiscoveryImpl;
import com.wzm.myrpc.client.net.NettyClientImpl;
import com.wzm.myrpc.common.protocol.JavaSerializeMessageProtocol;
import com.wzm.myrpc.common.protocol.MessageProtocol;
import com.wzm.myrpc.server.NettyRpcServer;
import com.wzm.myrpc.server.RequestHandler;
import com.wzm.myrpc.server.RpcServer;
import com.wzm.myrpc.server.register.DefaultRpcProcessor;
import com.wzm.myrpc.server.register.ServiceRegister;
import com.wzm.myrpc.server.register.ZookeeperExportServiceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

public class AutoConfiguration {
    @Bean
    public DefaultRpcProcessor defaultRpcProcessor(){
        return new DefaultRpcProcessor();
    }

    @Bean
    public ClientProxyFactory clientProxyFactory(@Autowired MyRPCProperty myRPCProperty){
        ClientProxyFactory clientProxyFactory = new ClientProxyFactory();
        //设置服务发现者
        clientProxyFactory.setServiceDiscovery(new ZookeeperServiceDiscoveryImpl(myRPCProperty.getRegisterAddress()));
        //设置支持的协议
        Map<String, MessageProtocol> supportMessageProtocols = new HashMap<>();
        supportMessageProtocols.put(myRPCProperty.getProtocol(), new JavaSerializeMessageProtocol());
        clientProxyFactory.setSupportMessageProtocols(supportMessageProtocols);

        //设置网络层发现
        clientProxyFactory.setNettyClient(new NettyClientImpl());
        return clientProxyFactory;
    }

    @Bean
    public ServiceRegister serviceRegister(@Autowired MyRPCProperty myRPCProperty) {
        return new ZookeeperExportServiceRegister(
                myRPCProperty.getRegisterAddress(),
                myRPCProperty.getPort(),
                myRPCProperty.getProtocol()
        );
    }

    @Bean
    public RequestHandler requestHandler(@Autowired ServiceRegister serviceRegister) {
        return new RequestHandler(new JavaSerializeMessageProtocol(),serviceRegister);
    }

    @Bean
    public RpcServer rpcServer(@Autowired RequestHandler requestHandler,@Autowired MyRPCProperty myRPCProperty) {
        return new NettyRpcServer(myRPCProperty.getPort(),myRPCProperty.getProtocol(),requestHandler);
    }

    @Bean
    public MyRPCProperty myRPCProperty() {
        return new MyRPCProperty();
    }
}
