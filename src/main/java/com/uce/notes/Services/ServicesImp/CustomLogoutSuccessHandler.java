package com.uce.notes.Services.ServicesImp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uce.notes.Jwt.JwtUtils;
import com.uce.notes.Model.TokenModel;
import com.uce.notes.Repository.TokenRepository;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final JwtUtils jwtUtils;
    private final TokenRepository revokedTokenService;

    public CustomLogoutSuccessHandler(JwtUtils jwtUtils, TokenRepository revokedTokenService) {
        this.jwtUtils = jwtUtils;
        this.revokedTokenService = revokedTokenService;
    }


    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            TokenModel tokenModel = revokedTokenService.findByToken(token);
            if (tokenModel != null) {
                tokenModel.setEnabled(false);
                revokedTokenService.save(tokenModel);
            }
        }
        Map<String, Object> httReponse = new HashMap<>();
        httReponse.put("Message", "Logout successful");
        response.getWriter()
                .write(new ObjectMapper()
                        .writeValueAsString(httReponse));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();

    }
}