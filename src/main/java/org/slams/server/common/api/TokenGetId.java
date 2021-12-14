package org.slams.server.common.api;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slams.server.user.exception.InvalidTokenException;
import org.slams.server.user.oauth.jwt.Jwt;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
public class TokenGetId {
    private Jwt.Claims claims;


    public TokenGetId(HttpServletRequest request, Jwt jwt) {


        String authorization = request.getHeader("Authorization");
        String[] tokenString = authorization.split(" ");
        if (!tokenString[0].equals("Bearer")) {
            throw new InvalidTokenException("토큰 정보가 올바르지 않습니다.");
        }

        claims = jwt.verify(tokenString[1]);
    }


    public Long getUserId() {

        return claims.getUserId();

    }

}
