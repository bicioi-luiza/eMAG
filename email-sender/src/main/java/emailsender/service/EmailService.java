package emailsender.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import emailsender.dtos.EmailRequestDto;
import emailsender.entities.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import repository.EmailRepository;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ObjectMapper objectMapper;

    private final EmailRepository emailRepository;

    @Autowired
    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public class EmailTemplateLoader {
        public String loadTemplate(String filePath) {
            try {
                return new String(Files.readAllBytes(Paths.get(filePath)));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void sendEmail(EmailRequestDto emailRequestDto) {
        try {
            // Convert EmailRequestDto to JSON string
            String jsonPayload = objectMapper.writeValueAsString(emailRequestDto);

            // Create MimeMessage
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set email properties
            helper.setFrom("luizaconstantina@gmail.com", "eMAG");
            helper.setTo(emailRequestDto.getRecipientEmail());
            helper.setSubject(emailRequestDto.getSubject());
            EmailTemplateLoader loader = new EmailTemplateLoader();
            String htmlTemplate = null;
            try {
                htmlTemplate = StreamUtils.copyToString(
                        new ClassPathResource("templates/email.html").getInputStream(),
                        StandardCharsets.UTF_8);
            } catch (IOException e) {
                // Handle file loading exception
                e.printStackTrace();
            }
            String modifiedHtmlContent = htmlTemplate.replace("$BODY_CONTENT$", emailRequestDto.getBody());
            helper.setText(modifiedHtmlContent, true); // true indicates HTML content
            // Attach chosen file to email
            String filePath = "C:\\Facultate\\sem2_an3\\PS\\eMAG\\";
            String fileType;

            if(emailRequestDto.getFileType().equals("txt"))
                fileType = "Order.txt";
            else if (emailRequestDto.getFileType().equals("pdf"))
                fileType = "Order.pdf";
            else
                fileType = "Order.csv";

            filePath = filePath + fileType;

            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(fileType, file);


            // Send email
            emailSender.send(message);

            // Save the email entity with status "SENT" initially
            Email emailEntity = new Email();
            emailEntity.setRecipientEmail(emailRequestDto.getRecipientEmail());
            emailEntity.setSubject(emailRequestDto.getSubject());
            emailEntity.setLastName(emailRequestDto.getLastName());
            emailEntity.setFirstName(emailRequestDto.getFirstName());
            emailEntity.setBody(emailRequestDto.getBody());
            emailRepository.save(emailEntity);

        } catch (JsonProcessingException | MessagingException | UnsupportedEncodingException e) {
            // Handle exception
            e.printStackTrace();
        }
    }
}
