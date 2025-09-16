package co.com.crediya.sqs.sender;

import co.com.crediya.model.approvedloanrequestmetric.events.ApprovedLoanRequestMetricsNotificationEvent;
import co.com.crediya.model.approvedloanrequestmetric.gateways.ApprovedLoanRequestMetricsEventPublisher;
import co.com.crediya.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements ApprovedLoanRequestMetricsEventPublisher {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper objectMapper;

    public Mono<String> send(String message, String queueUrl) {
        return Mono.fromCallable(() -> buildRequest(message, queueUrl))
                .doOnNext(json -> log.info("Mapped JSON message to send: {}", json))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message, String queueUrl) {
        return SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();
    }

    @Override
    public Mono<Void> publishNotification(ApprovedLoanRequestMetricsNotificationEvent event) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(event))
                .flatMap(msg -> send(msg, properties.metricsNotificationQueueUrl()))
                .then();
    }
}
