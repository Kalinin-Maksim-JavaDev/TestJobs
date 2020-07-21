/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safetech.dataSignatureGenerator;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 *
 * @author Kalinin Maksim <kalinin-maksim-javadev@mail.ru>
 */
public class RedisObserver {

    private static final String PRIVATE_CHANNEL = String.valueOf(Math.random()) + "CHANNEL";
    private static final String MSSG_TIME_OUT = "Error:Time out";
    String stMessage;
    String stError;

    EventHandle onReady;

    public RedisObserver() {
        this(null);
    }

    public RedisObserver(EventHandle<Jedis> onReady) {
        this.onReady = onReady;
    }

    public Optional<String> waitMessage(Jedis jedis, String channel, int timeOut) {
        JedisPubSub pubSup = new JedisPubSub() {

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                if (onReady != null) {
                    Jedis jedis = new Jedis();
                    onReady.onEvent(jedis);
                    jedis.close();
                }
            }

            @Override
            public void onMessage(String channel, String message) {

                message = "".equals(message) ? null : message;
                switch (message) {
                    default:
                        if (!message.contains("Error")){
                            RedisObserver.this.stMessage = message;
                            break;
                        }                        
                    case MSSG_TIME_OUT:
                        stError = message;
                        break;
                }
                unsubscribe();
            }
        };

        new Thread() {

            @Override
            public void run() {

                try {
                    Thread.sleep(timeOut * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RedisObserver.class.getName()).log(Level.SEVERE, null, ex);
                }
                Jedis jedis = new Jedis();
                jedis.publish(channel, MSSG_TIME_OUT);
                jedis.close();
            }

        }.start();
        jedis.subscribe(pubSup, channel);

        return Optional.ofNullable(stMessage);
    }

    public Optional getError() {
        return Optional.ofNullable(stError);
    }

    public boolean isTimeOut() {
        return MSSG_TIME_OUT.equals(getError().orElse(""));
    }

    public interface EventHandle<T> {

        void onEvent(T owner);
    }
}
