package com.sofisticat.security.auth;

import org.checkerframework.checker.nullness.Opt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ApplicationUserDao {

    Optional<ApplicationUser> selectApplicationUserByUsername(String username);


}


