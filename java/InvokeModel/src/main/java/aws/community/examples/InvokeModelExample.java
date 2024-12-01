package aws.community.examples;

import org.json.JSONObject;
import org.json.JSONPointer;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

/**
 * Getting Started with Amazon Bedrock InvokeModel API
 * <p>
 * This example demonstrates how to use Amazon Bedrock to generate completions
 * using AI models like Anthropic Claude, Amazon Titan, or Meta LLama.
 * It shows the basic setup and usage of the Bedrock InvokeModel API using
 * the model's native request and response payloads.
 * <p>
 * Prerequisites:
 * - AWS credentials configured (via AWS CLI or environment variables)
 * - Appropriate permissions to access Amazon Bedrock
 */


public class InvokeModelExample {
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

        // Initialize the Bedrock Runtime Client
        // The client will use your configured AWS credentials automatically
        BedrockRuntimeClient client = BedrockRuntimeClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(region)
                .build();

        //-------------------
        // 3. Request Setup
        //-------------------

        // Embed the prompt in Llama 3's instruction format
        String formattedPrompt = (
                "<|begin_of_text|><|start_header_id|>user<|end_header_id|>\\n" +
                "{{prompt}} <|eot_id|>\\n" +
                "<|start_header_id|>assistant<|end_header_id|>\\n"
        ).replace("{{prompt}}", prompt);

        // Format request using the model's native payload structure.
        JSONObject requestBody = new JSONObject()

                // Add the formatted prompt
                .put("prompt", formattedPrompt)

                // Optional: Configure inference parameters
                .put("temperature", 0.5)
                .put("max_gen_len", 500);

        // Configure the invoke model request
        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId(modelId)
                .body(SdkBytes.fromUtf8String(requestBody.toString()))
                .contentType("application/json")
                .build();

        //----------------------
        // 4. Send the Request
        //----------------------

        try {
            // Send the request and get the response
            InvokeModelResponse response = client.invokeModel(request);

            // Decode the model's native response payload
            JSONObject responsePayload = new JSONObject(response.body().asUtf8String());

            // Extract and print the response text
            String text = new JSONPointer("/generation")
                    .queryFrom(responsePayload)
                    .toString();
            System.out.println(text);

        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
            // In production, you should handle specific exceptions:
            // - AccessDeniedException: Missing access permissions
            // - ValidationException: Invalid request parameters
            // - etc.
        }
    }
}