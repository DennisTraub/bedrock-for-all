using System.Text.Json;
using System.Text.Json.Nodes;
using Amazon;
using Amazon.BedrockRuntime;
using Amazon.BedrockRuntime.Model;

namespace InvokeModel;

public abstract class Program
{
    private static async Task Main()
    {
        var region = RegionEndpoint.USEast1;

        const string modelId = "meta.llama3-8b-instruct-v1:0";

        const string prompt = "Explain 'rubber duck debugging'";
        
        using var client = new AmazonBedrockRuntimeClient(region);

        var formattedPrompt = $"""
                               <|begin_of_text|><|start_header_id|>user<|end_header_id|>
                               {prompt}
                               <|eot_id|>
                               <|start_header_id|>assistant<|end_header_id|>
                               """;
        
        var nativeRequest = JsonSerializer.Serialize(new
        {
            prompt = formattedPrompt,
            temperature = 0.5,
            max_gen_len = 500
        });

        var request = new InvokeModelRequest()
        {
            ModelId = modelId,
            Body = new MemoryStream(System.Text.Encoding.UTF8.GetBytes(nativeRequest)),
            ContentType = "application/json"
        };

        try
        {
            var response = await client.InvokeModelAsync(request);

            var modelResponse = await JsonNode.ParseAsync(response.Body);

            var responseText = modelResponse["generation"] ?? "";
            Console.WriteLine(responseText);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"\nError: {ex.Message}");
        }
    }
}