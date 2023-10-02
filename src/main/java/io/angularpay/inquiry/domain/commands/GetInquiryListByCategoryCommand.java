package io.angularpay.inquiry.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.inquiry.adapters.outbound.MongoAdapter;
import io.angularpay.inquiry.domain.Inquiry;
import io.angularpay.inquiry.domain.Role;
import io.angularpay.inquiry.exceptions.ErrorObject;
import io.angularpay.inquiry.models.GetInquiryListByCategoryCommandRequest;
import io.angularpay.inquiry.validation.DefaultConstraintValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class GetInquiryListByCategoryCommand extends AbstractCommand<GetInquiryListByCategoryCommandRequest, List<Inquiry>> {

    private final MongoAdapter mongoAdapter;
    private final DefaultConstraintValidator validator;

    public GetInquiryListByCategoryCommand(ObjectMapper mapper, MongoAdapter mongoAdapter, DefaultConstraintValidator validator) {
        super("GetInquiryListByCategoryCommand", mapper);
        this.mongoAdapter = mongoAdapter;
        this.validator = validator;
    }

    @Override
    protected String getResourceOwner(GetInquiryListByCategoryCommandRequest request) {
        return "";
    }

    @Override
    protected List<Inquiry> handle(GetInquiryListByCategoryCommandRequest request) {
        Pageable pageable = PageRequest.of(request.getPaging().getIndex(), request.getPaging().getSize());
        return this.mongoAdapter.listInquiryListByCategory(request.getCategory(), pageable).getContent();
    }

    @Override
    protected List<ErrorObject> validate(GetInquiryListByCategoryCommandRequest request) {
        return this.validator.validate(request);
    }

    @Override
    protected List<Role> permittedRoles() {
        return Arrays.asList(Role.ROLE_INQUIRY_ADMIN, Role.ROLE_PLATFORM_ADMIN);
    }
}
