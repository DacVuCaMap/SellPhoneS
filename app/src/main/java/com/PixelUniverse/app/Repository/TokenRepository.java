package com.PixelUniverse.app.Repository;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Entity.Token;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {
    @Query("SELECT t FROM Token t inner join Account a on t.account.id=a.id where a.id=:id and " +
            "(t.expiration=false or t.revoked=false)")
    List<Token> findTokenByAccountId(Long id);

    Optional<Token> findByTokenString(String tokenString);
}
