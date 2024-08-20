package com.uce.notes.Repository;


import com.uce.notes.Model.TokenModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TokenRepository extends JpaRepository<TokenModel, Long> {
    TokenModel findByToken(String token);
}
