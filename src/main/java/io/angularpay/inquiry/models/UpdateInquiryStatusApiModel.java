package io.angularpay.inquiry.models;

import io.angularpay.inquiry.domain.InquiryStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateInquiryStatusApiModel {

    @NotNull
    private InquiryStatus status;
}
