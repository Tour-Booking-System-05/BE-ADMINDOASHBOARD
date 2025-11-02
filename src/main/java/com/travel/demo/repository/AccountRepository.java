package com.travel.demo.repository;

import com.travel.demo.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Accounts, Integer> {
    public Accounts findByEmail(String email);
}
