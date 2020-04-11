package com.home.quarantine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.home.quarantine.model.Sales;

public interface SalesRepository extends JpaRepository<Sales, Integer> {
	List<Sales> findByDate(String date);
}
