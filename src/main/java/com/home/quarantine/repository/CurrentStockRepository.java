package com.home.quarantine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.home.quarantine.model.CurrentStock;

public interface CurrentStockRepository extends JpaRepository<CurrentStock, Integer> {

}
