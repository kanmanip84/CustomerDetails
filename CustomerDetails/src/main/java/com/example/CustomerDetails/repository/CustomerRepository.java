package com.example.CustomerDetails.repository;

import com.example.CustomerDetails.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByName(String name);
    List<Customer> findByNameStartingWith(String startingLetter);
}

