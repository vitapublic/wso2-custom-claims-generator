package edu.byu.wso2.is.jwt;

/*
 *    Copyright 2016 Brigham Young University
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

import edu.byu.wso2.is.helper.BYUEntity;
import edu.byu.wso2.is.helper.BYUEntityHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.dto.APIKeyValidationInfoDTO;
import org.wso2.carbon.apimgt.impl.token.JWTGenerator;
import org.wso2.carbon.apimgt.api.*;
import org.wso2.carbon.user.core.util.UserCoreUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;


public class CustomTokenGenerator extends JWTGenerator {

    private static final Log log = LogFactory.getLog(CustomTokenGenerator.class);

    static String BYU_DIALECT = "http://byu.edu/claims";
    private final BYUEntityHelper BYUEntityHelper = new BYUEntityHelper();

    public CustomTokenGenerator() {
    }

    //there is no access to the api call headers, etc. only what was passed in the DTO

	public Map<String, String> populateStandardClaims(APIKeyValidationInfoDTO keyValidationInfoDTO, String apiContext, String version)
            throws APIManagementException {

        //generating expiring timestamp
        long currentTime = Calendar.getInstance().getTimeInMillis() / 1000;
        long expireIn = currentTime + 60 * getTTL();

		// Get all of the normally populated claims
        Map<String, String> claims = super.populateStandardClaims(keyValidationInfoDTO,apiContext,version);

		// Now edit the issuer and change expiration to seconds
		claims.put("iss", "https://api.byu.edu");
		claims.put("exp", String.valueOf(expireIn));

		return claims;
    }

    public Map<String, String> populateCustomClaims(APIKeyValidationInfoDTO keyValidationInfoDTO, String apiContext, String version, String accessToken)
            throws APIManagementException {
        if (log.isDebugEnabled())
            log.debug("populateCustomClaims starting");
        Map<String, String> map = new HashMap<String, String>();//map for custom claims
        Map<String, String> claims = super.populateCustomClaims(keyValidationInfoDTO,apiContext,version,accessToken);

        boolean isApplicationToken =
                keyValidationInfoDTO.getUserType().equalsIgnoreCase(APIConstants.ACCESS_TOKEN_USER_TYPE_APPLICATION) ? true : false;
        if (isApplicationToken) {
            if (log.isDebugEnabled())
                log.debug("Application Token detected - no resource owner claims will be added");
        }
        else {
            String netid = extractNetId(keyValidationInfoDTO.getEndUserName());

            if (log.isDebugEnabled())
                log.debug("adding resource owner claims to map - netid " + netid);

            map = addResourceOwnerClaims(netid, map);
        }

        String consumerKey = keyValidationInfoDTO.getConsumerKey();
        String dialect = getDialectURI();
        String subscriberNetId = extractNetId(keyValidationInfoDTO.getSubscriber());

        if (log.isDebugEnabled())
            log.debug("adding client claims to map - subscriberNetId " + subscriberNetId + " client_id " + consumerKey);

        map.put(dialect + "/client_id",consumerKey);

        map = addClientClaims(consumerKey, subscriberNetId, map);

        if (log.isDebugEnabled())
            log.debug("populateCustomClaims ending");

        return map;
    }

    private Map<String, String> addClientClaims(String consumerKey, String subscriberNetId, Map<String, String> map) {

        if (log.isDebugEnabled())
            log.debug("addClientClaims starting");

        if (consumerKey == null) {
            return map;
        }
        boolean isConsumerClaims = true;
        BYUEntity identifiers = BYUEntityHelper.getBYUEntityFromConsumerKey(consumerKey);
        if (identifiers == null) {
            if (log.isDebugEnabled())
                log.debug("No claims found for consumerKey, using subscriberNetId");
            isConsumerClaims = false;
            identifiers = BYUEntityHelper.getBYUEntityFromNetId(subscriberNetId);
            if (identifiers == null)
                return map;
        }
        if (isConsumerClaims)
            map.put(BYU_DIALECT + "/client_claim_source", "CLIENT_ID");
        else
            map.put(BYU_DIALECT + "/client_claim_source", "CLIENT_SUBSCRIBER");

        map.put(BYU_DIALECT + "/client_subscriber_net_id", subscriberNetId);
        map.put(BYU_DIALECT + "/client_person_id", identifiers.getPersonId());
        map.put(BYU_DIALECT + "/client_byu_id", identifiers.getByuId());
        map.put(BYU_DIALECT + "/client_net_id", identifiers.getNetId());
        map.put(BYU_DIALECT + "/client_surname", identifiers.getSurname());
        map.put(BYU_DIALECT + "/client_rest_of_name", identifiers.getRestOfName());
        map.put(BYU_DIALECT + "/client_surname_position", identifiers.getSurnamePosition());
        map.put(BYU_DIALECT + "/client_name_prefix", identifiers.getPrefix());
        map.put(BYU_DIALECT + "/client_name_suffix", identifiers.getSuffix());
        map.put(BYU_DIALECT + "/client_sort_name", identifiers.getSortName());
        map.put(BYU_DIALECT + "/client_preferred_first_name", identifiers.getPreferredFirstName());


        if (log.isDebugEnabled())
            log.debug("addClientClaims ending");
        return map;
    }
    /* adds resource owner credentials to the map */
    private Map<String, String> addResourceOwnerClaims(String netid, Map<String, String> map) {

        if (log.isDebugEnabled())
            log.debug("addResourceOwnerClaims starting");

        if (netid == null) {
            return map;
        }
        BYUEntity identifiers = BYUEntityHelper.getBYUEntityFromNetId(netid);
        if (identifiers == null) {
            return map;
        }
        map.put(BYU_DIALECT + "/resourceowner_person_id", identifiers.getPersonId());
        map.put(BYU_DIALECT + "/resourceowner_byu_id", identifiers.getByuId());
        map.put(BYU_DIALECT + "/resourceowner_surname", identifiers.getSurname());
        map.put(BYU_DIALECT + "/resourceowner_rest_of_name", identifiers.getRestOfName());
        map.put(BYU_DIALECT + "/resourceowner_surname_position", identifiers.getSurnamePosition());
        map.put(BYU_DIALECT + "/resourceowner_prefix", identifiers.getPrefix());
        map.put(BYU_DIALECT + "/resourceowner_suffix", identifiers.getSuffix());
        map.put(BYU_DIALECT + "/resourceowner_sort_name", identifiers.getSortName());
        map.put(BYU_DIALECT + "/resourceowner_preferred_first_name", identifiers.getPreferredFirstName());
        map.put(BYU_DIALECT + "/resourceowner_net_id", netid);

        if (log.isDebugEnabled())
            log.debug("addResourceOwnerClaims ending");
        return map;
    }

    private String extractNetId(String carbonIdentifier) {
        if (log.isDebugEnabled()) {
            log.debug("extractNetId starting");
            log.debug("step 1: carbonIdentifier is " + carbonIdentifier);
        }
        String netid = UserCoreUtil.removeDomainFromName(carbonIdentifier);
        if (log.isDebugEnabled())
            log.debug("step 2: after remove domain netid is " + netid);
        if (netid != null) {
            if (netid.endsWith("@carbon.super")) {
                netid = netid.replace("@carbon.super", "");
            }
        }
        if (log.isDebugEnabled())
            log.debug("extractNetId ending with result " + netid);
        return netid;
    }
}
