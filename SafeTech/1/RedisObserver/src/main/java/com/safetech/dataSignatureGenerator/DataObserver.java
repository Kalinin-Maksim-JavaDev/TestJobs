/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safetech.dataSignatureGenerator;

import java.util.Map;
import java.util.Optional;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Kalinin Maksim <kalinin-maksim-javadev@mail.ru>
 */
public class DataObserver {

    private static final int TIME_OUT = 20;
    private static final String REDIS_SERVER = "Localhost";
    private static final int REDIS_PORT = 6379;
    private static final String CHANNEL_DATA = "CHANNEL_DATA_RECIVER";
    private static final String CHANNEL_RESULT = "CHANNEL_SIGN_RECIVER";
    private static final String CHANNEL_ERROR = "CHANNEL_ERROR";

    public static void main(String[] args) {
        try (Jedis jedis = new Jedis(REDIS_SERVER, REDIS_PORT)) {
            next:while (!("quit".equals(Optional.ofNullable(jedis.get("RedisOrder")).orElse("")))) {
                try {

                    RedisObserver observer = new RedisObserver();

                    Optional<String> message = observer.waitMessage(jedis, CHANNEL_DATA, TIME_OUT);

                    if (observer.isTimeOut()) {
                        continue next;
                    }

                    String dataID = message.orElseThrow(() -> new Exception("Data is empty"));

                    if (randomFail()) {
                        throw new Exception("Error:it happens");
                    }
                    String stdata = jedis.lpop(dataID);
                    
                    String signatureKey = dataID + ".signature";

                    Map<String,String> signatureDate = SignatureGenerator.sign(stdata);
                                        
                    jedis.hmset(signatureKey, signatureDate);
                    jedis.expire(signatureKey, 60);
                    jedis.publish(CHANNEL_RESULT, signatureKey);
                } catch (Exception ex) {

                    jedis.publish(CHANNEL_RESULT, ex.getMessage());
                }
            }
        }
    }

    private static final boolean randomFail = false;

    static boolean randomFail() {
        return randomFail && Math.random() < 0.2;
    }

}
