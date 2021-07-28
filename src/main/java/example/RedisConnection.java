package example;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class RedisConnection {

    private final static String REDIS_HOST = "localhost";
    private final static int REDIS_PORT = 6379;
    private final static JedisPoolConfig POOL_CONFIG = buildPoolConfig();
    private final static JedisPool JEDIS_POOL = new JedisPool(POOL_CONFIG, REDIS_HOST, REDIS_PORT);

    private static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);

        return poolConfig;
    }

    public static JedisPool getPool() {

        return JEDIS_POOL;
    }
}
