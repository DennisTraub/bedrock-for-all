import boto3
import json

region = "us-east-1"

model_id = "meta.llama3-8b-instruct-v1:0"

prompt = "Explain 'rubber duck debugging'"

client = boto3.client("bedrock-runtime", region_name=region)

formatted_prompt = f"""
<|begin_of_text|><|start_header_id|>user<|end_header_id|>
{prompt}
<|eot_id|>
<|start_header_id|>assistant<|end_header_id|>
"""

native_request = {
    "prompt": formatted_prompt,
    # Optional: Configure inference parameters
    "temperature": 0.5,
    "max_gen_len": 512
}

request = {
    "modelId": model_id,
    "body": json.dumps(native_request),
    "contentType":"application/json"
}

try:
    response = client.invoke_model(**request)

    native_response = json.loads(response["body"].read())

    text = native_response["generation"]
    print(text)

except Exception as error:
    print(f"Error: {str(error)}")
