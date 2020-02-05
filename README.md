# helidon-mp-native
Prerequisites:

- This example requires Java 11
- If you want to run GraalVM native image, you need to install version 19.3.1 

## Build and run Java application
To build:
`mvn clean package`

To run:
`java -jar target/helidon-examples-native-image-mp.jar`

## Build and run native image
This example was tested with `GraalVM Version 19.3.1 CE`.

To build native image:
1. Export the location of graal-vm installation with native image to an env variable `GRAALVM_HOME`
    `export GRAALVM_HOME=/your/installation/of/graalvm`
2. `mvn clean package -Pnative-image`
    
Note: _There is a few warnings in the output, we are working on fixing them_

To run:
`./target/helidon-examples-native-image-mp`


## Endpoints

### Application endpoints
See the standard output of the application - you see the token content to be used for jwt, referenced as ${TOKEN} in examples

MicroProfile JWT-Auth protected endpoint:

`curl -i -H "Authorization: bearer ${TOKEN}" http://localhost:7001/jwt/scope`

`curl -i -H "Authorization: bearer ${TOKEN}" http://localhost:7001/jwt/role`

`curl -i http://localhost:7001/jwt/public`

OIDC (Open ID Connect) protected endpoint (Uses JWT to handle authentication, redirect is disabled):

`curl -i -H "Authorization: bearer ${TOKEN}" http://localhost:7001/oidc/scope`

`curl -i -H "Authorization: bearer ${TOKEN}" http://localhost:7001/oidc/role`

`curl -i http://localhost:7001/oidc/public`

Basic Authentication:

`curl -i -u jack:password http://localhost:7001/basic/role`

`curl -i http://localhost:7001/basic/public`

- Not authorized (not in correct role) - should return "403 Forbidden"

`curl -i -u john:password http://localhost:7001/basic/role`

Any protected endpoint (`*/role` and `*/scope`) invoked without authentication information should not be authenticated and
return "401 Unauthorized"

_A small issue with HTTP response codes:_
- "401 Unauthorized" means "Not Authenticated"
- "403 Forbidden" means "Not Authorized"

### MP endpoints

Health:
- Liveness health checks: `curl http://localhost:7001/health/live`
- Readiness health checks: `curl http://localhost:7001/health/ready`

Metrics:
- Base metrics: `curl -H "Accept: application/json" http://localhost:7001/metrics/base`
- Vendor metrics: `curl -H "Accept: application/json" http://localhost:7001/metrics/vendor`
- Application metrics: `curl -H "Accept: application/json" http://localhost:7001/metrics/application`


                 