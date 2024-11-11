using System.Text.Json;
using System.Text.Json.Nodes;
using Amazon.BedrockRuntime;
using Amazon.BedrockRuntime.Model;

const string ModelId = "anthropic.claude-3-haiku-20240307-v1:0";

var client = new AmazonBedrockRuntimeClient(Amazon.RegionEndpoint.USEast1);

var requestBody = JsonSerializer.Serialize(new
{
    anthropic_version = "bedrock-2023-05-31",
    messages = new[]
    {
        new {
            role = "user",
            content = "What is 'rubber duck debugging'?"
        }
    },
    max_tokens = 512
});

var request = new InvokeModelRequest
{
    ModelId = ModelId,
    Body = new MemoryStream(System.Text.Encoding.UTF8.GetBytes(requestBody)),
    ContentType = "application/json"
};

var response = await client.InvokeModelAsync(request);

var responseBody = await JsonNode.ParseAsync(response.Body);

var responseText = responseBody?["content"]?[0]?["text"] ?? "";

Console.WriteLine(responseText);