// Getting Started with Amazon Bedrock InvokeModel API
//
// This example demonstrates how to use Amazon Bedrock to generate completions
// using AI models like Anthropic Claude, Amazon Titan, or Meta LLama.
// It shows the basic setup and usage of the Bedrock InvokeModel API using
// the model's native request and response payloads.
//
// Prerequisites:
// - AWS credentials configured (via AWS CLI or environment variables)
// - Appropriate permissions to access Amazon Bedrock
// - Node.js installed
// - AWS SDK v3 installed: npm install @aws-sdk/client-bedrock-runtime

import { BedrockRuntimeClient, InvokeModelCommand } from "@aws-sdk/client-bedrock-runtime";

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

// Embed the prompt in Llama 3's instruction format
const formattedPrompt = `
<|begin_of_text|><|start_header_id|>user<|end_header_id|>
${prompt}
<|eot_id|>
<|start_header_id|>assistant<|end_header_id|>
`;

// Format the request using the model's native payload structure
const nativeRequest = {
    // Add the formatted prompt
    prompt: formattedPrompt,

    // Optional: Configure inference parameters
    temperature: 0.5,
    max_gen_len: 512
};

// Configure the invoke model request
const request = {
    modelId,
    body: JSON.stringify(nativeRequest),
    contentType: "application/json"
};

//----------------------
// 4. Send the Request
//----------------------

try {
    // Send the request and wait for the response
    const command = new InvokeModelCommand(request);
    const response = await client.send(command);

    // Decode the model's native response payload
    const nativeResponse = JSON.parse(new TextDecoder().decode(response.body));

    // Extract and print the response text
    const text = nativeResponse.generation;
    console.log(text);

} catch (error) {
    console.error("\nError:", error);
    // In production, you should handle specific exceptions:
    // - AccessDeniedException: Missing access permissions
    // - ValidationException: Invalid request parameters
    // - etc.
}