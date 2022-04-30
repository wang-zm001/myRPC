package com.wzm.myrpc.server.register;

import com.alibaba.fastjson.JSON;
import com.wzm.myrpc.common.serializer.ZookeeperSerializer;
import com.wzm.myrpc.common.service.Service;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;

import static com.wzm.myrpc.common.constants.MyRPCConstants.*;

public class ZookeeperExportServiceRegister extends DefaultServiceRegister implements ServiceRegister{
    private ZkClient zkClient;

    public ZookeeperExportServiceRegister(String zkAddress, Integer port, String protocol) {
        this.zkClient = new ZkClient(zkAddress);
        this.zkClient.setZkSerializer(new ZookeeperSerializer());
        this.port = port;
        this.protocol = protocol;
    }

    /**
     * 服务注册
     * @param serviceObject 服务持有者
     * @throws Exception 注册异常
     */
    @Override
    public void register(ServiceObject serviceObject) throws Exception {
        super.register(serviceObject);
        Service service = new Service();
        String host = InetAddress.getLocalHost().getHostAddress();
        String address = host + ":" + port;
        service.setAddress(address);
        service.setName(serviceObject.getClazz().getName());
        service.setProtocol(protocol);
        this.exportService(service);
    }

    /**
     * 服务暴露
     * @param serviceResource 需要暴露的服务信息
     */
    private void exportService(Service serviceResource) {
        String serviceName = serviceResource.getName();
        String url = JSON.toJSONString(serviceResource);
        try{
            url = URLEncoder.encode(url,UTF_8);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        String servicePath = ZK_Service_path + PATH_DELIMITER + serviceName + "/service";
        if(!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath,true);
        }
        String urlPath = servicePath + PATH_DELIMITER + url;
        if(zkClient.exists(urlPath)) {
            zkClient.delete(urlPath);
        }
        zkClient.createEphemeral(urlPath);
    }

    @Override
    public ServiceObject getServiceObject(String name) throws Exception {
        return super.getServiceObject(name);
    }
}
