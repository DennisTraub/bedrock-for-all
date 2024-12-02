using Amazon;
using Amazon.BedrockRuntime;
using Amazon.BedrockRuntime.Model;

namespace ConverseStream;

public abstract class Program
{
    private static async Task Main()
    {
        var region = RegionEndpoint.USEast1;

        const string modelId = "meta.llama3-8b-instruct-v1:0";

        const string prompt = "Explain 'rubber duck debugging'";

        using var client = new AmazonBedrockRuntimeClient(region);

        var request = new ConverseStreamRequest
        {
            ModelId = modelId,
            Messages =
            [
                new Message
                {
                    Role = ConversationRole.User,
                    Content = [new ContentBlock { Text = prompt }]
                }
            ],
            InferenceConfig = new InferenceConfiguration
            {
                Temperature = 0.5F,
                MaxTokens = 500
            },
        };

        try
        {
            var response = await client.ConverseStreamAsync(request);

            foreach (var chunk in response.Stream.AsEnumerable())
            {
                if (chunk is ContentBlockDeltaEvent textChunk)
                {
                    Console.Write(textChunk.Delta.Text);
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"\nError: {ex.Message}");
        }
    }
}