package co.com.crediya.sqs.listener;

import co.com.crediya.sqs.listener.dto.LoanApplicationApprovedEventDTO;
import co.com.crediya.usecase.incrementapprovedloanapplicationamount.IncrementApprovedLoanApplicationAmountUseCase;
import co.com.crediya.usecase.incrementapprovedloanapplicationcount.IncrementApprovedLoanApplicationCountUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final IncrementApprovedLoanApplicationAmountUseCase incrementApprovedLoanApplicationAmountUseCase;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> apply(Message message) {
        return Mono.fromCallable(() -> objectMapper.readValue(message.body(), LoanApplicationApprovedEventDTO.class))
                .doOnError(e -> log.error("Error converting message to input: {}", e.getMessage()))
                .flatMap(event ->
                        incrementApprovedLoanApplicationAmountUseCase.execute(event.amount())
                                .then(incrementApprovedLoanApplicationCountUseCase.execute(null))
                )
                .doOnSuccess(result -> log.info("Message processed successfully: {}", result))
                .then();
    }
}
