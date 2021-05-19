package com.sofisticat.security.auth;

import com.google.common.collect.Lists;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.sofisticat.security.security.permission.ApplicationUserRole.*;

@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao {
    private final PasswordEncoder passwordEncoder;


    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers().stream()
                .filter(user -> user.getUsername().equals(username)).findFirst();
    }

    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> applicationUsers = Lists.newArrayList(
                new ApplicationUser(
                        "´tom",
                        passwordEncoder.encode("1234"),
                        true,
                        true,
                        true,
                        true,
                        STUDENT.getGrantedAuthorities()
                ),
                new ApplicationUser(
                        "linda",
                        passwordEncoder.encode("1234"),
                        true,
                        true,
                        true,
                        true,
                        ADMIN.getGrantedAuthorities()
                ),
                new ApplicationUser(
                        "user",
                        passwordEncoder.encode("1234"),
                        true,
                        true,
                        true,
                        true,
                        ADMINTRAINEE.getGrantedAuthorities()
                )
        );

        return applicationUsers;
    }
}
