package com.logistics.auth.service;

import com.logistics.auth.entity.RefreshToken;
import com.logistics.auth.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    void revokeRefreshToken(String token);

}