package io.angularpay.inquiry.adapters.outbound;

import io.angularpay.inquiry.domain.Inquiry;
import io.angularpay.inquiry.domain.InquirerCategory;
import io.angularpay.inquiry.domain.InquiryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InquiryRepository extends MongoRepository<Inquiry, String> {

    Optional<Inquiry> findByReference(String reference);
    Optional<Inquiry> findByEmail(String email);
    Page<Inquiry> findByCategory(InquirerCategory category, Pageable pageable);
    Page<Inquiry> findByStatus(InquiryStatus status, Pageable pageable);
    long countByCategory(InquirerCategory category);
    long countByStatus(InquiryStatus status);
}
