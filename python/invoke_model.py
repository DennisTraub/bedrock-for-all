import boto3
import json

MODEL_ID = "anthropic.claude-3-haiku-20240307-v1:0"

client = boto3.client("bedrock-runtime", region_name="us-east-1")

request_body = {
    "anthropic_version": "bedrock-2023-05-31",
    "messages": [
        {
            "role": "user",
            "content": [
                {
                    "type": "text",
                    "text": "What is 'rubber duck debugging'?"
                }
            ]
        }
    ],
    "max_tokens": 500,
}

response = client.invoke_model(
    modelId=MODEL_ID,
    body=json.dumps(request_body)
)

response_body = json.loads(response["body"].read())

response_text = response_body["content"][0]["text"]

print(response_text)