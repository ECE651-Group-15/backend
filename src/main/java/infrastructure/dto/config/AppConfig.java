package infrastructure.dto.config;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

public class AppConfig {
    public static String getSecret(String secretName) {
        try (SecretsManagerClient client = SecretsManagerClient.builder().region(Region.of("us-east-2")).build()) {
            GetSecretValueRequest valueRequest = GetSecretValueRequest.builder().secretId(secretName).build();
            return client.getSecretValue(valueRequest).secretString();
        }
    }
}
