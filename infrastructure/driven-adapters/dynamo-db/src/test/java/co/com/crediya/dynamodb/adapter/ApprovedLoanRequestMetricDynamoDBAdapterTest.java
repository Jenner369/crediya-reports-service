package co.com.crediya.dynamodb.adapter;

import co.com.crediya.dynamodb.entity.ApprovedLoanRequestMetricEntity;
import co.com.crediya.model.approvedloanrequestmetric.ApprovedLoanRequestMetric;
import co.com.crediya.model.approvedloanrequestmetric.enums.ApprovedLoanRequestMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;

class ApprovedLoanRequestMetricDynamoDBAdapterTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private DynamoDbAsyncTable<ApprovedLoanRequestMetricEntity> table;

    private ApprovedLoanRequestMetricDynamoDBAdapter adapter;

    private ApprovedLoanRequestMetricEntity entity;
    private ApprovedLoanRequestMetric model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        entity = new ApprovedLoanRequestMetricEntity(
                ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode(),
                "10"
        );

        model = new ApprovedLoanRequestMetric(
                ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode(),
                "10"
        );

        when(dynamoDbEnhancedAsyncClient.table("approved_loan_request_metrics",
                software.amazon.awssdk.enhanced.dynamodb.TableSchema.fromBean(ApprovedLoanRequestMetricEntity.class)))
                .thenReturn(table);

        adapter = new ApprovedLoanRequestMetricDynamoDBAdapter(dynamoDbEnhancedAsyncClient, mapper);
    }

    @Test
    void testFindByMetricCode() {
        String code = ApprovedLoanRequestMetrics.APPROVED_LOAN_COUNT.getCode();

        when(table.getItem(Key.builder().partitionValue(code).build()))
                .thenReturn(CompletableFuture.completedFuture(entity));

        when(mapper.map(entity, ApprovedLoanRequestMetric.class))
                .thenReturn(model);

        StepVerifier.create(adapter.findByMetricCode(code))
                .expectNext(model)
                .verifyComplete();
    }

    @Test
    void testSaveMetric() {
        when(mapper.map(model, ApprovedLoanRequestMetricEntity.class)).thenReturn(entity);
        when(table.putItem(entity)).thenReturn(CompletableFuture.completedFuture(null));

        StepVerifier.create(adapter.saveMetric(model))
                .expectNext(model)
                .verifyComplete();
    }
}
