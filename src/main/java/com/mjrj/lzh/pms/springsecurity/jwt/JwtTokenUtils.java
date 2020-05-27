package com.mjrj.lzh.pms.springsecurity.jwt;

import com.mjrj.lzh.pms.dto.CurrentUserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: security
 * @BelongsPackage: com.example.security.jwt
 * @Author: lzh
 * @CreateTime: 2020-03-19 20:52
 * @Description: ${Description}
 */
@Component
@Slf4j
public class JwtTokenUtils {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_ID = "id";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_ROLES = "roles";

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration}")
    private int expiration; //过期时长，单位为秒,可以通过配置写入。

    //    生成token
    public String generateToken(CurrentUserDTO user) {
        Map <String, Object> claims = new HashMap <>();
        claims.put(CLAIM_KEY_USERNAME, user.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_ID, user.getToken());
        claims.put(CLAIM_KEY_ROLES, user.getAuthorities());
        return generateToken(claims);
    }

    public String generateToken(Map <String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }


    public String getUsernameFromToken(String token) {
        String username;
        try {
            log.info(getClaimsFromToken(token).get(CLAIM_KEY_USERNAME).toString());
            username = getClaimsFromToken(token).getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String getTokenKeyFromToken(String token) {
        String key;
        try {
            final Claims claims = getClaimsFromToken(token);
            key = claims.get(CLAIM_KEY_ID).toString();
        } catch (Exception e) {
            key = null;
        }
        return key;
    }


    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }


    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }


  //多少分钟
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration  * 60 * 1000L);
    }


    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }



    public Boolean canTokenBeRefreshed(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
            if((expirationDate.getTime() - System.currentTimeMillis()) < MINUTES_10){
                 return true;
            }
        return false;
    }


    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    private static final Long MINUTES_10 = 10 * 60 * 1000L;

    
    public Boolean validateToken(String token, CurrentUserDTO user) {
        try {
            final String key = getTokenKeyFromToken(token);
            return (
                    key.equals(user.getToken())
                            && !isTokenExpired(token));
        }catch(Exception e){
            return false;
        }
    }





}
