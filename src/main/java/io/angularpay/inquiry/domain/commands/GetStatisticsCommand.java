package io.angularpay.inquiry.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.inquiry.adapters.outbound.MongoAdapter;
import io.angularpay.inquiry.domain.Role;
import io.angularpay.inquiry.domain.InquirerCategory;
import io.angularpay.inquiry.domain.InquiryStatus;
import io.angularpay.inquiry.exceptions.ErrorObject;
import io.angularpay.inquiry.models.GetStatisticsCommandRequest;
import io.angularpay.inquiry.models.Statistics;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class GetStatisticsCommand extends AbstractCommand<GetStatisticsCommandRequest, List<Statistics>> {

    private final MongoAdapter mongoAdapter;

    public GetStatisticsCommand(ObjectMapper mapper, MongoAdapter mongoAdapter) {
        super("GetStatisticsCommand", mapper);
        this.mongoAdapter = mongoAdapter;
    }

    @Override
    protected String getResourceOwner(GetStatisticsCommandRequest request) {
        return "";
    }

    @Override
    protected List<Statistics> handle(GetStatisticsCommandRequest request) {
        List<Statistics> statistics = new ArrayList<>();

        long total = this.mongoAdapter.getTotalCount();
        statistics.add(Statistics.builder()
                .name("Total")
                .value(String.valueOf(total))
                .build());

        long investor = this.mongoAdapter.getCountByInquirerCategory(InquirerCategory.INVESTOR);
        statistics.add(Statistics.builder()
                .name("Investor")
                .value(String.valueOf(investor))
                .build());

        long serviceProvider = this.mongoAdapter.getCountByInquirerCategory(InquirerCategory.SERVICE_PROVIDER);
        statistics.add(Statistics.builder()
                .name("Service Provider")
                .value(String.valueOf(serviceProvider))
                .build());

        long financialInstitution = this.mongoAdapter.getCountByInquirerCategory(InquirerCategory.FINANCIAL_INSTITUTION);
        statistics.add(Statistics.builder()
                .name("Financial Institution")
                .value(String.valueOf(financialInstitution))
                .build());

        long other = this.mongoAdapter.getCountByInquirerCategory(InquirerCategory.OTHER);
        statistics.add(Statistics.builder()
                .name("Other")
                .value(String.valueOf(other))
                .build());

        long pending = this.mongoAdapter.getCountByInquiryStatus(InquiryStatus.PENDING);
        statistics.add(Statistics.builder()
                .name("Status - Pending")
                .value(String.valueOf(pending))
                .build());

        long acknowledged = this.mongoAdapter.getCountByInquiryStatus(InquiryStatus.ACKNOWLEDGED);
        statistics.add(Statistics.builder()
                .name("Status - Acknowledged")
                .value(String.valueOf(acknowledged))
                .build());

        long markedAsSpam = this.mongoAdapter.getCountByInquiryStatus(InquiryStatus.MARKED_AS_SPAM);
        statistics.add(Statistics.builder()
                .name("Status - Spam")
                .value(String.valueOf(markedAsSpam))
                .build());

        long completed = this.mongoAdapter.getCountByInquiryStatus(InquiryStatus.COMPLETED);
        statistics.add(Statistics.builder()
                .name("Status - Completed")
                .value(String.valueOf(completed))
                .build());

        return statistics;
    }

    @Override
    protected List<ErrorObject> validate(GetStatisticsCommandRequest request) {
        return Collections.emptyList();
    }

    @Override
    protected List<Role> permittedRoles() {
        return Arrays.asList(Role.ROLE_PLATFORM_ADMIN, Role.ROLE_PLATFORM_USER);
    }
}
