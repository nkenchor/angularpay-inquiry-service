package io.angularpay.inquiry.adapters.outbound;

import io.angularpay.inquiry.domain.InquirerCategory;
import io.angularpay.inquiry.domain.Inquiry;
import io.angularpay.inquiry.domain.InquiryStatus;
import io.angularpay.inquiry.ports.outbound.PersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MongoAdapter implements PersistencePort {

    private final InquiryRepository inquiryRepository;

    @Override
    public Inquiry createInquiry(Inquiry inquiry) {
        inquiry.setCreatedOn(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        inquiry.setLastModified(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        return inquiryRepository.save(inquiry);
    }

    @Override
    public Inquiry updateInquiry(Inquiry inquiry) {
        inquiry.setLastModified(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        return inquiryRepository.save(inquiry);
    }

    @Override
    public Optional<Inquiry> findInquiryByReference(String reference) {
        return inquiryRepository.findByReference(reference);
    }

    @Override
    public Optional<Inquiry> findInquiryByEmail(String email) {
        return inquiryRepository.findByEmail(email);
    }

    @Override
    public Page<Inquiry> listInquiries(Pageable pageable) {
        return this.inquiryRepository.findAll(pageable);
    }

    @Override
    public Page<Inquiry> listInquiryListByCategory(InquirerCategory category, Pageable pageable) {
        return this.inquiryRepository.findByCategory(category, pageable);
    }

    @Override
    public Page<Inquiry> listInquiryListByStatus(InquiryStatus status, Pageable pageable) {
        return this.inquiryRepository.findByStatus(status, pageable);
    }

    @Override
    public long getCountByInquirerCategory(InquirerCategory category) {
        return inquiryRepository.countByCategory(category);
    }

    @Override
    public long getCountByInquiryStatus(InquiryStatus status) {
        return inquiryRepository.countByStatus(status);
    }

    @Override
    public long getTotalCount() {
        return inquiryRepository.count();
    }

}
