package com.project.User_Authentication.Config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTTokenUtil {
	private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	private String secret = "jwtSecretKey";

	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerate(claims, email);
	}

	private String doGenerate(Map<String , Object> claims , String subject) {	
		return Jwts.builder()
					.setClaims(claims)
					.setSubject(subject)
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
					.signWith(SignatureAlgorithm.HS512,secret)
					.compact();
	}
	public Boolean validateToken(String token , String email) {
		 	final String username = generateEmailFromToken(token);
		 	return (username.equals(email) && !isTokenExpired(token));
	}
	public String generateEmailFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	public <T> T getClaimFromToken(String token , Function<Claims , T>  claimsResolver) {
		 final Claims claims = getAllClaimsFromToken(token);
	        return claimsResolver.apply(claims);
	}
	 private Claims getAllClaimsFromToken(String token) {
	        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	    }

	    private Boolean isTokenExpired(String token) {
	        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
	        return expiration.before(new Date());
	    }
}
