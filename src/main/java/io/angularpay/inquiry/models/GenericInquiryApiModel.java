package io.angularpay.inquiry.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.angularpay.inquiry.domain.InquirerCategory;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static io.angularpay.inquiry.common.Constants.REGEX_EMAIL_ADDRESS;

@Data
public class GenericInquiryApiModel {

    @NotEmpty
    @JsonProperty("inquirer_name")
    private String inquirerName;

    @Pattern(regexp = REGEX_EMAIL_ADDRESS, message = "Invalid email address", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String email;

    @NotEmpty
    private String phone;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String message;

    @NotNull
    private InquirerCategory category;

    @NotNull
    @Valid
    private GoogleReCaptchaRequest recaptcha;
}
