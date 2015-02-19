package eu.europeana.portal2.web.util.keyvalue;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Provides a Java Redis (Jedis) implementation for writing items to cache 
 * 
 * @author Bram Lohman
 */
public class RedisProvider {

  private Jedis jedis;

  Logger log = Logger.getLogger(this.getClass());

  public RedisProvider(String host, int port, String password) {
    log.info("Redis connection: " + host + ":" + port + "; secret: " + password);
    jedis = new Jedis(host, port);
    jedis.auth(password);
    JedisPool pool = new JedisPool("europeana");
    jedis.setDataSource(pool);
    jedis.connect();
  }


  /**
   * Getter
   * @return Jedis
   */
  public Jedis getJedis() {
    return this.jedis;
  }

}
