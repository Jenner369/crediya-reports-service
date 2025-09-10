package co.com.crediya.dynamodb.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

/* Enhanced DynamoDB annotations are incompatible with Lombok #1932
         https://github.com/aws/aws-sdk-java-v2/issues/1932*/
@SuppressWarnings("LombokSetterMayBeUsed")
@DynamoDbBean
public class ApprovedLoanRequestMetricEntity {

    private String code;
    private String value;

    public ApprovedLoanRequestMetricEntity() {
    }

    public ApprovedLoanRequestMetricEntity(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @DynamoDbAttribute("value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
