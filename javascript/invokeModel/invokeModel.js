import { BedrockRuntimeClient, InvokeModelCommand } from "@aws-sdk/client-bedrock-runtime";

const region = "us-east-1";

const modelId = "meta.llama3-8b-instruct-v1:0";

const prompt = "Explain 'rubber duck debugging'";

const client = new BedrockRuntimeClient({ region });

const formattedPrompt = `
<|begin_of_text|><|start_header_id|>user<|end_header_id|>
${prompt}
<|eot_id|>
<|start_header_id|>assistant<|end_header_id|>
`;

const nativeRequest = {
    prompt: formattedPrompt,
    temperature: 0.5,
    max_gen_len: 512
};

const request = {
    modelId,
    body: JSON.stringify(nativeRequest),
    contentType: "application/json"
};

try {
    const command = new InvokeModelCommand(request);
    const response = await client.send(command);

    const nativeResponse = JSON.parse(new TextDecoder().decode(response.body));

    const text = nativeResponse.generation;
    console.log(text);
} catch (error) {
    console.error("\nError:", error);
}