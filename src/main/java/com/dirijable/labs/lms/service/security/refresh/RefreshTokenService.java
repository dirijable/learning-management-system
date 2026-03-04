package com.dirijable.labs.lms.service.security.refresh;

import com.dirijable.labs.lms.db.entity.RefreshToken;
import com.dirijable.labs.lms.db.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    void deleteAllByUserId(Long userId);

    RefreshToken verifyExpiration(String token);
}