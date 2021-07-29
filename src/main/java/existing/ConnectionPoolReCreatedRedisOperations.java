package existing;


import org.openjdk.jmh.annotations.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Date;
import java.util.HashMap;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ConnectionPoolReCreatedRedisOperations {

    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
    @Warmup(iterations = 2, time = 1)
    @Benchmark
    public static void setHash() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,"localhost", 6379,1800);
        try (Jedis jedis = jedisPool.getResource()) {
            ConnectionPoolReCreatedRedisOperations redisOperations = new ConnectionPoolReCreatedRedisOperations();
            String key = redisOperations.getUserKey();
            jedis.hmset(key, redisOperations.getUser());
        }
    }

   @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
    @Warmup(iterations = 2, time = 1)
    @Benchmark
    public Map<String, String> getHash() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
       jedisPoolConfig.setMaxWaitMillis(100000);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,"localhost", 6379,1800);
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> user = jedis.hgetAll(getUserKey());
            return user;
        }
    }

/*    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Measurement(iterations = 100, time = 1)
    @Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
    @Warmup(iterations = 2, time = 1)
    @Benchmark*/
    public void updateHashWithLock() throws Exception{
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxWaitMillis(100000);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,"localhost", 6379,1800);
        try (Jedis jedis = jedisPool.getResource()) {
            String key = getUserKey();
            Map<String, String> user = jedis.hgetAll(key);
            user.put("status", "active");
            if(Integer.parseInt(jedis.get("LOCKED"))!=0){
                Thread.sleep(1000);
            }else {
                jedis.hset(key, user);
            }

        }
    }

    public String getUserKey(){
        return  "user:" + new Random().nextInt(100);
    }

    public static HashMap<String, String> getUser(){
        HashMap<String, String> map = new HashMap<>();
        map.put("name", new Random().toString());
        Date date = new Date();
        map.put("joined", date.toString());
        return map;
    }

}



