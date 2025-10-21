package com.nitish.insta.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.nitish.insta.Configuration.AppConstant;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.io.Decoders;
@Component
public class JwtTokenHelper {
    public String getUserNameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }
    public  String generateToken(String username){
        Map<String,Object> claims=new HashMap<>();
        return doGenerateToken(claims,username);
    }
    public Boolean validateToken(String token,String username){
        final String tokenUsername=getUserNameFromToken(token);
        return (username.equals(username)&& !isTokenExpired(token));
    }
    private SecretKey getSigningKey(){
        byte[] keyBytes= Decoders.BASE64.decode(AppConstant.JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private String doGenerateToken(Map<String,Object> claims,String userName){
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+AppConstant.JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private <T>T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims=getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Claims getAllClaimsFromToken(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private boolean isTokenExpired(String token){
        final Date expiration=getExpirationnDateFromToken(token);
        return expiration.before(new Date());
    }
    private Date getExpirationnDateFromToken(String token){
        return getClaimFromToken(token,Claims::getExpiration);
    }
}
