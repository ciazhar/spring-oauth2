package com.ciazhar.client.authcode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * Created by ciazhar on 3/13/17.
 */

@Configuration
@EnableOAuth2Client
public class KonfigurasiOauth2Client {

    private String urlAuthorization = "http://localhost:10000/oauth/authorize";
    private String urlToken = "http://localhost:10000/oauth/token";

    @Bean
    public OAuth2RestOperations restOperations(OAuth2ClientContext context){
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource(),context);
        return restTemplate;
    }

    @Bean
    public OAuth2ProtectedResourceDetails resource(){
        AuthorizationCodeResourceDetails resourceDetails= new AuthorizationCodeResourceDetails();
        resourceDetails.setClientId("clientauthcode");
        resourceDetails.setClientSecret("123456");
        resourceDetails.setUserAuthorizationUri(urlAuthorization);
        resourceDetails.setAccessTokenUri(urlToken);
        return resourceDetails;
    }
}
