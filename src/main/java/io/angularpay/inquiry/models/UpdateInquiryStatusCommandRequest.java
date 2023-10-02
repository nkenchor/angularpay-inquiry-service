package io.angularpay.inquiry.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UpdateInquiryStatusCommandRequest extends AccessControl {

    @NotEmpty
    private String reference;

    @NotNull
    @Valid
    private UpdateInquiryStatusApiModel updateInquiryStatusApiModel;

    UpdateInquiryStatusCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
