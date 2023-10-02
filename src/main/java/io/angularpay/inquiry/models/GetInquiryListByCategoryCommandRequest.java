package io.angularpay.inquiry.models;

import io.angularpay.inquiry.domain.InquirerCategory;
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
public class GetInquiryListByCategoryCommandRequest extends AccessControl {

    @NotNull
    private InquirerCategory category;

    @NotNull
    @Valid
    private Paging paging;

    GetInquiryListByCategoryCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
