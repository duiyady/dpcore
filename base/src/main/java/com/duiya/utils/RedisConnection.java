package com.duiya.utils;

import redis.clients.jedis.Jedis;

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

    public void set(String key, Object object){
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffSerializerUtil.serialize(object);
        jedis.set(bkey, bvalue);
    }

    public <T> T get(String key, Class<T> targetClass) {
        byte[] result = jedis.get(key.getBytes());
        if(result == null){
            return null;
        }
        return ProtoStuffSerializerUtil.deserialize(result, targetClass);
    }

    @Override
    protected void finalize() throws Throwable {
        jedis.close();
        super.finalize();
    }
}
