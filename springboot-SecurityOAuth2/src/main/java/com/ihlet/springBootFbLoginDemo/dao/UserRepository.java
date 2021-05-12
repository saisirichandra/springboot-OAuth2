package com.ihlet.springBootFbLoginDemo.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ihlet.springBootFbLoginDemo.dto.UserDetailsDTO;



@Repository
public interface UserRepository extends JpaRepository<UserDetailsDTO, String>{

	@Query("select u from UserDetailsDTO u where username=?1")
	public UserDetailsDTO findByUsername(String username);
	
	@Query("select u from UserDetailsDTO u where emailId=?1")
	public UserDetailsDTO findByEmail(String emailId);
	
}