package com.scm.scm20.UserService;

import java.util.List;
import com.scm.scm20.entity.User;

public interface UserService {
    User saveUser(User user);
    
    User updateUser(User user); // Renamed to follow Java conventions
    
    User getUserById(String id);
    
    List<User> getAllUser();

    void deleteUser(String id);
    
    boolean isUserExist(String userId);
    
    boolean isUserExistByEmail(String email);

	boolean isEmailTaken(String email);

	boolean isPhoneNumberTaken(String phoneNumber);
	
	User getUserByEmail(String email);
}
