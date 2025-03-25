package com.jobportal.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jobportal.dto.UserDTO;
import com.jobportal.exception.JobPortalException;
import com.jobportal.service.UserService;

@Service
public class MyUserDetailsSevice implements UserDetailsService{


    @Autowired
    private UserService userService;

    @Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    try {
        UserDTO dto = userService.getUserByEmail(email);
        if (dto == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        System.out.println("Loaded user: " + dto.getEmail());
        System.out.println("Encoded password: " + dto.getPassword());
        return new CustomUserDetails(dto.getId(), dto.getEmail(),dto.getName(), dto.getPassword(),dto.getProfileId(), dto.getAccountType(), new ArrayList<>());
    } catch (JobPortalException e) {
        throw new UsernameNotFoundException("Error loading user by email: " + email, e);
    }
}

}
