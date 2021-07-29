package example;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class RedisConnection {

    private final static String REDIS_HOST = "xx.xx.xx.xxx";
    private final static int REDIS_PORT = 14092;
    private final static JedisPoolConfig POOL_CONFIG = buildPoolConfig();
    private final static JedisPool JEDIS_POOL = new JedisPool(POOL_CONFIG, REDIS_HOST, REDIS_PORT);

    public static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(8);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(false);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(false);
        return poolConfig;
    }

    public static JedisPool getPool() {

        return JEDIS_POOL;
    }
}
