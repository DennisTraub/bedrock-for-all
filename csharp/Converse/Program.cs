using System.Collections.Generic;
using System.Text.Json;
using System.Text.Json.Nodes;
using Amazon.BedrockRuntime;
using Amazon.BedrockRuntime.Model;

const string ModelId = "anthropic.claude-3-haiku-20240307-v1:0";

var client = new AmazonBedrockRuntimeClient(Amazon.RegionEndpoint.USEast1);

var request = new ConverseRequest
{
    ModelId = ModelId,
    Messages = new List<Message>
    {
        new Message
        {
            Role = ConversationRole.User,
            Content = new List<ContentBlock> {
                new ContentBlock { Text = "What is 'rubber duck debugging'?" }
            }
        }
    }
};

var response = await client.ConverseAsync(request);

var responseText = response?.Output?.Message?.Content?[0]?.Text ?? "";

Console.WriteLine(responseText);