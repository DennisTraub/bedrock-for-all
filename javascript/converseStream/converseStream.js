import { BedrockRuntimeClient, ConverseStreamCommand } from "@aws-sdk/client-bedrock-runtime";

const region = "us-east-1";

const modelId = "meta.llama3-8b-instruct-v1:0";

const prompt = "Explain 'rubber duck debugging'";

const client = new BedrockRuntimeClient({ region });

const request = {
    modelId,
    messages: [
        {
            role: "user",
            content: [{ text: prompt }]
        }
    ],
    inferenceConfiguration: {
    temperature: 0.5,
        maxTokens: 500,
    }
};

try {
    const command = new ConverseStreamCommand(request);
    const response = await client.send(command);

    for await (const chunk of response.stream) {
        if (chunk.contentBlockDelta) {
            process.stdout.write(chunk.contentBlockDelta.delta?.text);
        }
    }
} catch (error) {
    console.error("\nError:", error);
}