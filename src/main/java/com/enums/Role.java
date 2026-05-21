package com.enums;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN(Set.of(Permissions.READ, Permissions.WRITE, Permissions.DELETE, Permissions.UPDATE)),
    USER(Set.of(Permissions.READ,Permissions.WRITE));

    private final Set<Permissions> permissions;
}
