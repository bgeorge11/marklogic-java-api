package com.marklogic.client.auth;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.NewCookie;

public class AuthenticationUtils {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationUtils.class);

    public static final String TOKEN_COOKIE = "SessionID";
    private static final String USER_PARAMETER = "rs:user";

    public static String getAuthenticationTokenForUser(String authenticationEndpointUrl, String user) {
        ApacheHttpClient4 client = ApacheHttpClient4.create(new DefaultApacheHttpClient4Config());
        final String url = authenticationEndpointUrl + "?" + USER_PARAMETER + "=" + user;
        WebResource resource = client.resource(url);
        logger.debug("Calling {} to retrieve Marklogic auth token", url);
        ClientResponse response = resource.get(ClientResponse.class);
        String sessionId = null;
        for (NewCookie cookie : response.getCookies()) {
            if (TOKEN_COOKIE.equals(cookie.getName())) {
                sessionId = cookie.getValue();
            }
        }
        logger.debug("Session id retrieved from Marklogic authentication endpoint: {}", sessionId);
        return sessionId;
    }
}
