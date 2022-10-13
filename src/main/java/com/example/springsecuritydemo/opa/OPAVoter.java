package com.example.springsecuritydemo.opa;

import com.example.springsecuritydemo.service.impl.UserDetailsServiceImpl;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.*;

public class OPAVoter implements AccessDecisionVoter<Object> {

    private final String opaUrl;

    public OPAVoter(String opaUrl) {
        this.opaUrl = opaUrl;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object obj, Collection<ConfigAttribute> attrs) {

        if (!(obj instanceof FilterInvocation)) {
            return ACCESS_ABSTAIN;
        }

        FilterInvocation filter = (FilterInvocation) obj;
        Map<String, String> headers = new HashMap<>();

        for (Enumeration<String> headerNames = filter.getRequest().getHeaderNames(); headerNames.hasMoreElements();) {
            String header = headerNames.nextElement();
            headers.put(header, filter.getRequest().getHeader(header));
        }

        String[] path = filter.getRequest().getRequestURI().replaceAll("^/|/$", "").split("/");

        Map<String, Object> input = new HashMap<>();
        List<String> authorities = new ArrayList<>();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            authorities.add(auth.getAuthority());
        }

        input.put("authorities", authorities);
        input.put("username", userDetails.getUsername());
        input.put("method", filter.getRequest().getMethod());
        input.put("path", path);
        input.put("headers", headers);

        RestTemplate client = new RestTemplate();
        HttpEntity<?> request = new HttpEntity<>(new OPADataRequest(input));
        OPADataResponse response = client.postForObject(this.opaUrl, request, OPADataResponse.class);

        if (response == null || !response.getResult()) {
            return ACCESS_DENIED;
        }

        return ACCESS_GRANTED;
    }

}