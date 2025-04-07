package com.pdfai.pdfai.serviceimpl;

import com.pdfai.pdfai.service.LogoutService;
import com.pdfai.pdfai.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogoutServiceImpl implements LogoutService {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public void logoutUser(HttpServletRequest request){
        String uid=String.valueOf(jwtUtil.extractUserIdFromRequest(request));
        jwtUtil.removeToken(uid);
    }

}
