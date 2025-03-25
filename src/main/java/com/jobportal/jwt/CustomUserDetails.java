package com.jobportal.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jobportal.dto.AccountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails{
    private Long id;
    private String username;
    private String name;
    private String password;
    private Long profileId;
    private AccountType accountType;
    private Collection<?extends GrantedAuthority>authorities;
    @Override
    public boolean isAccountNonExpired() {
        return true; // Return true if the account is not expired
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true; // Return true if the account is not locked
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Return true if the credentials are not expired
    }
    
    @Override
    public boolean isEnabled() {
        return true; // Return true if the account is enabled
    }
   
}
