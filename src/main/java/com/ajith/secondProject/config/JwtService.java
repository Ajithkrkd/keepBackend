package com.ajith.secondProject.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    @Value ( "${application.security.jwt.secret-key}" )
    private String secretKey;
    @Value ( "${application.security.jwt.expiration}" )
    private long jwtExpiration ;

    @Value ( "${application.security.jwt.refresh-token.expiration}" )
    private long refreshExpiration ;

    public String extractUsername (String token) {
        return extractClaim (token, Claims::getSubject);
    }


    public <T>T extractClaim (String token, Function<Claims,T>claimsResolver) {
        final Claims claims = extractAllClaims ( token );
        return claimsResolver.apply ( claims );
    }

    public  String generateToken(UserDetails userDetails){
        return generateToken ( new HashMap <> () ,userDetails );
    }

    public  boolean isTokenValid(String token ,UserDetails userDetails){
        String username = extractUsername ( token );
        return (username.equals ( userDetails.getUsername() ) ) && ! isTokenExpired(token);
    }

    private boolean isTokenExpired (String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration (String token) {
        return extractClaim ( token ,Claims::getExpiration );
    }

    public String generateToken(
            Map <String ,Object> extraClaims,
            UserDetails userDetails) {
        return buildToken ( extraClaims,userDetails,jwtExpiration );

    }
    public String generateRefreshToken(
            UserDetails userDetails) {
            return buildToken (new HashMap <> (),userDetails,refreshExpiration );

    }

    private String buildToken( Map <String ,Object> extraClaims,
                               UserDetails userDetails,long expiration ) {


        Collection <? extends GrantedAuthority > authorities = userDetails.getAuthorities();
        List <String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put("roles", roles);
        return Jwts
                .builder ()
                .setClaims ( claims )
                .setSubject ( userDetails.getUsername () )
                .setIssuedAt ( new Date (System.currentTimeMillis ()) )
                .setExpiration ( new Date (System.currentTimeMillis ()+ expiration) )
                .signWith ( getSigningKey (), SignatureAlgorithm.HS256 )
                .compact ();
    }
    private Claims extractAllClaims (String token) {
        return Jwts
                .parserBuilder ()
                .setSigningKey ( getSigningKey() )
                .build ()
                .parseClaimsJws ( token )
                .getBody ();
    }

    private Key getSigningKey ( ) {
        byte[] keyBytes = Decoders.BASE64.decode (secretKey);
        return Keys.hmacShaKeyFor (keyBytes);
    }
}