package com.wzm.myrpc.common.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaSerializeMessageProtocol implements MessageProtocol{
    private byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        return bout.toByteArray();
    }

    @Override
    public byte[] marshallingRequest(MyRPCRequest req) throws Exception {
        return this.serialize(req);
    }

    @Override
    public MyRPCRequest unmarshallingRequest(byte[] data) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        return (MyRPCRequest) in.readObject();
    }

    @Override
    public byte[] marshallingResponse(MyRPCResponse rsp) throws Exception {
        return this.serialize(rsp);
    }

    @Override
    public MyRPCResponse unmarshallingResponse(byte[] data) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        return (MyRPCResponse) in.readObject();
    }
}
