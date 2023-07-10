package com.ecommerceboari.api.repository;

import com.ecommerceboari.api.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {
}
