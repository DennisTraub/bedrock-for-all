"""
Getting Started with Amazon Bedrock InvokeModel API

This example demonstrates how to use Amazon Bedrock to generate completions
using AI models like Anthropic Claude, Amazon Titan, or Meta LLama.
It shows the basic setup and usage of the Bedrock InvokeModel API using
the model's native request and response payloads.

Prerequisites:
- AWS credentials configured (via AWS CLI or environment variables)
- Appropriate permissions to access Amazon Bedrock
- Required package: pip install boto3
"""

import boto3
import json

#-------------------
# 1. Configuration
#-------------------

# Specify an AWS region
# Note: Make sure Bedrock is supported in your chosen region
region = "us-east-1"

# Choose your model ID. Supported models can be found at:
# https://docs.aws.amazon.com/bedrock/latest/userguide/conversation-inference-supported-models-features.html
model_id = "meta.llama3-8b-instruct-v1:0"

# Your prompt or question for the AI model
prompt = "Explain 'rubber duck debugging'"

#-------------------
# 2. Client Setup
#-------------------

# Initialize the Bedrock Runtime Client
# The client will use your configured AWS credentials automatically
client = boto3.client("bedrock-runtime", region_name=region)

#-------------------
# 3. Request Setup
#-------------------

# Embed the prompt in Llama 3's instruction format
formatted_prompt = f"""
<|begin_of_text|><|start_header_id|>user<|end_header_id|>
{prompt}
<|eot_id|>
<|start_header_id|>assistant<|end_header_id|>
"""

# Format the request using the model's native payload structure
native_request = {
    # Add the formatted prompt
    "prompt": formatted_prompt,

    # Optional: Configure inference parameters
    "temperature": 0.5,
    "max_gen_len": 512
}

# Configure the invoke model request
request = {
    "modelId": model_id,
    "body": json.dumps(native_request),
    "contentType":"application/json"
}

#----------------------
# 4. Send the Request
#----------------------

try:
    # Send the request and wait for the response
    response = client.invoke_model(**request)

    # Decode the model's native response payload
    native_response = json.loads(response["body"].read())

    # Extract and print the response text
    text = native_response["generation"]
    print(text)

except Exception as error:
    print(f"Error: {str(error)}")
    # In production, you should handle specific exceptions:
    # - AccessDeniedException: Missing access permissions
    # - ValidationException: Invalid request parameters
    # - etc.
