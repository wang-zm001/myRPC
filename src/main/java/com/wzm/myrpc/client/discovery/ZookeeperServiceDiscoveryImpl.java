package com.wzm.myrpc.client.discovery;

import java.util.List;

import com.wzm.myrpc.common.constants.MyRPCConstants;
import com.wzm.myrpc.common.serializer.ZookeeperSerializer;
import org.I0Itec.zkclient.ZkClient;

/**
 * 以Zookeeper作为注册中心发现服务
 * @date 2022/4/24
 * @author ziming
 */
public class ZookeeperServiceDiscoveryImpl implements ServiceDiscovery{
    ZkClient zkClient;

    ZookeeperServiceDiscoveryImpl(String zkAddress) {
        zkClient = new ZkClient(zkAddress);
        zkClient.setZkSerializer(new ZookeeperSerializer());
    }

    @Override
    public List<String> getServices(String name) {
        String servicePath = MyRPCConstants.ZK_Service_path + MyRPCConstants.PATH_DELIMITER + name + "/service";
        List<String> children = zkClient.getChildren(servicePath);

        return null;
    }
}
