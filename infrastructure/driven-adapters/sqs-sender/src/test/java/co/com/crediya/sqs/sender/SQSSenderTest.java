package co.com.crediya.sqs.sender;


import co.com.crediya.model.approvedloanrequestmetric.events.ApprovedLoanRequestMetricsNotificationEvent;
import co.com.crediya.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SQSSenderTest {
    @Mock
    private SQSSenderProperties properties;

    @Mock
    private SqsAsyncClient client;

    private ObjectMapper objectMapper;

    private SQSSender sqsSender;

    private ApprovedLoanRequestMetricsNotificationEvent event;

    @BeforeEach
    void setUp() {
        objectMapper = spy(new ObjectMapper());
        sqsSender = new SQSSender(properties, client, objectMapper);

        event = new ApprovedLoanRequestMetricsNotificationEvent(
                5L,
                new BigDecimal("500000"),
                new Date()
        );
    }

    @Test
    void publishNotificationShouldSendEventSuccessfully() throws JsonProcessingException {
        var response = SendMessageResponse
                .builder().messageId("msg-123").build();
        when(client.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        StepVerifier.create(sqsSender.publishNotification(event))
                .verifyComplete();

        verify(objectMapper).writeValueAsString(event);
        verify(client).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void publishNotificationShouldReturnErrorOnJsonProcessingException() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any()))
                .thenThrow(new JsonProcessingException("JSON error") { });

        StepVerifier.create(sqsSender.publishNotification(event))
                .expectError(JsonProcessingException.class)
                .verify();
    }
}
