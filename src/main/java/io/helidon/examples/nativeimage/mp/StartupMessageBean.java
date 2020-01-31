package io.helidon.examples.nativeimage.mp;

import java.time.Instant;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;

import io.helidon.common.configurable.Resource;
import io.helidon.security.jwt.Jwt;
import io.helidon.security.jwt.SignedJwt;
import io.helidon.security.jwt.jwk.JwkKeys;
import io.helidon.security.jwt.jwk.JwkRSA;

@ApplicationScoped
public class StartupMessageBean {
    private void startServer(@Observes @Priority(500000) @Initialized(ApplicationScoped.class) Object event, BeanManager bm) {
        Jwt jwt = Jwt.builder()
                .subject("jack")
                .addUserGroup("admin")
                .addScope("admin_scope")
                .algorithm(JwkRSA.ALG_RS256)
                .issuer("native-image-mp1")
                .audience("http://localhost:8087/jwt")
                .issueTime(Instant.now())
                .userPrincipal("jack")
                .keyId("SIGNING_KEY")
                .build();

        JwkKeys jwkKeys = JwkKeys.builder()
                .resource(Resource.create("security/sign-jwk.json"))
                .build();

        SignedJwt signed = SignedJwt.sign(jwt, jwkKeys.forKeyId("sign-rsa").get());
        String tokenContent = signed.tokenContent();

        System.out.println("*********************************************************");
        System.out.println("**  JWT token to use for /jwt and /oidc requests: ");
        System.out.println("*********************************************************");
        System.out.println(tokenContent);
        System.out.println("*********************************************************");
        System.out.println("**  Example curl command:");
        System.out.println("*********************************************************");
        System.out.println("curl -H \"Authorization: bearer " + tokenContent + "\" http://localhost:7001/jwt/scope");
        System.out.println("*********************************************************");
    }
}
