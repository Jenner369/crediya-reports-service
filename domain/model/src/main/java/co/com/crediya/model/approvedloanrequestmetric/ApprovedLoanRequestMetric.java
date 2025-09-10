package co.com.crediya.model.approvedloanrequestmetric;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApprovedLoanRequestMetric {
    private String code;
    private String value;
}
