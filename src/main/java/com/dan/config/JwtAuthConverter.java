package com.dan.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Map;

public class JwtAuthConverter extends JwtAuthenticationConverter {

    public JwtAuthConverter() {
        setJwtGrantedAuthoritiesConverter(new CustomRoleConverter());
    }

    private static class CustomRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);
            // Adiciona roles do realm_access como ROLE_
            var realmAccess = (Map<String, Object>) jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                var roles = (Collection<String>) realmAccess.get("roles");
                roles.forEach(role -> authorities.add(() -> "ROLE_" + role));
            }
            return authorities;
        }
    }
}
