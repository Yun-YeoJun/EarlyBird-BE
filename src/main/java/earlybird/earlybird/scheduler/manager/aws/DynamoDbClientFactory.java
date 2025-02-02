package earlybird.earlybird.scheduler.manager.aws;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public interface DynamoDbClientFactory {
  DynamoDbClient create();
}
