package co.com.crediya.sqs.listener;

import co.com.crediya.usecase.incrementapprovedloanapplicationcount.IncrementApprovedLoanApplicationCountUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final IncrementApprovedLoanApplicationCountUseCase incrementApprovedLoanApplicationCountUseCase;

    @Override
    public Mono<Void> apply(Message message) {
        return incrementApprovedLoanApplicationCountUseCase.execute(null)
                .doOnSuccess(result -> log.info("Message processed successfully: {}", result))
                .doOnError(e -> log.error("Error processing message: {}", e.getMessage()))
                .then();
    }
}
