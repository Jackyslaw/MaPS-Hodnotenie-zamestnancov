package com.hackag.fibimeter.security;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeService.findByEmailAddress(username);
        if (employee == null) {
            log.info("UsernameNotFoundException for user: " + username);
            throw new UsernameNotFoundException(username);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        employeeService.getAuthorities(employee).forEach(
            authorityName -> grantedAuthorities.add(new SimpleGrantedAuthority(authorityName.name()))
        );
        return new org.springframework.security.core.userdetails.User(
            employee.getEmailAddress(), employee.getPassword(), grantedAuthorities);
    }
}
