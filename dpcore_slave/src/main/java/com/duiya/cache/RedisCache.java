package com.duiya.cache;

import com.duiya.utils.ProtoStuffSerializerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * redis缓存
 *
 * @author yingjun
 *
 */
@Component
public class RedisCache {
	public final static String CAHCENAME = "cache";// 缓存名
	public final static int CAHCETIME = 300;// 默认缓存时间

	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 放入缓存 默认过期 300 秒
	 * @param key
	 * @param obj
	 * @return
	 */
	public <T> boolean putCache(String key, T obj) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(bkey, bvalue);
			}
		});
		return result;
	}

	/**
	 * 放入缓存 自定义时间
	 * @param key
	 * @param obj
	 * @param expireTime
	 */
	public <T> void putCacheWithExpireTime(String key, T obj, final long expireTime) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);
		redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(bkey, expireTime, bvalue);
				return true;
			}
		});
	}

	/**
	 * 设置不过期缓存
	 * @param key
	 * @param obj
	 * @param <T>
	 */
	public <T> void putPerpetualCache(final String key, T obj){
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);
		if(redisTemplate == null){
			System.out.println("sdfas");
		}
		redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection.set(bkey, bvalue);
			}
		});
	}

	/**
	 * 放入List 缓存 默认过期300秒
	 * @param key
	 * @param objList
	 * @return
	 */
	public <T> boolean putListCache(String key, List<T> objList) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(objList);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(bkey, bvalue);
			}
		});
		return result;
	}

	/**
	 * 放入List 缓存 自定义时间
	 * @param key
	 * @param objList
	 * @param expireTime
	 * @return
	 */
	public <T> boolean putListCacheWithExpireTime(String key, List<T> objList, final long expireTime) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(objList);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(bkey, expireTime, bvalue);
				return true;
			}
		});
		return result;
	}

	/**
	 * 放入永不过期的缓存
	 * @param key
	 * @param objList
	 * @param <T>
	 * @return
	 */
	public <T> boolean putPerpetualListCache(String key, List<T> objList){
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(objList);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(bkey, bvalue);
				return true;
			}
		});
		return result;
	}

	/**
	 * 获取缓存
	 * @param key
	 * @param targetClass
	 * @return
	 */
	public <T> T getCache(final String key, Class<T> targetClass) {
		byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.get(key.getBytes());
			}
		});
		if (result == null) {
			return null;
		}
		return ProtoStuffSerializerUtil.deserialize(result, targetClass);
	}

	/**
	 * 获取List 缓存
	 * @param key
	 * @param targetClass
	 * @return
	 */
	public <T> List<T> getListCache(final String key, Class<T> targetClass) {
		byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.get(key.getBytes());
			}
		});
		if (result == null) {
			return null;
		}
		return ProtoStuffSerializerUtil.deserializeList(result, targetClass);
	}

	/**
	 * 精确删除key
	 * @param key
	 */
	public void deleteCache(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 模糊删除key
	 * @param pattern
	 */
	public void deleteCacheWithPattern(String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		redisTemplate.delete(keys);
	}

	/**
	 * 清空所有缓存 谨慎用
	 */
	public void clearCache() {
		deleteCacheWithPattern("*");
	}
}
