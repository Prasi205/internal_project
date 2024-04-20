package com.tm.internalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.internalproject.entity.UserDetails;

public interface UserRepository extends JpaRepository<UserDetails, Integer> {

	public UserDetails findByEmail(String email);
	
	public boolean existsByEmail(String email);
	
	public boolean existsByEmailAndPassword(String email,String password);
	
}
