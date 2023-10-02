package io.angularpay.inquiry.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.inquiry.adapters.outbound.MongoAdapter;
import io.angularpay.inquiry.domain.Inquiry;
import io.angularpay.inquiry.domain.Role;
import io.angularpay.inquiry.exceptions.ErrorObject;
import io.angularpay.inquiry.models.GetInquiryListCommandRequest;
import io.angularpay.inquiry.validation.DefaultConstraintValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class GetInquiryListCommand extends AbstractCommand<GetInquiryListCommandRequest, List<Inquiry>> {

    private final MongoAdapter mongoAdapter;
    private final DefaultConstraintValidator validator;

    public GetInquiryListCommand(ObjectMapper mapper, MongoAdapter mongoAdapter, DefaultConstraintValidator validator) {
        super("GetInquiryListCommand", mapper);
        this.mongoAdapter = mongoAdapter;
        this.validator = validator;
    }

    @Override
    protected String getResourceOwner(GetInquiryListCommandRequest request) {
        return "";
    }

    @Override
    protected List<Inquiry> handle(GetInquiryListCommandRequest request) {
        Pageable pageable = PageRequest.of(request.getPaging().getIndex(), request.getPaging().getSize());
        return this.mongoAdapter.listInquiries(pageable).getContent();
    }

    @Override
    protected List<ErrorObject> validate(GetInquiryListCommandRequest request) {
        return this.validator.validate(request);
    }

    @Override
    protected List<Role> permittedRoles() {
        return Arrays.asList(Role.ROLE_INQUIRY_ADMIN, Role.ROLE_PLATFORM_ADMIN);
    }
}
