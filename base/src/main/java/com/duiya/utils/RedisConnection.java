package com.duiya.utils;

import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisConnection {
    private Jedis jedis;
    public RedisConnection(String ip, int port, String password) {
        jedis = new Jedis(ip, port);
        //权限认证
        jedis.auth(password);
    }
    public RedisConnection(String ip, String password) {
        jedis = new Jedis(ip, 6379);
        //权限认证
        jedis.auth(password);
    }
    public RedisConnection(String ip) {
        jedis = new Jedis(ip, 6379);
    }

    public void set(String key, Object object) throws Exception {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffSerializerUtil.serialize(object);
        jedis.set(bkey, bvalue);
    }

    public void setList(String key, List object) throws Exception {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(object);
        jedis.set(bkey, bvalue);
    }

    public <T> T get(String key, Class<T> targetClass) throws Exception {
        byte[] result = jedis.get(key.getBytes());
        if(result == null){
            return null;
        }
        return ProtoStuffSerializerUtil.deserialize(result, targetClass);
    }

    public <T> List<T> getList(String key, Class<T> targetClass) throws Exception {
        byte[] result = jedis.get(key.getBytes());
        if(result == null){
            return null;
        }
        return ProtoStuffSerializerUtil.deserializeList(result, targetClass);
    }

    @Override
    protected void finalize() throws Throwable {
        jedis.close();
        super.finalize();
    }
}
