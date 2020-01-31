package io.helidon.examples.nativeimage.mp;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.helidon.security.abac.scope.ScopeValidator;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.annotation.Counted;

@Path("/")
public class JaxRsResource {
    @Inject
    @ConfigProperty(name = "app.message")
    private String message;

    @Inject
    @ConfigProperty(name = "app.number")
    private int number;

    @GET
    @Path("/public")
    @PermitAll
    @Counted
    public String unprotectedMethod() {
        return message + ", the answer to the question of life, universe and everything is: " + number;
    }

    @GET
    @Path("/scope")
    @ScopeValidator.Scope("admin_scope")
    public String message() {
        return message + " admin_scope";
    }

    @GET
    @Path("/role")
    @RolesAllowed("admin")
    public String jaxRsMessage() {
        return message + " admin role";
    }
}
