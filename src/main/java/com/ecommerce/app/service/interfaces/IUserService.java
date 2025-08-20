package com.ecommerce.app.service.interfaces;

import com.ecommerce.app.entity.User;
import java.util.Optional;

public interface IUserService {
    User registerUser(User user);
    User saveUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    boolean authenticateUser(String username, String password);
    void updatePassword(Long userId, String newPassword);
}
