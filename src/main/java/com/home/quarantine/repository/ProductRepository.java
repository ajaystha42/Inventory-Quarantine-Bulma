package com.home.quarantine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.home.quarantine.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
List<Product> findByIfDeleted(String ifDeleted);
List<Product> findByCategoryId(Integer id);

}
