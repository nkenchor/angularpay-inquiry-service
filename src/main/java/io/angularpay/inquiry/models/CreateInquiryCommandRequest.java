package io.angularpay.inquiry.models;

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
public class CreateInquiryCommandRequest extends AccessControl {

    @NotNull
    @Valid
    private GenericInquiryApiModel genericInquiryApiModel;

    CreateInquiryCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
