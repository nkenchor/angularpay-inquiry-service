package io.angularpay.inquiry.adapters.inbound;

import io.angularpay.inquiry.domain.commands.PlatformConfigurationsConverterCommand;
import io.angularpay.inquiry.models.platform.PlatformConfigurationIdentifier;
import io.angularpay.inquiry.ports.inbound.InboundMessagingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static io.angularpay.inquiry.models.platform.PlatformConfigurationSource.TOPIC;

@Service
@RequiredArgsConstructor
public class RedisMessageAdapter implements InboundMessagingPort {

    private final PlatformConfigurationsConverterCommand converterCommand;

    @Override
    public void onMessage(String message, PlatformConfigurationIdentifier identifier) {
        this.converterCommand.execute(message, identifier, TOPIC);
    }
}
