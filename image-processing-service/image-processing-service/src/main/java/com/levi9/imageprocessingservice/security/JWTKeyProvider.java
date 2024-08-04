package com.levi9.imageprocessingservice.security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;

@Component
public class JWTKeyProvider {

	@Value("${jwt.rsa.publicKey}")
	private String publicKeyString;
	
    private PublicKey publicKey;

	public PublicKey getPublicKey() {
		return publicKey;
	}

	@PostConstruct
    public void init() {
        publicKey = readKey(
        		publicKeyString,
                "PUBLIC",
                this::publicKeySpec,
                this::publicKeyGenerator
            );
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

    private EncodedKeySpec publicKeySpec(String data) {
        return new X509EncodedKeySpec(Base64.getDecoder().decode(data));
    }
 
    private PublicKey publicKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
        try {
            return kf.generatePublic(spec);
        } catch(InvalidKeySpecException e) {
            throw new JwtException(e.getMessage());
        }
    }
}

