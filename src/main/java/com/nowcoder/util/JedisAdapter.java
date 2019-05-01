package com.nowcoder.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizeyang on 2019/5/1.
 */
@Component
public class JedisAdapter implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool = null;
    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost",6379);

    }
    private Jedis getJedis(){
        return pool.getResource();
    }

    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
           logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    //数值显示
    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }






/*
    public static void print(int index,Object obj){
        System.out.println(String.format("%d,%s",index,obj.toString()));
    }

    public static void main(String[] argv){
        Jedis jedis = new Jedis();
        jedis.flushAll();


//        Map<String,String> map = new HashMap<>();

        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello","newhello");
        print(1,jedis.get("newhello"));
        //设置key对应的value过期时间
        jedis.setex("hello2",15,"world");

        jedis.set("pv","100");
        jedis.incr("pv");
        print(2,jedis.get("pv"));
        jedis.incrBy("pv",5);
        print(2,jedis.get("pv"));

        //列表操作
        String listName = "listA";
        for(int i=0;i<10;++i){
            jedis.lpush(listName,"a"+String.valueOf(i));
        }
        print(3,jedis.lrange(listName,0,12));
        print(4,jedis.llen(listName));
        print(5,jedis.lpop(listName));
        print(6,jedis.lindex(listName,3));

        String userKey = "user12";
        jedis.hset(userKey,"name","Jim");
        jedis.hset(userKey,"age","12");
        jedis.hset(userKey,"phone","18888888888");

        print(7,jedis.hget(userKey,"name"));
        print(8,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(9,jedis.hgetAll(userKey));

        print(10,jedis.hkeys(userKey));
        print(11,jedis.hvals(userKey));
        print(12,jedis.hexists(userKey,"email"));

        jedis.hsetnx(userKey,"name","lizeyang");
        print(13,jedis.hgetAll(userKey));

        //set集合
        String likeKeys1 = "newsLike1";
        String likeKeys2 = "newsLike2";
        for(int i=0;i<10;++i){
            jedis.sadd(likeKeys1,String.valueOf(i));
            jedis.sadd(likeKeys2,String.valueOf(i*2));
        }
        print(14,jedis.smembers(likeKeys1));
        print(15,jedis.smembers(likeKeys2));
        //求交集
        print(16,jedis.sinter(likeKeys1,likeKeys2));
        //求并集
        print(17,jedis.sunion(likeKeys1,likeKeys2));
        //求差集
        print(18,jedis.sdiff(likeKeys1,likeKeys2));
        //删除列表中的数字5
        jedis.srem(likeKeys1,"5");
        print(19,jedis.smembers(likeKeys1));

        //获取set里面一共有多少个数字
        print(20,jedis.scard(likeKeys1));
        //转移集合中的元素
        jedis.smove(likeKeys2,likeKeys1,"14");
        print(21,jedis.smembers(likeKeys1));

        //优先队列
        String rankKey = "rankKey";
        jedis.zadd(rankKey,15,"Jim");
        jedis.zadd(rankKey,60,"Ben");
        jedis.zadd(rankKey,90,"Broter");
        jedis.zadd(rankKey,80,"Alex");
        jedis.zadd(rankKey,75,"lizeyang");
        print(22,jedis.zcard(rankKey));
        print(23,jedis.zcount(rankKey,61,100));
        print(24,jedis.zscore(rankKey,"lizeyang"));
        //给lizeyang加5分
        jedis.zincrby(rankKey,5,"lizeyang");
        print(25,jedis.zscore(rankKey,"lizeyang"));
        for(Tuple tuple : jedis.zrangeByScoreWithScores(rankKey,"0","100")){
            print(26,tuple.getElement()+":"+String.valueOf(tuple.getScore()));
        }

        //线程
        JedisPool pool = new JedisPool();
        for(int i =0;i<100;++i){
            Jedis j = pool.getResource();
            j.get("a");
            System.out.println("POOL"+i);
            j.close();
        }
    }
    */
}
