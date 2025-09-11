package co.com.crediya.dynamodb.adapter;

import co.com.crediya.dynamodb.entity.ApprovedLoanRequestMetricEntity;
import co.com.crediya.dynamodb.helper.TemplateAdapterOperations;
import co.com.crediya.model.approvedloanrequestmetric.ApprovedLoanRequestMetric;
import co.com.crediya.model.approvedloanrequestmetric.enums.ApprovedLoanRequestMetrics;
import co.com.crediya.model.approvedloanrequestmetric.gateways.ApprovedLoanRequestMetricRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;


@Repository
public class ApprovedLoanRequestMetricDynamoDBAdapter
        extends TemplateAdapterOperations<ApprovedLoanRequestMetric, String, ApprovedLoanRequestMetricEntity>
        implements ApprovedLoanRequestMetricRepository {

    public ApprovedLoanRequestMetricDynamoDBAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        super(
                connectionFactory,
                mapper,
                d -> mapper.map(d, ApprovedLoanRequestMetric.class),
                "approved_loan_request_metrics"
        );
    }

    @Override
    public Mono<ApprovedLoanRequestMetric> findByMetricCode(String code) {
        return getById(code);
    }

    @Override
    public Mono<ApprovedLoanRequestMetric> saveMetric(ApprovedLoanRequestMetric metric) {
        return save(metric);
    }
}
