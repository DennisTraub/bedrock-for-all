using System.Text.Json;
using System.Text.Json.Nodes;
using Amazon;
using Amazon.BedrockRuntime;
using Amazon.BedrockRuntime.Model;

namespace InvokeModel;

/// <summary>
/// Getting Started with Amazon Bedrock InvokeModel API
/// 
/// This example demonstrates how to use Amazon Bedrock to generate completions
/// using AI models like Anthropic Claude, Amazon Titan, or Meta LLama.
/// It shows the basic setup and usage of the Bedrock InvokeModel API using
/// the model's native request and response payloads.
/// 
/// Prerequisites:
/// - AWS credentials configured (via AWS CLI or environment variables)
/// - Appropriate permissions to access Amazon Bedrock
/// </summary>
public abstract class Program
{
    private static async Task Main()
    {
        //-------------------
        // 1. Configuration
        //-------------------

        // Specify an AWS region
        // Note: Make sure Bedrock is supported in your chosen region
        var region = RegionEndpoint.USEast1;

        // Choose your model ID. Supported models can be found at:
        // https://docs.aws.amazon.com/bedrock/latest/userguide/conversation-inference-supported-models-features.html
        const string modelId = "meta.llama3-8b-instruct-v1:0";

        // Your prompt or question for the AI model
        const string prompt = "Explain 'rubber duck debugging'";
        
        //-------------------
        // 2. Client Setup
        //-------------------

        // Initialize the Bedrock Runtime Client
        // The client will use your configured AWS credentials automatically
        using var client = new AmazonBedrockRuntimeClient(region);

        //-------------------
        // 3. Request Setup
        //-------------------
        
        // Embed the prompt in Llama 3's instruction format
        var formattedPrompt = $"""
                               <|begin_of_text|><|start_header_id|>user<|end_header_id|>
                               {prompt}
                               <|eot_id|>
                               <|start_header_id|>assistant<|end_header_id|>
                               """;
        
        // Format the request using the model's native payload structure
        var nativeRequest = JsonSerializer.Serialize(new
        {
            // Add the formatted prompt
            prompt = formattedPrompt,
            
            // Optional: Configure inference parameters
            temperature = 0.5,
            max_gen_len = 500
        });

        // Configure the invoke model request
        var request = new InvokeModelRequest()
        {
            ModelId = modelId,
            Body = new MemoryStream(System.Text.Encoding.UTF8.GetBytes(nativeRequest)),
            ContentType = "application/json"
        };

        //----------------------
        // 4. Send the Request
        //----------------------

        try
        {
            // Send the request and wait for the response
            var response = await client.InvokeModelAsync(request);
            
            // Decode the model's native response payload
            var modelResponse = await JsonNode.ParseAsync(response.Body);

            // Extract and print the response text
            var responseText = modelResponse["generation"] ?? "";
            Console.WriteLine(responseText);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"\nError: {ex.Message}");
            // In production, you should handle specific exceptions:
            // - AccessDeniedException: Missing access permissions
            // - ValidationException: Invalid request parameters
            // - etc.
        }
    }
}