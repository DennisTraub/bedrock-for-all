import boto3

MODEL_ID = "anthropic.claude-3-haiku-20240307-v1:0"

client = boto3.client("bedrock-runtime", region_name="us-east-1")

messages = [
    {
        "role": "user", 
        "content": [{"text": "What is 'rubber duck debugging'?"}]
    }
]

response = client.converse(
    modelId=MODEL_ID,
    messages=messages
)

response_text = response["output"]["message"]["content"][0]["text"]

print(response_text)
