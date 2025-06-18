package com.picpay_simplified.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.picpay_simplified.domain.transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
}
