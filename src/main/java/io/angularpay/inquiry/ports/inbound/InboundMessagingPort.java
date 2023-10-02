package io.angularpay.inquiry.ports.inbound;

import io.angularpay.inquiry.models.platform.PlatformConfigurationIdentifier;

public interface InboundMessagingPort {
    void onMessage(String message, PlatformConfigurationIdentifier identifier);
}
