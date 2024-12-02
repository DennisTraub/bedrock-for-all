package aws.community.examples;

import org.json.JSONObject;
import org.json.JSONPointer;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;


public class InvokeModelExample {
    public static void main(String[] args) {
        Region region = Region.US_EAST_1;

        String modelId = "meta.llama3-8b-instruct-v1:0";

        String prompt = "Explain 'rubber duck debugging'";

        BedrockRuntimeClient client = BedrockRuntimeClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(region)
                .build();

        String formattedPrompt = (
                "<|begin_of_text|><|start_header_id|>user<|end_header_id|>\\n" +
                "{{prompt}} <|eot_id|>\\n" +
                "<|start_header_id|>assistant<|end_header_id|>\\n"
        ).replace("{{prompt}}", prompt);

        JSONObject requestBody = new JSONObject()
                .put("prompt", formattedPrompt)
                .put("temperature", 0.5)
                .put("max_gen_len", 500);

        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId(modelId)
                .body(SdkBytes.fromUtf8String(requestBody.toString()))
                .contentType("application/json")
                .build();

        try {
            InvokeModelResponse response = client.invokeModel(request);

            JSONObject responsePayload = new JSONObject(response.body().asUtf8String());

            String text = new JSONPointer("/generation")
                    .queryFrom(responsePayload)
                    .toString();
            System.out.println(text);

        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }
}