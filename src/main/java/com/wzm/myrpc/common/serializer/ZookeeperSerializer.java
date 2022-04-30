package com.wzm.myrpc.common.serializer;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.nio.charset.StandardCharsets;

public class ZookeeperSerializer implements ZkSerializer {
    /**
     * 序列化
     * @param o 对象
     * @return 字节数组
     */
    @Override
    public byte[] serialize(Object o) throws ZkMarshallingError {
        return String.valueOf(o).getBytes(StandardCharsets.UTF_8);
    }

    /**
     *  反序列化
     * @param bytes 字节数组
     * @return 对象
     */
    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return new String(bytes,StandardCharsets.UTF_8);
    }
}
