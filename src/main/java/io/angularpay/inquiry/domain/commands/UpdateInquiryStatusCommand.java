package io.angularpay.inquiry.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.inquiry.adapters.outbound.MongoAdapter;
import io.angularpay.inquiry.domain.Inquiry;
import io.angularpay.inquiry.domain.Role;
import io.angularpay.inquiry.exceptions.ErrorObject;
import io.angularpay.inquiry.models.UpdateInquiryStatusCommandRequest;
import io.angularpay.inquiry.validation.DefaultConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static io.angularpay.inquiry.helpers.CommandHelper.getInquiryByReferenceOrThrow;

@Slf4j
@Service
public class UpdateInquiryStatusCommand extends AbstractCommand<UpdateInquiryStatusCommandRequest, Void> {

    private final DefaultConstraintValidator validator;
    private final MongoAdapter mongoAdapter;

    public UpdateInquiryStatusCommand(
            ObjectMapper mapper,
            DefaultConstraintValidator validator,
            MongoAdapter mongoAdapter) {
        super("UpdateInquiryStatusCommand", mapper);
        this.validator = validator;
        this.mongoAdapter = mongoAdapter;
    }

    @Override
    protected String getResourceOwner(UpdateInquiryStatusCommandRequest request) {
        return "";
    }

    @Override
    protected Void handle(UpdateInquiryStatusCommandRequest request) {
        Inquiry inquiry = getInquiryByReferenceOrThrow(this.mongoAdapter, request.getReference())
                .toBuilder()
                .status(request.getUpdateInquiryStatusApiModel().getStatus())
                .build();
        this.mongoAdapter.updateInquiry(inquiry);
        return null;
    }

    @Override
    protected List<ErrorObject> validate(UpdateInquiryStatusCommandRequest request) {
        return this.validator.validate(request);
    }

    @Override
    protected List<Role> permittedRoles() {
        return Arrays.asList(Role.ROLE_INQUIRY_ADMIN, Role.ROLE_PLATFORM_ADMIN);
    }

}
