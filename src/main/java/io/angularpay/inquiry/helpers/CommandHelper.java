package io.angularpay.inquiry.helpers;

import io.angularpay.inquiry.adapters.outbound.MongoAdapter;
import io.angularpay.inquiry.domain.Inquiry;
import io.angularpay.inquiry.exceptions.CommandException;
import io.angularpay.inquiry.exceptions.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static io.angularpay.inquiry.exceptions.ErrorCode.DUPLICATE_REQUEST_ERROR;
import static io.angularpay.inquiry.exceptions.ErrorCode.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommandHelper {

    public static Inquiry getInquiryByReferenceOrThrow(MongoAdapter mongoAdapter, String reference) {
        return mongoAdapter.findInquiryByReference(reference).orElseThrow(
                () -> commandException(HttpStatus.NOT_FOUND, REQUEST_NOT_FOUND)
        );
    }

    public static void validateNotExistOrThrow(MongoAdapter mongoAdapter, String email) {
        mongoAdapter.findInquiryByEmail(email).ifPresent(
                (x) -> {
                    throw commandException(HttpStatus.CONFLICT, DUPLICATE_REQUEST_ERROR);
                }
        );
    }

    private static CommandException commandException(HttpStatus status, ErrorCode errorCode) {
        return CommandException.builder()
                .status(status)
                .errorCode(errorCode)
                .message(errorCode.getDefaultMessage())
                .build();
    }

}
