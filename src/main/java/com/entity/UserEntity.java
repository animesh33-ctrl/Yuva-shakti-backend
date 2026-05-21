package com.entity;

import com.enums.Role;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity  implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID  id;

    @Column(name = "user_name",nullable = false,unique = true)
    private String fullname;
    @Column(name = "user_email",nullable = false,unique = true)
    private String email;

    @Column(name = "user_password",nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "failed_attempts")
    private int failedAttempts = 0;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Override
    public @NullMarked Collection<? extends GrantedAuthority> getAuthorities() {
        if (role==null){
            return List.of();
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
        grantedAuthorities.addAll(role.getPermissions().stream()
                .map(permissions -> new SimpleGrantedAuthority(permissions.name()))
                .collect(Collectors.toSet()));
        return grantedAuthorities;
    }

    @Override
    public @NullMarked String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
