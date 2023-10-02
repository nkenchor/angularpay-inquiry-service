package io.angularpay.inquiry.ports.outbound;

import io.angularpay.inquiry.models.GoogleReCaptchaRequest;
import io.angularpay.inquiry.models.GoogleReCaptchaResponse;

import java.util.Optional;

public interface GoogleReCaptchaV3Port {
    Optional<GoogleReCaptchaResponse> recapatcha(GoogleReCaptchaRequest request);
}
