// Getting Started with Amazon Bedrock ConverseStream API
//
// This example demonstrates how to use Amazon Bedrock to generate completions
// using AI models like Anthropic Claude, Amazon Titan, or Meta LLama.
// It shows the basic setup and usage of the Bedrock Converse API for
// streaming responses.
//
// Prerequisites:
// - AWS credentials configured (via AWS CLI or environment variables)
// - Appropriate permissions to access Amazon Bedrock
// - Node.js installed
// - AWS SDK v3 installed: npm install @aws-sdk/client-bedrock-runtime

import { BedrockRuntimeClient, ConverseStreamCommand } from "@aws-sdk/client-bedrock-runtime";

//-------------------
// 1. Configuration
//-------------------

// Specify an AWS region
// Note: Make sure Bedrock is supported in your chosen region
const region = "us-east-1";

// Choose your model ID. Supported models can be found at:
// https://docs.aws.amazon.com/bedrock/latest/userguide/conversation-inference-supported-models-features.html
const modelId = "meta.llama3-8b-instruct-v1:0";

// Your prompt or question for the AI model
const prompt = "Explain 'rubber duck debugging'";

//-------------------
// 2. Client Setup
//-------------------

// Initialize the Bedrock Runtime Client
// The client will use your configured AWS credentials automatically
const client = new BedrockRuntimeClient({ region });

//-------------------
// 3. Request Setup
//-------------------

// Configure the request
const request = {

    modelId,

    // Set up the user message
    messages: [
        {
            // Specify the role (user/assistant)
            role: "user",

            // Add the message content
            content: [{ text: prompt }]
        }
    ],

    // Optional: Configure inference parameters
    inferenceConfiguration: {

        // Temperature controls response randomness
        temperature: 0.5,

        // Maximum tokens (words/characters) in the response
        maxTokens: 500,
    }
};

//----------------------
// 4. Send the Request
//----------------------

try {
    // Send the request and wait for the streaming response
    const command = new ConverseStreamCommand(request);
    const response = await client.send(command);

    // Process the streaming response
    for await (const chunk of response.stream) {
        // Check if the chunk contains text content
        if (chunk.contentBlockDelta) {
            // Write each piece of the response as it arrives
            process.stdout.write(chunk.contentBlockDelta.delta?.text);
        }
    }

} catch (error) {
    console.error("\nError:", error);
    // In production, you should handle specific exceptions:
    // - AccessDeniedException: Missing access permissions
    // - ValidationException: Invalid request parameters
    // - etc.
}