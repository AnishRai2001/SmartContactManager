package com.scm.scm20.UserServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.scm.scm20.Exception.ResourceNotFoundException;
import com.scm.scm20.Repository.UserRepository;
import com.scm.scm20.UserService.UserService;
import com.scm.scm20.entity.User;
import com.scm.scm20.helper.AppConstants;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		String userId=UUID.randomUUID().toString();
		user.setUserId(userId);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		user.setRoleList(List.of(AppConstants.ROLE_USER));
		return userRepository.save(user);

	}

	@Override
	public User updateUser(User updatedUser) {
		Optional<User> existingUser = userRepository.findById(updatedUser.getUserId());

		if (existingUser.isPresent()) {
			User userToUpdate = existingUser.get();

			userToUpdate.setName(updatedUser.getName());
			userToUpdate.setEmail(updatedUser.getEmail());
			userToUpdate.setPassword(updatedUser.getPassword()); // Consider hashing before saving
			userToUpdate.setAbout(updatedUser.getAbout());
			//userToUpdate.setProfilePic(updatedUser.getProfilePic());
			userToUpdate.setEnabled(updatedUser.isEnabled());
			userToUpdate.setEmailVerified(updatedUser.isEmailVerified());
			userToUpdate.setPhoneVerified(updatedUser.isPhoneVerified());

			return userRepository.save(userToUpdate);
		} else {
			throw new ResourceNotFoundException("User not found with ID: " + updatedUser.getUserId());
		}
	}

	@Override
	public User getUserById(String id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		List<User> list = userRepository.findAll();
		return list;
	}

	@Override
	public boolean isUserExist(String userId) {
	    boolean exists = userRepository.existsById(userId);
	    if (!exists) {
	        throw new ResourceNotFoundException("User not found with ID: " + userId);
	    }
	    return true;
	}

	@Override
	public boolean isUserExistByEmail(String email) {
	    boolean exists = userRepository.existsByEmail(email);
	    if (!exists) {
	        throw new ResourceNotFoundException("User not found with Email: " + email);
	    }
	    return true;
	}
	@Override
	public void deleteUser(String id) {
	    if (!userRepository.existsById(id)) {
	        throw new ResourceNotFoundException("User not found with ID: " + id);
	    }
	    userRepository.deleteById(id);
	}

	@Override
	 public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }
	@Override
	public boolean isPhoneNumberTaken(String phoneNumber) {
	    return userRepository.existsByPhoneNumber(phoneNumber);
	}

	public User getUserByEmail(String email) {
	    return userRepository.findByEmail(email)
	            .orElseThrow(() -> {
	                logger.error("User not found with email: {}", email);
	                return new ResourceNotFoundException("User not found with email: " + email);
	            });
	}

	@Override
	public boolean isNameTaken(String name) {
	    return userRepository.existsByName(name);
	}








}
