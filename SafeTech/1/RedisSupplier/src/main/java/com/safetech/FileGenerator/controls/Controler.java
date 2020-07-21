package com.safetech.FileGenerator.controls;

import com.safetech.util.RedisObserver;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Kalinin Maksim <kalinin-maksim-javadev@mail.ru>
 */
@Controller
public class Controler {

    private static final int TIME_OUT = 20;
    private static final String REDIS_SERVER = "Localhost";
    private static final int REDIS_PORT = 6379;
    private static final String CHANNEL_DATA_IN = "CHANNEL_SIGN_RECIVER";
    private static final String CHANNEL_DATA_OUT = "CHANNEL_DATA_RECIVER";
    private static final String SIGNATURE = "signature";
    private static final String SIGNATURE_PAGE = "Signature";
    private static final String ATR_ERROR = "Error";
    private static final String PAGE_ERROR = "ErrorPage";

    //http://localhost:8080/test
    @RequestMapping(
            value = "/test",
            method = RequestMethod.GET
    )
    public String test(Model model) {
        return "testAnswer";
    }

    //http://localhost:8080/test
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET
    )
    public String getSignature(Model model) {
        String dataID = "data of " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        try (Jedis jedis = new Jedis(REDIS_SERVER, REDIS_PORT + (randomFail() ? 1 : 0))) {

            Data data = new Data(200 * 1024);
            jedis.lpush(dataID, data.toString());
            jedis.expire(dataID, 60);
            RedisObserver observer = new RedisObserver(j -> j.publish(CHANNEL_DATA_OUT, randomFail() ? "" : dataID));

            Optional<String> message = observer.waitMessage(jedis, CHANNEL_DATA_IN, randomFail  ? 5 : TIME_OUT);

            if (observer.getError().isPresent()) {
                throw new Exception(observer.getError().orElse("it happenes").toString());
            }

            String stSignKey = message.orElseThrow(() -> new Exception("The signature key is empty!"));

            String stSignature = Optional.ofNullable(jedis.hget(stSignKey, "signature")).orElseThrow(() -> new Exception("Generate failed: signature is empty"));
            String stPublicKey = jedis.hget(stSignKey, "publicKey");
            String stAlgorithm = jedis.hget(stSignKey, "algorithm");

            if (!data.verify(stAlgorithm, stPublicKey, stSignature)) {
                throw new Exception("Validation failed ");
            }
            model.addAttribute(SIGNATURE, stSignature);
            return SIGNATURE_PAGE;
        } catch (Exception ex) {
            model.addAttribute(ATR_ERROR, ex);
            return PAGE_ERROR;
        }
    }
    private static final boolean randomFail = false;

    static boolean randomFail() {
        return randomFail && Math.random() < 0.1;
    }
}

class Data {

    byte[] data;

    Data(int size) {
        data = new byte[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (-128 + 256 * Math.random());
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(data.length);
        for (int i = data.length - 1; i >= 0; i -= 2) {
            result.append((char) (data[i] * 128 + (i - 1 < 0 ? 0 : data[i - 1])));
        }
        return result.toString();
    }

    boolean verify(String stAlgorithm, String stPublicKey, String stSignature) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException {

        Signature ecdsaVerify = Signature.getInstance(stAlgorithm);
        KeyFactory kf = KeyFactory.getInstance("EC");

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(stPublicKey));

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(toString().getBytes("UTF-8"));
        boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(stSignature));

        return result;
    }
}
