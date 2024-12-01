package aws.community.examples;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.*;

/**
 * Getting Started with Amazon Bedrock ConverseStream API
 * <p>
 * This example demonstrates how to use Amazon Bedrock to generate completions
 * using AI models like Anthropic Claude, Amazon Titan, or Meta LLama.
 * It shows the basic setup and usage of the Bedrock ConverseStream API for
 * streaming responses.
 * <p>
 * Prerequisites:
 * - AWS credentials configured (via AWS CLI or environment variables)
 * - Appropriate permissions to access Amazon Bedrock
 */


public class ConverseStreamExample {
    public static void main(String[] args) {
        //-------------------
        // 1. Configuration
        //-------------------

        // Specify an AWS region
        // Note: Make sure Bedrock is supported in your chosen region
        Region region = Region.US_EAST_1;

        // Choose your model ID. Supported models can be found at:
        // https://docs.aws.amazon.com/bedrock/latest/userguide/conversation-inference-supported-models-features.html
        String modelId = "meta.llama3-8b-instruct-v1:0";

        // Your prompt or question for the AI model
        String prompt = "Explain 'rubber duck debugging'";

        //-------------------
        // 2. Client Setup
        //-------------------

        // Initialize the asynchronous Bedrock Runtime Client
        // The client will use your configured AWS credentials automatically
        BedrockRuntimeAsyncClient client = BedrockRuntimeAsyncClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(region)
                .build();

        //-------------------
        // 3. Request Setup
        //-------------------

        // Configure the converse stream request
        ConverseStreamRequest request = ConverseStreamRequest.builder()

                .modelId(modelId)

                // Set up the user message
                .messages(Message.builder()

                        // Specify the role (USER/ASSISTANT)
                        .role(ConversationRole.USER)

                        // Add the message content
                        .content(ContentBlock.fromText(prompt))
                        .build())

                // Optional: Configure inference parameters
                .inferenceConfig(config -> config

                        // Temperature controls response randomness
                        .temperature(0.5F)

                        // Maximum tokens (words/characters) in the response
                        .maxTokens(500)
                )
                .build();

        //--------------------
        // 4. Stream Handler
        //--------------------

        // Create a visitor to process the individual chunks
        ConverseStreamResponseHandler.Visitor visitor = ConverseStreamResponseHandler.Visitor.builder()

                // Check if the chunk contains text content
                .onContentBlockDelta(

                        // Write each piece of the response as it arrives
                        chunk -> System.out.print(chunk.delta().text())

                ).build();

        // Configure a stream handler
        ConverseStreamResponseHandler streamHandler = ConverseStreamResponseHandler.builder()

                // Add the visitor
                .subscriber(visitor)

                // Implement error handling
                .onError(err -> System.out.println("\nError: " + err.getMessage()))

                .build();

        //----------------------
        // 4. Send the Request
        //----------------------

        try {
            // Send the request and wait for the handler to process the response
            client.converseStream(request, streamHandler).get();

        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
            // In production, you should handle specific exceptions:
            // - AccessDeniedException: Missing access permissions
            // - ValidationException: Invalid request parameters
            // - etc.
        }
    }
}