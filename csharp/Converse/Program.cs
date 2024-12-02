using Amazon;
using Amazon.BedrockRuntime;
using Amazon.BedrockRuntime.Model;

namespace Converse;

public abstract class Program
{
    private static async Task Main()
    {
        var region = RegionEndpoint.USEast1;

        const string modelId = "meta.llama3-8b-instruct-v1:0";

        const string prompt = "Explain 'rubber duck debugging'";

        using var client = new AmazonBedrockRuntimeClient(region);

        var request = new ConverseRequest
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
            var response = await client.ConverseAsync(request);

            var responseText = response.Output?.Message?.Content?[0]?.Text ?? "";
            Console.WriteLine(responseText);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"\nError: {ex.Message}");
        }
    }
}