package com.scm.scm20.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.scm20.Exception.userNameNotFoundException;

import com.scm.scm20.Exception.userNameNotFoundException;
import com.scm.scm20.Repository.UserRepository;
@Service
public class SecurityCustomUserDetailService  implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
	
	return  userRepository.findByEmail(username).orElseThrow(()->new userNameNotFoundException("invalid username or password"+ username));
	}
}
