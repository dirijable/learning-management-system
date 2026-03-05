package com.dirijable.labs.lms.service.security.auth;

import com.dirijable.labs.lms.db.entity.RefreshToken;
import com.dirijable.labs.lms.db.entity.User;
import com.dirijable.labs.lms.db.entity.UserRole;
import com.dirijable.labs.lms.db.repository.UserRepository;
import com.dirijable.labs.lms.dto.jwt.TokenRequest;
import com.dirijable.labs.lms.dto.jwt.TokenResponse;
import com.dirijable.labs.lms.dto.security.UserDetailsImpl;
import com.dirijable.labs.lms.dto.user.LoginRequest;
import com.dirijable.labs.lms.exception.business.notfound.UserNotFoundException;
import com.dirijable.labs.lms.security.JwtUtils;
import com.dirijable.labs.lms.service.security.refresh.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public TokenResponse refreshAccessToken(TokenRequest tokenRequest) {
        String token = tokenRequest.refreshToken();
        RefreshToken refreshToken = refreshTokenService.verifyExpiration(token);
        User user = refreshToken.getUser();

        String accessToken = jwtUtils.generateAccessToken(user);

        refreshTokenService.deleteAllByUserId(user.getId());
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return new TokenResponse(accessToken, newRefreshToken.getToken());
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );
        UserDetails userDetails = (UserDetails)authenticate.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        User user = ((UserDetailsImpl)userDetails).getUser();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken.getToken());
    }

    @Override
    public void backDoor(String email) {
        userRepository.findByEmail(email)
                .map(user -> {
                    user.setRole(UserRole.ADMIN);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(email));
    }

}
