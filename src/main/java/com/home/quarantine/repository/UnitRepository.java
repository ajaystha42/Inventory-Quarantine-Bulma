package com.home.quarantine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.home.quarantine.model.Unit;

public interface UnitRepository extends JpaRepository<Unit, Integer> {

	List<Unit> findByIfDeleted(String ifDeleted);

}
