package com.pdfai.pdfai.service;


import com.pdfai.pdfai.dto.Code;
import com.pdfai.pdfai.dto.LoginInfoDTO;

public interface LoginService {
    public LoginInfoDTO login(Code code);
}
