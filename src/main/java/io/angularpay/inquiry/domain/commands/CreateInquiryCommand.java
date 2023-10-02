package io.angularpay.inquiry.domain.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.angularpay.inquiry.adapters.outbound.GoogleReCaptchaV3Adapter;
import io.angularpay.inquiry.adapters.outbound.MongoAdapter;
import io.angularpay.inquiry.configurations.AngularPayConfiguration;
import io.angularpay.inquiry.domain.Inquiry;
import io.angularpay.inquiry.domain.InquiryStatus;
import io.angularpay.inquiry.domain.Role;
import io.angularpay.inquiry.exceptions.CommandException;
import io.angularpay.inquiry.exceptions.ErrorObject;
import io.angularpay.inquiry.models.CreateInquiryCommandRequest;
import io.angularpay.inquiry.models.GoogleReCaptchaRequest;
import io.angularpay.inquiry.models.GoogleReCaptchaResponse;
import io.angularpay.inquiry.models.ResourceReferenceResponse;
import io.angularpay.inquiry.ports.outbound.GoogleReCaptchaV3Port;
import io.angularpay.inquiry.validation.DefaultConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.angularpay.inquiry.exceptions.ErrorCode.AUTHORIZATION_ERROR;
import static io.angularpay.inquiry.helpers.CommandHelper.validateNotExistOrThrow;
import static io.angularpay.inquiry.helpers.Helper.maskEmail;
import static io.angularpay.inquiry.helpers.Helper.maskPhone;

@Slf4j
@Service
public class CreateInquiryCommand extends AbstractCommand<CreateInquiryCommandRequest, ResourceReferenceResponse>
        implements SensitiveDataCommand<CreateInquiryCommandRequest> {

    private final DefaultConstraintValidator validator;
    private final GoogleReCaptchaV3Port googleReCaptchaV3Port;
    private final AngularPayConfiguration configuration;
    private final MongoAdapter mongoAdapter;

    public CreateInquiryCommand(
            ObjectMapper mapper,
            DefaultConstraintValidator validator,
            GoogleReCaptchaV3Adapter googleReCaptchaV3Port,
            AngularPayConfiguration configuration,
            MongoAdapter mongoAdapter) {
        super("CreateInquiryCommand", mapper);
        this.validator = validator;
        this.googleReCaptchaV3Port = googleReCaptchaV3Port;
        this.configuration = configuration;
        this.mongoAdapter = mongoAdapter;
    }

    @Override
    protected String getResourceOwner(CreateInquiryCommandRequest request) {
        return request.getGenericInquiryApiModel().getEmail();
    }

    @Override
    protected ResourceReferenceResponse handle(CreateInquiryCommandRequest request) {
        validateNotExistOrThrow(this.mongoAdapter, request.getGenericInquiryApiModel().getEmail());

        boolean success = !this.configuration.getGoogleRecaptcha().isEnabled() || isRecaptchaSuccess(request.getGenericInquiryApiModel().getRecaptcha());
        if (!success) {
            throw CommandException.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .errorCode(AUTHORIZATION_ERROR)
                    .message(AUTHORIZATION_ERROR.getDefaultMessage())
                    .build();
        }

        Inquiry inquiry = Inquiry.builder()
                .reference(UUID.randomUUID().toString())
                .inquirerName(request.getGenericInquiryApiModel().getInquirerName())
                .email(request.getGenericInquiryApiModel().getEmail())
                .phone(request.getGenericInquiryApiModel().getPhone())
                .subject(request.getGenericInquiryApiModel().getSubject())
                .message(request.getGenericInquiryApiModel().getMessage())
                .category(request.getGenericInquiryApiModel().getCategory())
                .status(InquiryStatus.PENDING)
                .build();

        Inquiry response = mongoAdapter.createInquiry(inquiry);

        return new ResourceReferenceResponse(response.getReference());
    }

    private boolean isRecaptchaSuccess(GoogleReCaptchaRequest reCaptchaRequest) {
        Optional<GoogleReCaptchaResponse> optional = this.googleReCaptchaV3Port.recapatcha(GoogleReCaptchaRequest.builder()
                .recaptchaToken(reCaptchaRequest.getRecaptchaToken())
                .actionName(reCaptchaRequest.getActionName())
                .build());

        if (optional.isEmpty()) {
            throw CommandException.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .errorCode(AUTHORIZATION_ERROR)
                    .message(AUTHORIZATION_ERROR.getDefaultMessage())
                    .build();
        }

        GoogleReCaptchaResponse googleReCaptchaResponse = optional.get();

        String captchaRequestActionName = reCaptchaRequest.getActionName();
        float expectedScore = configuration.getGoogleRecaptcha().getThreshold();
        String captchaResponseActionName = googleReCaptchaResponse.getAction();
        float scoreFromResponse = googleReCaptchaResponse.getScore();
        boolean verificationResultOk = googleReCaptchaResponse.isSuccess();

        return captchaResponseActionName.equals(captchaRequestActionName) && verificationResultOk && scoreFromResponse > expectedScore;
    }

    @Override
    protected List<ErrorObject> validate(CreateInquiryCommandRequest request) {
        return this.validator.validate(request);
    }

    @Override
    protected List<Role> permittedRoles() {
        return Collections.emptyList();
    }

    @Override
    public CreateInquiryCommandRequest mask(CreateInquiryCommandRequest raw) {
        try {
            JsonNode node = mapper.convertValue(raw, JsonNode.class);
            JsonNode genericInquiryApiModel = node.get("genericInquiryApiModel");
            ((ObjectNode) genericInquiryApiModel).put("email", maskEmail(raw.getGenericInquiryApiModel().getEmail()));
            ((ObjectNode) genericInquiryApiModel).put("phone", maskPhone(raw.getGenericInquiryApiModel().getPhone()));
            return mapper.treeToValue(node, CreateInquiryCommandRequest.class);
        } catch (JsonProcessingException exception) {
            return raw;
        }
    }
}
