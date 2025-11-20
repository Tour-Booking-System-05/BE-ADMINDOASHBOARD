package com.travel.demo.service.impl;

import com.travel.demo.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Async
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public void sendResetPasswordEmail(String to, String newPassword) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Mật khẩu mới của bạn");

            // HTML template
            String html = """
                <div style="font-family: Arial, sans-serif; padding: 20px;">
                    <h2 style="color: #2b7cff;">Yêu cầu đặt lại mật khẩu</h2>
                    <p>Xin chào,</p>
                    <p>Bạn đã yêu cầu reset mật khẩu. Đây là mật khẩu mới của bạn:</p>

                    <div style="padding: 10px 20px; background: #f2f2f2; 
                                display: inline-block; font-size: 18px; 
                                border-radius: 8px; margin: 15px 0;">
                        <b>%s</b>
                    </div>

                    <p>Vui lòng đăng nhập và đổi lại mật khẩu để bảo mật tài khoản.</p>

                    <a href="https://www.travelweb.com/login" 
                       style="display: inline-block; padding: 10px 18px; 
                              background: #2b7cff; color: white; 
                              text-decoration: none; border-radius: 5px;">
                        Đăng nhập ngay
                    </a>

                    <br><br>
                    <p>Trân trọng,<br>Hệ thống Travel Admin</p>
                </div>
                """.formatted(newPassword);

            helper.setText(html, true); // true = HTML

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi gửi email: " + e.getMessage());
        }
    }
}
