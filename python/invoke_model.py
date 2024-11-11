import boto3
import json

client = boto3.client("bedrock-runtime", region_name="us-east-1")

request_body = {
    "anthropic_version": "bedrock-2023-05-31",
    "messages": [
        {
            "role": "user",
            "content": [
                {
                "type": "text",
                "text": "What is the capital of France?"
                }
            ]
        }
    ]
}

response = client.invoke_model(
    modelId="anthropic.claude-3-5-haiku-20241022-v1:0",
    body=json.dumps(request_body)
)

response_text = json.loads(response["body"].read())["content"][0]["text"]

print(response_text)