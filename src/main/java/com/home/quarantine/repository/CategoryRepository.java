package com.home.quarantine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.home.quarantine.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
List<Category> findByIfDeleted(String ifDeleted);

	
	
}
