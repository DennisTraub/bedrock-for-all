package aws.community.examples;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.*;


public class ConverseStreamExample {
    public static void main(String[] args) {
        Region region = Region.US_EAST_1;

        String modelId = "meta.llama3-8b-instruct-v1:0";

        String prompt = "Explain 'rubber duck debugging'";

        BedrockRuntimeAsyncClient client = BedrockRuntimeAsyncClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(region)
                .build();

        ConverseStreamRequest request = ConverseStreamRequest.builder()
                .modelId(modelId)
                .messages(Message.builder()
                        .role(ConversationRole.USER)
                        .content(ContentBlock.fromText(prompt))
                        .build())
                .inferenceConfig(config -> config
                        .temperature(0.5F)
                        .maxTokens(500)
                ).build();

        ConverseStreamResponseHandler.Visitor visitor = ConverseStreamResponseHandler.Visitor.builder()
                .onContentBlockDelta(
                        chunk -> System.out.print(chunk.delta().text())
                ).build();

        ConverseStreamResponseHandler streamHandler = ConverseStreamResponseHandler.builder()
                .subscriber(visitor)
                .onError(err -> System.out.println("\nError: " + err.getMessage()))
                .build();

        try {
            client.converseStream(request, streamHandler).get();
        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }
}