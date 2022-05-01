package com.wzm.myrpc.client.discovery;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import com.wzm.myrpc.common.constants.MyRPCConstants;
import com.wzm.myrpc.common.serializer.ZookeeperSerializer;
import com.wzm.myrpc.common.service.Service;
import org.I0Itec.zkclient.ZkClient;

/**
 * 以Zookeeper作为注册中心发现服务
 * @date 2022/4/24
 * @author ziming
 */
public class ZookeeperServiceDiscoveryImpl implements ServiceDiscovery{
    ZkClient zkClient;

    public ZookeeperServiceDiscoveryImpl(String zkAddress) {
        zkClient = new ZkClient(zkAddress);
        zkClient.setZkSerializer(new ZookeeperSerializer());
    }

    @Override
    public List<Service> getServices(String name) {
        String servicePath = MyRPCConstants.ZK_Service_path + MyRPCConstants.PATH_DELIMITER + name + "/service";
        List<String> children = zkClient.getChildren(servicePath);

        return Optional.ofNullable(children).orElse(new ArrayList<>()).stream().map(str -> {
            String deCh = null;
            try {
                deCh = URLDecoder.decode(str, MyRPCConstants.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return JSON.parseObject(deCh, Service.class);
        }).collect(Collectors.toList());
    }
}
