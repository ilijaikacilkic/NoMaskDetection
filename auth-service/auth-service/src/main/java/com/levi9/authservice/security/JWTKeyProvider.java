package com.levi9.authservice.security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;

@Component
public class JWTKeyProvider {

	private String privateKeyString;
	
    private PrivateKey privateKey;
    
    public JWTKeyProvider() {}
    
    @Autowired
    public JWTKeyProvider(@Value("${jwt.rsa.privateKey}") String privateKeyString) {
    	this.privateKeyString = privateKeyString;
    	privateKey = readKey(
                privateKeyString,
                "PRIVATE",
                this::privateKeySpec,
                this::privateKeyGenerator
            );
    }
       
    public PrivateKey getPrivateKey() {
		return privateKey;
	}

    private <T extends Key> T readKey(String key, String headerSpec, Function<String, EncodedKeySpec> keySpec, BiFunction<KeyFactory, EncodedKeySpec, T> keyGenerator) {
        try {
        	String keyString = key;
            keyString = keyString
                .replace("-----BEGIN " + headerSpec + " KEY-----", "")
                .replace("-----END " + headerSpec + " KEY-----", "")
                .replaceAll("\\s+", "");

            return keyGenerator.apply(KeyFactory.getInstance("RSA"), keySpec.apply(keyString));
        } catch(NoSuchAlgorithmException e) {
            throw new JwtException(e.getMessage());
        }
    }

    private EncodedKeySpec privateKeySpec(String data) {
        return new PKCS8EncodedKeySpec(Base64.getDecoder().decode(data));
    }

    private PrivateKey privateKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
        try {
            return kf.generatePrivate(spec);
        } catch(InvalidKeySpecException e) {
            throw new JwtException(e.getMessage());
        }
    }
}

