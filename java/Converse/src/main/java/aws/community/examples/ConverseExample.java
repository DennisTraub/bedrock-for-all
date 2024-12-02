package aws.community.examples;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.*;

public class ConverseExample {
    public static void main(String[] args) {
        Region region = Region.US_EAST_1;

        String modelId = "meta.llama3-8b-instruct-v1:0";

        String prompt = "Explain 'rubber duck debugging'";

        BedrockRuntimeClient client = BedrockRuntimeClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(region)
                .build();

        ConverseRequest request = ConverseRequest.builder()
                .modelId(modelId)
                .messages(Message.builder()
                        .role(ConversationRole.USER)
                        .content(ContentBlock.fromText(prompt))
                        .build())
                .inferenceConfig(config -> config
                        .temperature(0.5F)
                        .maxTokens(500)
                ).build();

        try {
            ConverseResponse response = client.converse(request);

            String text = response.output().message().content().getFirst().text();
            System.out.println(text);

        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }
}