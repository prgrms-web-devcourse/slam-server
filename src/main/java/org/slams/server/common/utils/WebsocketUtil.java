package org.slams.server.common.utils;

import org.slams.server.notification.Exception.TokenNotFountException;
import org.slams.server.user.exception.InvalidTokenException;
import org.slams.server.user.oauth.jwt.Jwt;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by yunyun on 2021/12/18.
 */

@Component
public class WebsocketUtil {

    private Jwt jwt;
    public WebsocketUtil(Jwt jwt){
        this.jwt = jwt;
    }

    public Long findTokenFromHeader(SimpMessageHeaderAccessor headerAccessor){
        var nativeHeaders =  (Map) headerAccessor.getMessageHeaders().get("nativeHeaders");
        assert nativeHeaders != null;
        if (nativeHeaders.containsKey("token")) {
            String token = nativeHeaders.get("token").toString().replace("[", "").replace("]","");
            if(token.length()>64){
                return jwt.verify(token).getUserId();
            }else{
                throw new InvalidTokenException("유효한 토큰 형식이 아닙니다.");
            }
        }else{
            throw new TokenNotFountException("헤더에 토큰이 존재하지 않습니다.");
        }


    }
}
