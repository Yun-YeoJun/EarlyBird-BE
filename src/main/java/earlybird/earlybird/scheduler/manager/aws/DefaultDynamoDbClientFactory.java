package earlybird.earlybird.scheduler.manager.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Service
public class DefaultDynamoDbClientFactory implements DynamoDbClientFactory {
    private final AwsBasicCredentials credentials;

    public DefaultDynamoDbClientFactory(
            @Value("${aws.access-key}") String awsAccessKey,
            @Value("${aws.secret-access-key}") String awsSecretAccessKey
    ) {
        credentials = AwsBasicCredentials.create(awsAccessKey, awsSecretAccessKey);
    }

    @Override
    @Transactional
    public DynamoDbClient create() {
        return DynamoDbClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(this.credentials))
                .build();
    }
}
