## BYU Custom Claims Generator

This project is for generating a BYU specific custom cliams for the JSON Web Token and OpenId Connect id_token.
This is a Key Manager (Either standalone or integrated with IS) project.

### Build Process:

#### Prerequisites

1. Maven 3 must be installed.
2. Java 8 must be installed and on the path.

#### Build
From the home directory of the project run `mvn clean package`. This will download the dependencies into your local repository,
build the jar file and place it in the `target`directory under the home directory of the project.

#### Installation
1. Copy the create jar to the `repository/components/lib` directory of the key manager or Identity Server you wish to install
the generator into.

2. Be sure JWT generation is enabled in the `<APIConsumerAuthentication>` section of the file.

3. Add the following line to the `repository/conf/api-manager.xml` file on the key manager or Identity Server inside the
`<APIConsumerAuthentication>` tag.
`<TokenGeneratorImpl>edu.byu.wso2.is.jwt.CustomTokenGenerator</TokenGeneratorImpl>`

4. Replace the following line in `repository/conf/identity.xml` file on the Identity Server inside the `<openidconnect>` tag:
`<IDTokenCustomClaimsCallBackHandler>org.wso2.carbon.identity.openidconnect.SAMLAssertionClaimsCallback</IDTokenCustomClaimsCallBackHandler>`
with:
`<IDTokenCustomClaimsCallBackHandler>edu.byu.wso2.is.openidconnect.BYUAssertionClaimsCallback</IDTokenCustomClaimsCallBackHandler>`

5. Be sure that the BYUPRODB database connection is configured and points to the CESPRD database. The user specified in the
the connection needs to have read-only access to the PRO.PERSON and IAM.CREDENTIAL tables.

6. Restart the server.


