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

#----------------------
# 4. Send the Request
#----------------------

try:
    # Send the request and wait for the response
    response = client.converse(**request)

    # Extract and display the response text
    text = response["output"]["message"]["content"][0]["text"]
    print(text)

except Exception as error:
    print(f"Error: {str(error)}")
    # In production, you should handle specific exceptions:
    # - AccessDeniedException: Missing access permissions
    # - ValidationException: Invalid request parameters
    # - etc.
