using Amazon;
using Amazon.BedrockRuntime;
using Amazon.BedrockRuntime.Model;

namespace ConverseStream;

/// <summary>
/// Getting Started with Amazon Bedrock ConverseStream API
/// 
/// This example demonstrates how to use Amazon Bedrock to generate completions
/// using AI models like Anthropic Claude, Amazon Titan, or Meta LLama.
/// It shows the basic setup and usage of the Bedrock ConverseStream API for
/// streaming responses.
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

        // Configure the converse stream request
        var request = new ConverseStreamRequest
        {
            ModelId = modelId,

            // Set up the user message
            Messages =
            [
                new Message
                {
                    // Specify the role (User/Assistant)
                    Role = ConversationRole.User,

                    // Add the message content
                    Content = [new ContentBlock { Text = prompt }]
                }
            ],
            
            // Optional: Configure inference parameters
            InferenceConfig = new InferenceConfiguration
            {
                // Temperature controls response randomness
                Temperature = 0.5F,

                // Maximum tokens (words/characters) in the response
                MaxTokens = 500
            },
        };

        //----------------------
        // 4. Send the Request
        //----------------------

        try
        {
            // Send the request and get the streaming response
            var response = await client.ConverseStreamAsync(request);

            // Process the streaming response
            foreach (var chunk in response.Stream.AsEnumerable())
            {
                // Check if the chunk contains text content
                if (chunk is ContentBlockDeltaEvent textChunk)
                {
                    // Write each piece of the response as it arrives
                    Console.Write(textChunk.Delta.Text);
                }
            }
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