package com.uce.notes.Config.Filters;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uce.notes.Jwt.JwtUtils;
import com.uce.notes.Model.TokenModel;
import com.uce.notes.Model.User;
import com.uce.notes.Repository.TokenRepository;
import com.uce.notes.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtUtils jwtUtils;

    private UserRepository userRepository;

    private TokenRepository tokenRepository;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserRepository userRepository, TokenRepository tokenRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        User user = null;
        String password = "";
        String email = "";
        try {
            user = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
            email = user.getEmail();
            password = user.getPassword();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String token = jwtUtils.generateAccessToken(user.getUsername());

        User dbUser = userRepository.findUserByEmail(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if token already exists in the database
        TokenModel existingToken = tokenRepository.findByToken(token);
        if (existingToken == null) {
            // Create a new token only if it does not exist
            TokenModel tokenModel = new TokenModel();
            tokenModel.setToken(token);
            tokenModel.setEnabled(true);

            // Save token
            tokenRepository.save(tokenModel);

            // Add token to user
            Set<TokenModel> tokenSet = new HashSet<>(dbUser.getTokens());
            tokenSet.add(tokenModel);
            dbUser.setTokens(tokenSet);

            // Save user with updated tokens
            userRepository.save(dbUser);
        } else {
            // Token already exists; ensure it's enabled
            existingToken.setEnabled(true);
            tokenRepository.save(existingToken);

            // Ensure token is linked to the user
            Set<TokenModel> tokenSet = new HashSet<>(dbUser.getTokens());
            tokenSet.add(existingToken);
            dbUser.setTokens(tokenSet);
            userRepository.save(dbUser);
        }

        response.addHeader("Authorization", token);
        Map<String, Object> httReponse = new HashMap<>();
        httReponse.put("token", token);
        httReponse.put("Message", "Auhentication success");
        httReponse.put("Email", user.getUsername());

        response.getWriter()
                .write(new ObjectMapper()
                        .writeValueAsString(httReponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Invalid email or password");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        response.getWriter().flush();
    }
}
