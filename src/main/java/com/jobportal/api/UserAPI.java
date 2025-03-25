package com.jobportal.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.dto.LoginDTO;
import com.jobportal.dto.RespnseDTO;
import com.jobportal.dto.UserDTO;
import com.jobportal.exception.JobPortalException;
import com.jobportal.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Validated
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserAPI {

    @Autowired
    private UserService userService;

   @PostMapping("/register")
   public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid UserDTO userDTO)throws JobPortalException  {
        userDTO = userService.registerUser(userDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
   
    }

@PostMapping("/login")
public ResponseEntity<UserDTO> loginUser(@RequestBody @Valid LoginDTO loginDTO) throws JobPortalException {
    return new ResponseEntity<>(userService.loginUser(loginDTO), HttpStatus.OK);
}

@PostMapping("/changePass")
public ResponseEntity<RespnseDTO> changePassword(@RequestBody @Valid LoginDTO loginDTO) {
    try {
        if (loginDTO.getEmail() == null || loginDTO.getEmail().isEmpty()) {
            throw new JobPortalException("EMAIL_REQUIRED");
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            throw new JobPortalException("PASSWORD_REQUIRED");
        }
        
        return new ResponseEntity<>(userService.changePassword(loginDTO), HttpStatus.OK);
    } catch (JobPortalException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespnseDTO(e.getMessage()));
    }
}


@PostMapping("/sendOtp/{email}")
public ResponseEntity<RespnseDTO> sendOtp(@PathVariable @Email (message="{user.email.invalid}") String email) throws Exception {
    userService.sendOtp(email);
    return new ResponseEntity<>(new RespnseDTO("OTP sent successfully..."), HttpStatus.OK);
}
@GetMapping("/verifyOtp/{email}/{otp}")
public ResponseEntity<RespnseDTO> sendOtp(@PathVariable @Email (message="{user.email.invalid}") String email, @PathVariable @Pattern (regexp="^[0-9]{6}$",message="{otp.invalid}") String otp) throws JobPortalException {
    userService.verifyOtp(email, otp);
    return new ResponseEntity<>(new RespnseDTO("OTP has been Verified..."), HttpStatus.OK);
}

@PostMapping("/sendInterviewEmail")
public ResponseEntity<String> sendInterviewEmail(@RequestBody Map<String, String> request) {
    try {
        String email = request.get("email");
        String jobTitle = request.get("jobTitle");
        String company = request.get("company");
        String interviewTime = request.get("interviewTime");
        String interviewLocation = request.get("interviewLocation");

        userService.sendInterviewEmail(email, jobTitle, company, interviewTime, interviewLocation);
        return ResponseEntity.ok("Email Sent Successfully!");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email: " + e.getMessage());
    }
}



}
