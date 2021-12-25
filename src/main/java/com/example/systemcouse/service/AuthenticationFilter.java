package com.example.systemcouse.service;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.*;

@Provider
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter {

    private SecretKey key;
    private int T_LEN = 128;
    private byte[] IV;

    public void initFromStrings(String secretKey, String IV){
        key = new SecretKeySpec(decode(secretKey),"AES");
        this.IV = decode(IV);
    }

    public String encrypt(String message) throws Exception {
        byte[] messageInBytes = message.getBytes();
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN,IV);
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key,spec);
        byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        byte[] messageInBytes = decode(encryptedMessage);
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
        return new String(decryptedBytes);
    }

    private String encode(byte[] data) {
        return java.util.Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return java.util.Base64.getDecoder().decode(data);
    }

    public String aesEnc(String text) {
        String encryptedMessage = null;
        try {
            AuthenticationFilter aes = new AuthenticationFilter();
            aes.initFromStrings("CHuO1Fjd8YgJqTyapibFBQ==","e3IYYJC2hxe24/EO");
            encryptedMessage = aes.encrypt(text);
        } catch (Exception ignored) {
        }
        return encryptedMessage;
    }

    public String aesDec(String text) {
        String decryptedMessage = null;
        try {
            AuthenticationFilter aes = new AuthenticationFilter();
            aes.initFromStrings("CHuO1Fjd8YgJqTyapibFBQ==","e3IYYJC2hxe24/EO");
            decryptedMessage = aes.decrypt(text);
        } catch (Exception ignored) {
        }
        return decryptedMessage;
    }

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();
        if (!method.isAnnotationPresent(PermitAll.class)) {

            //Access denied for all
            if (method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("Access blocked for all users !!").build());
                return;
            }

            //Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();

            //Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

            //If no authorization information present; block access
            if (!Optional.ofNullable(authorization).isPresent() || authorization.isEmpty()) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("You cannot access this resource").build());
                return;
            }

            //Get encoded username and password
            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
            final String encodedUserPassword2 = aesEnc(encodedUserPassword);

            //Decode username and password
            String usernameAndPassword2 = aesDec(encodedUserPassword2);
            String usernameAndPassword = new String(Base64.decode(usernameAndPassword2));

            //Split username and password tokens
            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            final String username = tokenizer.nextToken();
            final String password = tokenizer.nextToken();

            //Verify user access
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                //Is user valid?
                if (!isUserAllowed(username, password, rolesSet)) {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                            .entity("You cannot access this resource").build());
                    return;
                }
            }
        }
    }

    private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet) {
        boolean isAllowed = false;

        //Step 1. Fetch password from database and match with password in argument
        //If both match then get the defined role for user from database and continue; else return isAllowed [false]
        //Access the database and do this part yourself
        //String userRole = userMgr.getUserRole(username);

        if (username.equals("ADMIN") && password.equals("ADMIN")) {
            String userRole = "ADMIN";

            //Step 2. Verify user role
            if (rolesSet.contains(userRole)) {
                isAllowed = true;
            }
        }
        else if (username.equals("TASK-ADMIN") && password.equals("TESTING")) {
            String userRole = "TASK-ADMIN";

            if (rolesSet.contains(userRole)) {
                isAllowed = true;
            }
        }
        else if (username.equals("INSTRUCTOR") && password.equals("TESTING")) {
            String userRole = "INSTRUCTOR";

            if (rolesSet.contains(userRole)) {
                isAllowed = true;
            }
        }
        else if (username.equals("USER") && password.equals("USER")) {
            String userRole = "USER";

            if (rolesSet.contains(userRole)) {
                isAllowed = true;
            }
        }
        return isAllowed;
    }
}
