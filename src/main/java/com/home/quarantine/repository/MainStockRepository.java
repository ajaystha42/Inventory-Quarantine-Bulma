package com.home.quarantine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.home.quarantine.model.MainStock;

public interface MainStockRepository extends JpaRepository<MainStock, Integer> {

	List<MainStock> findByIfDeleted(String ifDeleted);
}
