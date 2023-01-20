package com.excentria_it.wamya.domain;

import com.excentria_it.wamya.common.annotation.Generated;

@Generated
public enum UserRole {
    ROLE_TRANSPORTER, ROLE_CLIENT;

    public String bareName() {
        return this.name().substring(this.name().indexOf("_") + 1);
    }
}
