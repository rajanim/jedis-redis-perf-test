package example;


import org.openjdk.jmh.annotations.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RedisOperations {

   @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Measurement(iterations = 12, time = 1)
    @Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
    @Warmup(iterations = 2, time = 1)
    @Benchmark
   public static void setHash() {
        RedisOperations redisOperations = new RedisOperations();
        String key = redisOperations.getUserKey();
       redisOperations.setHash(key,RedisOperations.getUser());
    }

    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Measurement(iterations = 12, time = 1)
    @Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
    @Warmup(iterations = 2, time = 1)
    @Benchmark
    public Map<String, String> getHash() {
        JedisPool jedisPool = RedisConnection.getPool();
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> user = jedis.hgetAll(getUserKey());
            return user;
        }
    }

    public String getUserKey(){
    return  "user:" + new Random().nextInt(100);
    }
    public void sayHello(){
        JedisPool jedisPool = RedisConnection.getPool();
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.ping();
        }
    }


public String setHash(String key, Map<String, String> userMap) {
       String ret;
        JedisPool jedisPool = RedisConnection.getPool();
        try (Jedis jedis = jedisPool.getResource()) {
           ret= jedis.hmset(key, userMap);
        }
        return ret;
    }


    public static HashMap<String, String> getUser(){
        HashMap<String, String> map = new HashMap<>();
        map.put("name", new Random().toString());
        Date date = new Date();
        map.put("joined", date.toString());
        return map;
    }
}
