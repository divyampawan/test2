package com.ecommerce.app.mapper;

import com.ecommerce.app.dto.UserDto;
import com.ecommerce.app.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
        
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        
        return userDto;
    }
    
    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        
        return user;
    }
    
    public List<UserDto> toDtoList(List<User> users) {
        if (users == null) {
            return null;
        }
        
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<User> toEntityList(List<UserDto> userDtos) {
        if (userDtos == null) {
            return null;
        }
        
        return userDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    public UserDto toProfileDto(User user) {
        return toDto(user);
    }
} 