package com.jobportal.utility;

public class Data {

    public static String getMessageBody(String otp){
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<title>Your OTP Code</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }"
                + ".container { width: 100%; max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; "
                + "border-radius: 8px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); text-align: center; }"
                + ".otp-code { font-size: 24px; font-weight: bold; color: #2c3e50; padding: 10px 20px; "
                + "background: #f8f9fa; display: inline-block; border-radius: 5px; margin: 20px 0; letter-spacing: 3px; }"
                + ".footer { margin-top: 20px; font-size: 12px; color: #7d7d7d; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<h2>🔐 Your OTP Code</h2>"
                + "<p>Hello,</p>"
                + "<p>Your One-Time Password (OTP) for verification is:</p>"
                + "<div class='otp-code'>" +otp+ "</div>"
                + "<p>This OTP is valid for <b>10 minutes</b>. Please do not share it with anyone.</p>"
                + "<p>If you did not request this code, please ignore this email.</p>"
                + "<hr>"
                + "<p class='footer'>Need help? Contact our support team at <a href='mailto:support@jobseekhelp.com'>support@jobseekhelp.com</a></p>"
                + "<p class='footer'>© 2024 JobSeeK. All rights reserved.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

}
