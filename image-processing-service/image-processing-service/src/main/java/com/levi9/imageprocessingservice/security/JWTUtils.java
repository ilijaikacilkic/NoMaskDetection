package com.levi9.imageprocessingservice.security;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JWTUtils {

	private final JWTKeyProvider keyProvider;
	
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public List<GrantedAuthority> extractAuthorities(String token) {
    	final Claims claims = extractAllClaims(token);
    	List<String> authoritiesStrings = (List<String>) claims.get("authorities");
    	
    	return authoritiesStrings.stream()
        		.map(authority -> new GrantedAuthority() {
					
					@Override
					public String getAuthority() {
						return authority;
					}
				})
        		.collect(Collectors.toList());
    }
    
    public String extractName(String token) {
    	final Claims claims = extractAllClaims(token);
    	return (String) claims.get("name");
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(keyProvider.getPublicKey()).parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token) {
        try {
        	Jwts.parser().setSigningKey(keyProvider.getPublicKey()).parseClaimsJws(token);
        	return true;
        } catch (JwtException e) {
        	e.printStackTrace();
        }
        return false;
    }
    
	public String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
    
}
