package io.angularpay.inquiry.ports.outbound;

import io.angularpay.inquiry.domain.InquirerCategory;
import io.angularpay.inquiry.domain.Inquiry;
import io.angularpay.inquiry.domain.InquiryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PersistencePort {
    Inquiry createInquiry(Inquiry inquiry);
    Inquiry updateInquiry(Inquiry inquiry);
    Optional<Inquiry> findInquiryByReference(String reference);
    Optional<Inquiry> findInquiryByEmail(String email);
    Page<Inquiry> listInquiries(Pageable pageable);
    Page<Inquiry> listInquiryListByCategory(InquirerCategory category, Pageable pageable);
    Page<Inquiry>  listInquiryListByStatus(InquiryStatus status, Pageable pageable);
    long getCountByInquirerCategory(InquirerCategory category);
    long getCountByInquiryStatus(InquiryStatus status);
    long getTotalCount();
}
