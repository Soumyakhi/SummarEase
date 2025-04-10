package com.pdfai.pdfai.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
@Component
public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String uuid = request.getURI().getPath().split("/")[2];
        String jwt = request.getURI().getPath().split("/")[3];
        Long userId =Long.parseLong(jwtUtil.extractUserId(jwt));
        if (jwtUtil.validateToken(jwt,userId)) {
            attributes.put("userId", userId);
            attributes.put("uuId", uuid);
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
