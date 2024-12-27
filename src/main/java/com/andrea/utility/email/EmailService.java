package com.andrea.utility.email;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class EmailService {
    private String from = "andrea55fusella@gmail.com";
    private String host = "smtp.gmail.com";
    private String password = "wyke jxnd pfbf fogc";

    private void sendEmail(String to, String subject, String body) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void generateAndSendEmail(String to, String method) {
        switch (method) {
            case "Create Account":
                CreateAccount(to);
                break;
            case "Delete Account":
                DeleteAccount(to);
                break;
        }
    }

    private void CreateAccount(String to) {
        String subject = "Welcome to Our Service!";

        String body = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f9;
                        margin: 0;
                        padding: 0;
                    }
                    .email-container {
                        max-width: 600px;
                        margin: 20px auto;
                        background-color: #ffffff;
                        border-radius: 8px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                        overflow: hidden;
                    }
                    .header {
                        background-color: #28a745; /* Colore verde più acceso */
                        color: white;
                        text-align: center;
                        padding: 20px;
                    }
                    .header h1 {
                        margin: 0;
                        color: white; /* Assicura che il testo sia bianco */
                    }
                    .content {
                        padding: 20px;
                        text-align: center;
                    }
                    .content img {
                        max-width: 100%;
                        border-radius: 8px;
                    }
                    .content h2 {
                        color: #333;
                    }
                    .content p {
                        color: #666;
                        line-height: 1.5;
                    }
                    .button {
                        display: inline-block;
                        margin-top: 20px;
                        padding: 10px 20px;
                        background-color: #28a745; /* Colore verde più acceso */
                        color: white; /* Testo bianco */
                        text-decoration: none;
                        border-radius: 5px;
                        font-weight: bold;
                    }
                    .footer {
                        background-color: #f4f4f9;
                        color: #666;
                        text-align: center;
                        padding: 10px 20px;
                        font-size: 12px;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="header">
                        <h1>Welcome to Our Service!</h1>
                    </div>
                    <div class="content">
                        <h2>Hi there!</h2>
                        <p>Your account has been successfully created. We're excited to have you on board.</p>
                        <img src="https://via.placeholder.com/600x200.png?text=Welcome+to+Our+Service" alt="Welcome">
                        <p>Start exploring our features by logging into your account. If you have any questions, we're here to help!</p>
                        <a href="http://localhost:3000/login" class="button">Go to Dashboard</a>
                    </div>
                    <div class="footer">
                        <p>© 2024 Our Service. All rights reserved.</p>
                        <p>If you didn't create this account, please <a href="mailto:support@ourservice.com">contact us</a>.</p>
                    </div>
                </div>
            </body>
            </html>
        """;
        sendEmail(to, subject, body);
    }

    private void DeleteAccount(String to) {
        String subject = "Your Account Has Been Deleted by an Administrator";

        String body = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f9;
                    margin: 0;
                    padding: 0;
                }
                .email-container {
                    max-width: 600px;
                    margin: 20px auto;
                    background-color: #ffffff;
                    border-radius: 8px;
                    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    overflow: hidden;
                }
                .header {
                    background-color: #dc3545; /* Rosso acceso */
                    color: white;
                    text-align: center;
                    padding: 20px;
                }
                .header h1 {
                    margin: 0;
                    color: white; /* Testo bianco */
                }
                .content {
                    padding: 20px;
                    text-align: center;
                }
                .content img {
                    max-width: 100%;
                    border-radius: 8px;
                }
                .content h2 {
                    color: #333;
                }
                .content p {
                    color: #666;
                    line-height: 1.5;
                }
                .button {
                    display: inline-block;
                    margin-top: 20px;
                    padding: 10px 20px;
                    background-color: #dc3545; /* Rosso acceso */
                    color: white;
                    text-decoration: none;
                    border-radius: 5px;
                    font-weight: bold;
                }
                .footer {
                    background-color: #f4f4f9;
                    color: #666;
                    text-align: center;
                    padding: 10px 20px;
                    font-size: 12px;
                }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="header">
                    <h1>We're Sorry to See You Go</h1>
                </div>
                <div class="content">
                    <h2>Goodbye,</h2>
                    <p>Your account has been removed from our systems by an administrator. We hope you had a great experience with EduHub</p>
                    <img src="https://via.placeholder.com/600x200.png?text=Thank+You+For+Being+With+Us" alt="Goodbye">
                    <p>If you notice something strange in the removal of your account we ask you to contact us at support by clicking the button below.</p>
                    <a href="mailto:support@ourservice.com" class="button">Contact Support</a>
                </div>
                <div class="footer">
                    <p>© 2024 Our Service. All rights reserved.</p>
                    <p>If you believe this action was a mistake, please <a href="mailto:support@ourservice.com">contact us</a> immediately.</p>
                </div>
            </div>
        </body>
        </html>
        """;

        sendEmail(to, subject, body);
    }
}
