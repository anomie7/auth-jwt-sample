package com.withkid.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withkid.auth.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

	public Optional<User> findByEmail(String email);

}
