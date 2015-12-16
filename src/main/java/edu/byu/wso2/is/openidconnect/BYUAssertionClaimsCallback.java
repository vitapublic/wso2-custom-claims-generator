package edu.byu.wso2.is.openidconnect;


import edu.byu.wso2.is.helper.BYUEntity;
import edu.byu.wso2.is.helper.BYUEntityHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oltu.openidconnect.as.messages.IDTokenBuilder;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.wso2.carbon.identity.oauth.common.OAuthConstants;
import org.wso2.carbon.identity.oauth2.token.OAuthTokenReqMessageContext;
import org.wso2.carbon.identity.openidconnect.CustomClaimsCallbackHandler;
import org.wso2.carbon.identity.openidconnect.SAMLAssertionClaimsCallback;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Returns the claims of the SAML assertion
 *
 */
public class BYUAssertionClaimsCallback implements CustomClaimsCallbackHandler {

    Log log = LogFactory.getLog(SAMLAssertionClaimsCallback.class);

    static String BYU_DIALECT = "http://byu.edu/claims";
    private final BYUEntityHelper BYUEntityHelper = new BYUEntityHelper();

    @Override
    public void handleCustomClaims(IDTokenBuilder builder, OAuthTokenReqMessageContext requestMsgCtx) {
        // reading the token set in the same grant
        Assertion assertion = (Assertion) requestMsgCtx.getProperty(OAuthConstants.OAUTH_SAML2_ASSERTION);
        if (assertion != null) {
            List<AttributeStatement> list = assertion.getAttributeStatements();
            if (list.size() > 0) {
                Iterator<Attribute> attribIterator =
                        assertion.getAttributeStatements().get(0)
                                .getAttributes().iterator();
                while (attribIterator.hasNext()) {
                    Attribute attribute = attribIterator.next();
                    String value = attribute.getAttributeValues().get(0).getDOM().getTextContent();
                    builder.setClaim(attribute.getName(), value);
                    if (log.isDebugEnabled()) {
                        log.debug("Attribute: " + attribute.getName() + ", Value: " + value);
                    }
                }
            } else {
                log.debug("No AttributeStatement found! ");
            }
        }
        else {
            // get ByuId, NetId, and PersonId from DB
            //^BYU/([a-z0-9]*)
            String user = requestMsgCtx.getAuthorizedUser();
            Pattern pattern = Pattern.compile("^BYU/([a-z0-9]*)");
            Matcher matcher = pattern.matcher(user);
            if (matcher.find()) {
                String netid = matcher.group(1);
                if (log.isDebugEnabled())
                    log.debug("finding attributes for netid " + netid);
                BYUEntity identifiers = BYUEntityHelper.getBYUEntityFromNetId(netid);
                if (identifiers != null) {
                    builder.setClaim("person_id", identifiers.getPersonId());
                    builder.setClaim("byu_id", identifiers.getByuId());
                    builder.setClaim("net_id", identifiers.getNetId());
                    builder.setClaim("surname", identifiers.getSurname());
                    builder.setClaim("rest_of_name", identifiers.getRestOfName());
                    builder.setClaim("surname_position", identifiers.getSurnamePosition());
                    builder.setClaim("prefix", identifiers.getPrefix());
                    builder.setClaim("suffix", identifiers.getSuffix());
                    builder.setClaim("sort_name", identifiers.getSortName());
                    builder.setClaim("preferred_first_name", identifiers.getPreferredFirstName());
                }
                //if no identifiers can be matched - do nothing
            } // if pattern doesn't match - do nothing
        }
    }

}
