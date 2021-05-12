package com.ihlet.springBootFbLoginDemo.service;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ihlet.springBootFbLoginDemo.common.AuthProvider;
import com.ihlet.springBootFbLoginDemo.dao.UserRepository;
import com.ihlet.springBootFbLoginDemo.dto.RoleDetailsDTO;
import com.ihlet.springBootFbLoginDemo.dto.UserDetailsDTO;
import com.ihlet.springBootFbLoginDemo.model.RegisterUser;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {


	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder encode;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("fetching user details");
		UserDetailsDTO userDetails = userRepository.findByUsername(username); 
		System.out.println(userDetails);
		if(userDetails == null) {
			System.out.println("Throwing...");
//			throw new RuntimeException("check it");
			throw new RuntimeException("Username:"+username +" not found!");
		} 
		
		if(userDetails != null && !AuthProvider.local.equals(userDetails.getProvider())) {
			System.out.println("Throwing user provider issue");
			throw new RuntimeException("Username:"+username +" has logged in from "+userDetails.getProvider()+". Please login with this provider");
		}
		return userDetails;
	}
	
	
	public void registerUser(RegisterUser registerUser, AuthProvider provider) {
		
		UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
		userDetailsDTO.setUsername(registerUser.getEmail());
		userDetailsDTO.setPassword(encode.encode(registerUser.getPassword()));
		userDetailsDTO.setFirstName(registerUser.getFirstName());
		userDetailsDTO.setLastName(registerUser.getLastName());
		RoleDetailsDTO roleDetails = new RoleDetailsDTO();
		roleDetails.setRoleId("ROLE_USER");
		HashSet<RoleDetailsDTO> roleSet = new HashSet<>();
		userDetailsDTO.setAuthorities(roleSet);
		//When user registers through application. Provider would be Local
		userDetailsDTO.setProvider(provider);
		
		userRepository.save(userDetailsDTO);
	}

}