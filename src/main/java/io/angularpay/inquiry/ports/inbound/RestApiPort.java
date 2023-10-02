package io.angularpay.inquiry.ports.inbound;

import io.angularpay.inquiry.domain.InquirerCategory;
import io.angularpay.inquiry.domain.Inquiry;
import io.angularpay.inquiry.domain.InquiryStatus;
import io.angularpay.inquiry.models.GenericInquiryApiModel;
import io.angularpay.inquiry.models.GenericReferenceResponse;
import io.angularpay.inquiry.models.Statistics;
import io.angularpay.inquiry.models.UpdateInquiryStatusApiModel;

import java.util.List;
import java.util.Map;

public interface RestApiPort {
    GenericReferenceResponse createInquiry(GenericInquiryApiModel request, Map<String, String> headers);
    void updateInquiryByStatus(String inquiryReference, UpdateInquiryStatusApiModel updateInquiryStatusApiModel, Map<String, String> headers);
    Inquiry getInquiryByReference(String inquiryReference, Map<String, String> headers);
    List<Inquiry> getInquiryList(int page, Map<String, String> headers);
    List<Inquiry> getInquiryByCategory(InquirerCategory category, int page, Map<String, String> headers);
    List<Inquiry> getInquiryByStatus(InquiryStatus status, int page, Map<String, String> headers);
    List<Statistics> getStatistics(Map<String, String> headers);
}
