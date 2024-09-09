package com.devdemo.app.ws.shared.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.devdemo.app.ws.shared.dto.UserDto;
import lombok.extern.slf4j.Slf4j;

import static com.devdemo.app.ws.shared.util.Constant.UTF_8;

@Slf4j
public class AmazonSES {
    private final String from = "test@test.com";

    private final String subject = "Email verification to complete registration";

    //TODO: update localhost with public address
    private final String htmlBody = "<div>Please verify your email address via the link: <div/> <br/>"
            + "<a href='http:/localhost:8080/verification-service/email-verification.html?token=$tokenValue'>Verify email</a>";
    private final String textBody = "Please verify your email address via the link: "
            + "http:/localhost:8080/verification-service/email-verification.html?token=$tokenValue";

    public void verifyEmail(final UserDto userDto) {
        //TODO: update with AWS account region
        final AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
                .standard().withRegion(Regions.DEFAULT_REGION).build();

        final String htmlBodyWithToken = htmlBody.replace("$tokenValue", userDto.getEmailVerificationToken());
        final String textBodyWithToken = textBody.replace("$tokenValue", userDto.getEmailVerificationToken());

        final SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(userDto.getEmail()))
                .withMessage( new Message().withBody( new Body().withHtml(new Content().withCharset(UTF_8).withData(htmlBodyWithToken))
                        .withText( new Content().withCharset(UTF_8).withData(textBodyWithToken)))
                        .withSubject(new Content().withCharset(UTF_8).withData(subject)))
                .withSource(from);

        client.sendEmail(request);
        log.info("Email was sent to '{}'", userDto.getEmail());
    }
}
