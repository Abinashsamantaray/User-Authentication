package com.project.User_Authentication.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.User_Authentication.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	Optional<User>	findByEmail(String email);
}
