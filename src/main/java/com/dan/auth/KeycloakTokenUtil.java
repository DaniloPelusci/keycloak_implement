package com.dan.auth;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;


@Component
public class KeycloakTokenUtil {

    private final ConfigurableJWTProcessor<SecurityContext> jwtProcessor;

    @Value("${keycloak.issuer-uri}")
    private String issuerUri;

    public KeycloakTokenUtil(@Value("${keycloak.issuer-uri}") String issuerUri) throws Exception {
        this.issuerUri = issuerUri;

        // Setup do recurso para buscar o JWKS
        DefaultResourceRetriever resourceRetriever = new DefaultResourceRetriever(2000, 2000);
        URL jwkSetURL = new URL(issuerUri + "/protocol/openid-connect/certs");

        JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(jwkSetURL, resourceRetriever);

        jwtProcessor = new DefaultJWTProcessor<>();
        JWSKeySelector<SecurityContext> keySelector =
                new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);
    }

    public JWTClaimsSet validateAndGetClaims(String token) throws Exception {
        SecurityContext ctx = null;
        JWTClaimsSet claims = jwtProcessor.process(token, ctx);

        // valida o issuer (opcional, mas recomendado)
        if (!claims.getIssuer().equals(this.issuerUri)) {
            throw new SecurityException("Token com issuer inválido");
        }

        return claims;
    }

    public String getUserId(String token) throws Exception {
        return validateAndGetClaims(token).getSubject(); // sub
    }

    public String getUsername(String token) throws Exception {
        return (String) validateAndGetClaims(token).getClaim("preferred_username");
    }

    public List<String> getRoles(String token) throws Exception {
        Map<String, Object> realmAccess = (Map<String, Object>) validateAndGetClaims(token).getClaim("realm_access");
        return (List<String>) realmAccess.get("roles");
    }
}
