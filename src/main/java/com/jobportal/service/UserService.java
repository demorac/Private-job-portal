package com.jobportal.service;

import com.jobportal.dto.LoginDTO;
import com.jobportal.dto.RespnseDTO;
import com.jobportal.dto.UserDTO;
import com.jobportal.exception.JobPortalException;



public interface UserService {
    public UserDTO registerUser(UserDTO userDTO) throws JobPortalException;

    public UserDTO getUserByEmail(String email) throws JobPortalException;

    public UserDTO loginUser(LoginDTO loginDTO)throws JobPortalException;

    public Boolean sendOtp(String email)throws Exception;

    public Boolean verifyOtp(String email, String otp)throws JobPortalException;

    public RespnseDTO changePassword(LoginDTO loginDTO)throws JobPortalException;

    void sendInterviewEmail(String email, String jobTitle, String company, String interviewTime, String interviewLocation) throws Exception;



}
