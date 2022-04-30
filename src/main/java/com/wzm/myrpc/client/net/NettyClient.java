package com.wzm.myrpc.client.net;

import com.wzm.myrpc.common.service.Service;

public interface NettyClient {
    byte[] sendRequest(byte[] data, Service service) throws InterruptedException;
}
