package vip.yeee.memo.demo.distribute.lock.jedis;

import redis.clients.jedis.Jedis;

/**
 * description ...
 *
 * @author https://www.yeee.vip
 * @since 2021/12/22 15:23
 */
public class JedisRepository extends Jedis {

    private static Jedis jedisRepository = null;

/*    public static Jedis getJedis() {
        if (jedisRepository != null) {
            return jedisRepository;
        }
        synchronized (JedisRepository.class) {
            if (jedisRepository != null) {
                return jedisRepository;
            }
            Jedis jedis = new Jedis("127.0.0.1",6379);
            jedis.auth("yeah");
            jedis.select(15);
            jedisRepository = jedis;
        }
        return jedisRepository;
    }*/

    public synchronized static Jedis getJedis() {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        jedis.auth("yeah");
        jedis.select(15);
        return jedis;
    }

}
