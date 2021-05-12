package com.ihlet.springBootFbLoginDemo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ihlet.springBootFbLoginDemo.common.OAuth2UserFactory;
import com.ihlet.springBootFbLoginDemo.dao.UserRepository;
import com.ihlet.springBootFbLoginDemo.dto.UserDetailsDTO;
import com.ihlet.springBootFbLoginDemo.model.OAuth2UserInfo;
import com.ihlet.springBootFbLoginDemo.model.RegisterUser;


@Service
public class OAuth2ServiceImpl extends DefaultOAuth2UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired 
	UserDetailsServiceImpl userService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		// instead of calling to get basic info 
		//OAuth2User oauth2User = super.loadUser(userRequest);

		// call here to get complete information
		OAuth2UserInfo oauth2UserInfo = OAuth2UserFactory.getOAuth2User(userRequest);
		return 	processUser(oauth2UserInfo);
	}

	private UserDetailsDTO processUser(OAuth2UserInfo userInfo) {
		UserDetailsDTO  userDetailsDTO=  userRepo.findByEmail(userInfo.getEmail());
		if(userDetailsDTO != null) {
			// checking if user has logged in with different provider
			userDetailsDTO.setFirstName(userInfo.getFirstName());
			userDetailsDTO.setLastName(userInfo.getLastName());
			userRepo.save(userDetailsDTO);
		}  else {
			RegisterUser registerUser = new RegisterUser();
			registerUser.setEmail(userInfo.getEmail());
			registerUser.setFirstName(userInfo.getFirstName());
			registerUser.setLastName(userInfo.getLastName());
			userService.registerUser(registerUser, userInfo.getProvider());
			userDetailsDTO =  userRepo.findByEmail(userInfo.getEmail());
		}
		return userDetailsDTO;
	}
}