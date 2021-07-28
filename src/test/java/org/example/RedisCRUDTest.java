package org.example;

import example.RedisConnection;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

public class RedisCRUDTest {


    @Test
    public void setHash(){
        JedisPool jedisPool = RedisConnection.getPool();
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset("user:test", "name", "test");

        }
    }

    @Test
    public void getHash() {
        JedisPool jedisPool = RedisConnection.getPool();
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> user = jedis.hgetAll("user:2");
            System.out.println(user.toString());
        }
    }


}
