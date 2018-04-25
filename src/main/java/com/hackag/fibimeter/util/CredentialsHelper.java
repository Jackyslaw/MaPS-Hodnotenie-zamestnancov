package com.hackag.fibimeter.util;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Jakub Matus
 */
public class CredentialsHelper {

    public static String loggedUserMail() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

}
