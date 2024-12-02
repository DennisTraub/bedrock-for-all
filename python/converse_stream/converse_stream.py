import boto3

region = "us-east-1"

model_id = "meta.llama3-8b-instruct-v1:0"

prompt = "Explain 'rubber duck debugging'"

client = boto3.client("bedrock-runtime", region_name=region)

request = {
    "modelId": model_id,
    "messages": [
        {
            "role": "user",
            "content": [{"text": prompt}]
        }
    ],
    "inferenceConfig": {
        "temperature": 0.5,
        "maxTokens": 500
    }
}
try:
    response = client.converse_stream(**request)

    for chunk in response["stream"]:
        if "contentBlockDelta" in chunk:
            text = chunk["contentBlockDelta"]["delta"]["text"]
            print(text, end="", flush=True)

except Exception as error:
    print(f"Error: {str(error)}")
