package example;

import org.openjdk.jmh.annotations.*;
import redis.clients.jedis.Jedis;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JedisOnly {

    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
    @Warmup(iterations = 2, time = 1)
    @Benchmark
    public static void setHash() {
        Jedis jedis = new Jedis("localhost", 6379,1800);
        JedisOnly helper = new JedisOnly();
            String key = helper.getUserKey();
            jedis.hmset(key, helper.getUser());
    }


    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
    @Warmup(iterations = 2, time = 1)
    @Benchmark
    public Map<String, String> getHash() {
        Jedis jedis = new Jedis("localhost", 6379,1800);
            Map<String, String> user = jedis.hgetAll(getUserKey());
            return user;
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
