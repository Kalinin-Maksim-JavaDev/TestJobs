/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safetech.dataSignatureGenerator;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kalinin Maksim <kalinin-maksim-javadev@mail.ru>
 */
public class SignatureGenerator {

    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";

    public static Map<String,String> sign(String message) throws Exception{

        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        KeyPair keypair = g.generateKeyPair();
        PublicKey publicKey = keypair.getPublic();
        PrivateKey privateKey = keypair.getPrivate();

        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(message.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String sig = Base64.getEncoder().encodeToString(signature);
        System.out.println(sig);
        System.out.println(pub);

        HashMap<String,String> obj = new HashMap();
        obj.put("publicKey", pub);
        obj.put("signature", sig);
        //obj.put("message", message);
        obj.put("algorithm", ALGO);

        return obj;
    }
}
