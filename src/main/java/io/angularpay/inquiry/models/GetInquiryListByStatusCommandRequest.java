package io.angularpay.inquiry.models;

import io.angularpay.inquiry.domain.InquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetInquiryListByStatusCommandRequest extends AccessControl {

    @NotNull
    private InquiryStatus status;

    @NotNull
    @Valid
    private Paging paging;

    GetInquiryListByStatusCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
