package com.pdfai.pdfai.config;


import com.pdfai.pdfai.util.CustomHandshakeInterceptor;
import com.pdfai.pdfai.util.MyWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final MyWebSocketHandler myWebSocketHandler;
    @Autowired
    CustomHandshakeInterceptor customHandshakeInterceptor;
    @Autowired
    public WebSocketConfig(MyWebSocketHandler myWebSocketHandler) {
        this.myWebSocketHandler = myWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, "/ws/{uuid}/{jwt}")
                .addInterceptors(customHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
