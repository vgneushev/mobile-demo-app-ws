package com.devdemo.app.ws.shared.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.devdemo.app.ws.shared.dto.UserDto;
import com.devdemo.app.ws.ui.model.response.operation.RequestOperationStatus;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.devdemo.app.ws.shared.util.Constant.UTF_8;

@Slf4j
@Service
public class AmazonSES {
    private final String from = "test@test.com";

    //TODO: update with AWS account region
    private final AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
            .standard().withRegion(Regions.DEFAULT_REGION).build();

    public void verifyEmail(final UserDto userDto) {
        //TODO: update localhost with public address
        //TODO: move text to property or another file
        final String htmlBody = "<div>Please verify your email address via the link: <div/> <br/>"
                + "<a href='http:/localhost:8080/verification-service/email-verification.html?token=$tokenValue'>Verify email</a>";
        final String textBody = "Please verify your email address via the link: "
                + "http:/localhost:8080/verification-service/email-verification.html?token=$tokenValue";
        final String subject = "Email verification to complete registration";

        final String htmlBodyWithToken = htmlBody.replace("$tokenValue", userDto.getEmailVerificationToken());
        final String textBodyWithToken = textBody.replace("$tokenValue", userDto.getEmailVerificationToken());

        final SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination()
                                .withToAddresses(userDto.getEmail()))
                .withMessage(
                        new Message()
                                .withBody(
                                        new Body()
                                                .withHtml(
                                                        new Content()
                                                                .withCharset(UTF_8)
                                                                .withData(htmlBodyWithToken))
                                .withText(
                                        new Content()
                                                .withCharset(UTF_8)
                                                .withData(textBodyWithToken)))
                        .withSubject(
                                new Content()
                                        .withCharset(UTF_8)
                                        .withData(subject)))
                .withSource(from);

        client.sendEmail(request);
        log.info("Verify Email was sent to '{}'", userDto.getEmail());
    }

    public RequestOperationStatus sendPasswordResetRequest(
            @NonNull final String firstName, @NonNull final String email, @NonNull final String token) {
        //TODO: update localhost with public address
        final String htmlBody = "<div>Password reset request via the link: <div/> <br/>"
                + "<a href='http:/localhost:8080/verification-service/password-reset.html?token=$tokenValue'>Verify email</a>";
        final String textBody = "Password reset request via the link: "
                + "http:/localhost:8080/verification-service/password-reset.html?token=$tokenValue";
        final String passwordResetSubject = "Password reset request";

        final String htmlBodyWithToken = htmlBody.replace("$tokenValue", token);
        final String textBodyWithToken = textBody.replace("$tokenValue", token);

        final SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination()
                                .withToAddresses(email))
                .withMessage(
                        new Message()
                                .withBody(
                                        new Body().
                                                withHtml(
                                                        new Content()
                                                                .withCharset(UTF_8)
                                                                .withData(htmlBodyWithToken))
                                .withText(
                                        new Content()
                                                .withCharset(UTF_8)
                                                .withData(textBodyWithToken)))
                        .withSubject(
                                new Content()
                                        .withCharset(UTF_8)
                                        .withData(passwordResetSubject)))
                .withSource(from);

        log.info("Reset Password Email send to '{}'", email);
        return isResultSuccess(client.sendEmail(request))
                ? RequestOperationStatus.SUCCESS
                : RequestOperationStatus.ERROR;
    }

    private boolean isResultSuccess(SendEmailResult result) {
        return result != null && (result.getMessageId() != null && !result.getMessageId().isEmpty());
    }
}
