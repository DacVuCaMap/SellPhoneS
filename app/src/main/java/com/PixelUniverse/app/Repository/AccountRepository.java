package com.PixelUniverse.app.Repository;

import com.PixelUniverse.app.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByEmail(String email);

    List<Account> findAllByIsDeletedFalse();
    @Query("SELECT a from Account a WHERE YEAR(a.createAt) =:year AND a.isDeleted = false")
    List<Account> findAllByCreateAtYear(int year);

}
