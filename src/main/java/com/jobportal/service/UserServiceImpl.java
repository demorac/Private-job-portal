package com.jobportal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobportal.dto.LoginDTO;
import com.jobportal.dto.NotificationDTO;
import com.jobportal.dto.RespnseDTO;
import com.jobportal.dto.UserDTO;
import com.jobportal.entity.OTP;
import com.jobportal.entity.User;
import com.jobportal.exception.JobPortalException;
import com.jobportal.repository.OTPRepository;
import com.jobportal.repository.UsersRepository;
import com.jobportal.utility.Data;
import com.jobportal.utility.Utilities;

import jakarta.mail.internet.MimeMessage;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private Utilities utilities; // Injected as a bean

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificationService notificationService;

    @Override
    public UserDTO registerUser(UserDTO userDTO) throws JobPortalException {
        Optional<User> optional=usersRepository.findByEmail(userDTO.getEmail());
        if(optional.isPresent())throw new JobPortalException("USER_FOUND");
        userDTO.setProfileId(profileService.createProfile(userDTO.getEmail()));
        userDTO.setId(utilities.getNextSequence("users")); // Use injected utilities
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userDTO.toEntity();
        usersRepository.save(user);
        return user.toDTO();
    }

    @Override
    public UserDTO loginUser(LoginDTO loginDTO) throws JobPortalException{
        User user =usersRepository.findByEmail(loginDTO.getEmail()).orElseThrow(()-> new JobPortalException("USER_NOT_FOUND"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) throw new JobPortalException("INVALID_CREDENTIALS");
        return user.toDTO();
        
    }

    @Override
    public Boolean sendOtp(String email) throws Exception {
         MimeMessage mm=mailSender.createMimeMessage();
         MimeMessageHelper message=new MimeMessageHelper(mm, true);
         message.setTo(email);
         message.setSubject("Your OTP Code is...");
         String genOtp=Utilities.generateOtp();
         OTP otp=new OTP(email, genOtp, LocalDateTime.now());
         otpRepository.save(otp);
         message.setText(Data.getMessageBody(genOtp), true);
         mailSender.send(mm);
         return true;
    }

    @Override
    public Boolean verifyOtp(String email, String otp) throws JobPortalException {
       OTP otpEntity=otpRepository.findById(email).orElseThrow(()-> new JobPortalException("OTP Not Foud..."));
       if(!otpEntity.getOtpCode().equals(otp))throw new JobPortalException("OTP_INCORRECT");
       return true;
    }

    @Override
public RespnseDTO changePassword(LoginDTO loginDTO) throws JobPortalException {
    if (loginDTO.getEmail() == null || loginDTO.getEmail().isEmpty()) {
        throw new JobPortalException("EMAIL_REQUIRED");
    }
    if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
        throw new JobPortalException("PASSWORD_REQUIRED");
    }

    User user = usersRepository.findByEmail(loginDTO.getEmail())
        .orElseThrow(() -> new JobPortalException("USER_NOT_FOUND"));

    user.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
    usersRepository.save(user);
    NotificationDTO noti= new NotificationDTO();
    noti.setUserId(user.getId());
    noti.setMessage("Password Reset Successfully");
    noti.setAction("Password Reset");
    try {
        notificationService.sendNotification(noti);
    } catch (Exception e) {
        System.err.println("⚠️ Notification Error: " + e.getMessage());
    }
    
    return new RespnseDTO("Password Changed Successfully...");
}
@Scheduled(fixedRate = 60000) // Runs every 60 seconds
public void removeExpiredOTPs() {
    LocalDateTime expiry = LocalDateTime.now().minusMinutes(5);
    List<OTP> expiredOTPs = otpRepository.findByCreationTimeBefore(expiry); 

    if (!expiredOTPs.isEmpty()) {  // ✅ Fix: Check if NOT empty before deleting
        otpRepository.deleteAll(expiredOTPs);
        
    }
}


@Override
public void sendInterviewEmail(String email, String jobTitle, String company, String interviewTime, String interviewLocation) throws Exception {
    try {
        System.out.println("📩 Preparing email for: " + email);
        System.out.println("📌 Job Title: " + jobTitle);
        System.out.println("🏢 Company: " + company);
        System.out.println("🕒 Interview Time: " + interviewTime);
        System.out.println("📍 Interview Location: " + interviewLocation);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Interview Scheduled: " + jobTitle);

        String emailContent = "<!DOCTYPE html>"
                + "<html><head><meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<title>Interview Scheduled</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }"
                + ".container { background: white; padding: 20px; border-radius: 8px; text-align: center; }"
                + ".header { font-size: 20px; font-weight: bold; color: #2c3e50; }"
                + ".details { margin: 10px 0; font-size: 16px; color: #34495e; }"
                + "</style></head><body>"
                + "<div class='container'>"
                + "<div class='header'>📅 Your Interview is Scheduled!</div>"
                + "<p class='details'><strong>Job Title:</strong> " + jobTitle + "</p>"
                + "<p class='details'><strong>Company:</strong> " + company + "</p>"
                + "<p class='details'><strong>Interview Time:</strong> " + interviewTime + " (IST)</p>"
                + "<p class='details'><strong>Interview Location:</strong> " + interviewLocation + "</p>"
                + "<p>Good luck! Please be prepared for the interview.</p>"
                + "<hr>"
                + "<p class='footer'>Need help? Contact our support team at <a href='mailto:support@jobseekhelp.com'>support@jobseekhelp.com</a></p>"
                + "<p class='footer'>© 2024 JobSeeK. All rights reserved.</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(emailContent, true);

        System.out.println("📤 Sending email...");
        mailSender.send(message);
        System.out.println("✅ Email sent successfully!");

    } catch (Exception e) {
        System.err.println("❌ Email sending failed: " + e.getMessage());
        e.printStackTrace();
    }
}



@Override
public UserDTO getUserByEmail(String email) throws JobPortalException {
   return usersRepository.findByEmail(email) .orElseThrow(() -> new JobPortalException("USER_NOT_FOUND")).toDTO();
}

}
