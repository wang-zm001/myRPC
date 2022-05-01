package com.wzm.myrpc.server;

import com.wzm.myrpc.common.protocol.MessageProtocol;
import com.wzm.myrpc.common.protocol.MyRPCRequest;
import com.wzm.myrpc.common.protocol.MyRPCResponse;
import com.wzm.myrpc.common.protocol.MyRPCStatus;
import com.wzm.myrpc.server.register.ServiceObject;
import com.wzm.myrpc.server.register.ServiceRegister;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 请求处理者，提供解组请求，编组响应等操作
 * @author 东方雨倾
 * @since 1.0.0
 */
public class RequestHandler {
    private MessageProtocol protocol;

    private ServiceRegister serviceRegister;

    public RequestHandler(MessageProtocol protocol, ServiceRegister serviceRegister) {
        this.protocol = protocol;
        this.serviceRegister = serviceRegister;
    }

    public byte[] handleRequest(byte[] data) throws Exception {
        //1. 解组消息
        MyRPCRequest req  =  this.protocol.unmarshallingRequest(data);

        //2. 查找服务对象
        ServiceObject so = this.serviceRegister.getServiceObject(req.getServiceName());

        MyRPCResponse rsp = null;

        if(so == null) {
            rsp = new MyRPCResponse(MyRPCStatus.NOT_FOUND);
        } else {
            try{
                Method m = so.getClazz().getMethod(req.getMethod(),req.getParameterTypes());
                Object returnValue = m.invoke(so.getObj(),req.getParameters());
                rsp = new MyRPCResponse(MyRPCStatus.SUCCESS);
                rsp.setReturnValue(returnValue);
            }catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                rsp = new MyRPCResponse(MyRPCStatus.ERROR);
                rsp.setException(e);
            }
        }
        return this.protocol.marshallingResponse(rsp);
    }

    public MessageProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(MessageProtocol protocol) {
        this.protocol = protocol;
    }

    public ServiceRegister getServiceRegister() {
        return serviceRegister;
    }

    public void setServiceRegister(ServiceRegister serviceRegister) {
        this.serviceRegister = serviceRegister;
    }
}
