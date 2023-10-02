package io.angularpay.inquiry.adapters.inbound;

import io.angularpay.inquiry.configurations.AngularPayConfiguration;
import io.angularpay.inquiry.domain.Inquiry;
import io.angularpay.inquiry.domain.InquirerCategory;
import io.angularpay.inquiry.domain.InquiryStatus;
import io.angularpay.inquiry.domain.commands.*;
import io.angularpay.inquiry.models.*;
import io.angularpay.inquiry.ports.inbound.RestApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static io.angularpay.inquiry.helpers.Helper.fromHeaders;

@RestController
@RequestMapping("/inquiries/partners")
@RequiredArgsConstructor
public class RestApiAdapter implements RestApiPort {

    private final CreateInquiryCommand createInquiryCommand;
    private final UpdateInquiryStatusCommand updateInquiryStatusCommand;
    private final GetInquiryByReferenceCommand getInquiryByReferenceCommand;
    private final GetInquiryListCommand getInquiryListCommand;
    private final GetInquiryListByCategoryCommand getInquiryListByCategoryCommand;
    private final GetInquiryListByStatusCommand getInquiryListByStatusCommand;
    private final GetStatisticsCommand getStatisticsCommand;

    private final AngularPayConfiguration configuration;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public GenericReferenceResponse createInquiry(
            @RequestBody GenericInquiryApiModel request,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        authenticatedUser.setUsername(request.getEmail());
        authenticatedUser.setUserReference(request.getEmail());
        CreateInquiryCommandRequest createInquiryCommandRequest = CreateInquiryCommandRequest.builder()
                .genericInquiryApiModel(request)
                .authenticatedUser(authenticatedUser)
                .build();
        return this.createInquiryCommand.execute(createInquiryCommandRequest);
    }

    @PutMapping("{inquiryReference}/status")
    @Override
    public void updateInquiryByStatus(
            @PathVariable String inquiryReference,
            @RequestBody UpdateInquiryStatusApiModel updateInquiryStatusApiModel,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        UpdateInquiryStatusCommandRequest updateInquiryStatusCommandRequest = UpdateInquiryStatusCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .reference(inquiryReference)
                .updateInquiryStatusApiModel(updateInquiryStatusApiModel)
                .build();
        this.updateInquiryStatusCommand.execute(updateInquiryStatusCommandRequest);
    }

    @GetMapping("{inquiryReference}")
    @ResponseBody
    @Override
    public Inquiry getInquiryByReference(
            @PathVariable String inquiryReference,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GenericInquiryCommandRequest genericInquiryCommandRequest = GenericInquiryCommandRequest.builder()
                .reference(inquiryReference)
                .authenticatedUser(authenticatedUser)
                .build();
        return this.getInquiryByReferenceCommand.execute(genericInquiryCommandRequest);
    }

    @GetMapping("/list/page/{page}")
    @ResponseBody
    @Override
    public List<Inquiry> getInquiryList(
            @PathVariable int page,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetInquiryListCommandRequest getInquiryListCommandRequest = GetInquiryListCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .paging(Paging.builder().size(this.configuration.getPageSize()).index(page).build())
                .build();
        return this.getInquiryListCommand.execute(getInquiryListCommandRequest);
    }

    @GetMapping("/list/category/{category}/page/{page}")
    @ResponseBody
    @Override
    public List<Inquiry> getInquiryByCategory(
            @PathVariable InquirerCategory category,
            @PathVariable int page,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetInquiryListByCategoryCommandRequest getInquiryListByCategoryCommandRequest = GetInquiryListByCategoryCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .category(category)
                .paging(Paging.builder().size(this.configuration.getPageSize()).index(page).build())
                .build();
        return this.getInquiryListByCategoryCommand.execute(getInquiryListByCategoryCommandRequest);
    }

    @GetMapping("/list/status/{status}/page/{page}")
    @ResponseBody
    @Override
    public List<Inquiry> getInquiryByStatus(
            @PathVariable InquiryStatus status,
            @PathVariable int page,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetInquiryListByStatusCommandRequest getInquiryListByStatusCommandRequest = GetInquiryListByStatusCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .status(status)
                .paging(Paging.builder().size(this.configuration.getPageSize()).index(page).build())
                .build();
        return this.getInquiryListByStatusCommand.execute(getInquiryListByStatusCommandRequest);
    }

    @GetMapping("/statistics")
    @ResponseBody
    @Override
    public List<Statistics> getStatistics(@RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetStatisticsCommandRequest getStatisticsCommandRequest = GetStatisticsCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .build();
        return getStatisticsCommand.execute(getStatisticsCommandRequest);
    }
}
