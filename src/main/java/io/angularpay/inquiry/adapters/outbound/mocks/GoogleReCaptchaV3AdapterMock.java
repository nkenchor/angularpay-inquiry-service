package io.angularpay.inquiry.adapters.outbound.mocks;

import io.angularpay.inquiry.models.GoogleReCaptchaRequest;
import io.angularpay.inquiry.models.GoogleReCaptchaResponse;
import io.angularpay.inquiry.ports.outbound.GoogleReCaptchaV3Port;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleReCaptchaV3AdapterMock implements GoogleReCaptchaV3Port {

    @Override
    public Optional<GoogleReCaptchaResponse> recapatcha(GoogleReCaptchaRequest request) {
        GoogleReCaptchaResponse response = new GoogleReCaptchaResponse();
        return Optional.of(response);
    }
}
