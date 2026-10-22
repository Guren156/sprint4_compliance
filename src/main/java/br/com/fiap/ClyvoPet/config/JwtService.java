package br.com.fiap.ClyvoPet.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    /*
     * Depois podemos mover isso para variável de ambiente.
     * A chave precisa ser suficientemente grande para HS256.
     */
    private static final String SECRET_KEY =
            "YmFzZTY0LWtleS1jbHl2b3BldC1hcGktMjAyNi1zZWd1cmFuY2Etand0";

    public String gerarToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        return criarToken(
                claims,
                userDetails
        );
    }

    private String criarToken(
            Map<String, Object> claims,
            UserDetails userDetails
    ) {

        long agora = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(agora))
                .expiration(
                        new Date(
                                agora + 1000L * 60 * 60 * 24
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairClaim(
                token,
                Claims::getSubject
        );
    }

    public <T> T extrairClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        Claims claims = extrairTodasClaims(token);

        return resolver.apply(claims);
    }

    private Claims extrairTodasClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean tokenValido(
            String token,
            UserDetails userDetails
    ) {

        String email = extrairEmail(token);

        return email.equals(userDetails.getUsername())
                && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {

        Date expiracao = extrairClaim(
                token,
                Claims::getExpiration
        );

        return expiracao.before(new Date());
    }

    private SecretKey getSigningKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}