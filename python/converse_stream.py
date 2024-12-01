"""
Getting Started with Amazon Bedrock ConverseStream API

This example demonstrates how to use Amazon Bedrock to generate completions
using AI models like Anthropic Claude, Amazon Titan, or Meta LLama.
It shows the basic setup and usage of the Bedrock Converse API for
streaming responses.

Prerequisites:
- AWS credentials configured (via AWS CLI or environment variables)
- Appropriate permissions to access Amazon Bedrock
- Python 3.9+
- Required package: pip install boto3
"""

import boto3

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

# Configure the request
request = {
    "modelId": model_id,

    # Set up the user message
    "messages": [
        {
            # Specify the role (user/assistant)
            "role": "user",

            # Add the message content
            "content": [{"text": prompt}]
        }
    ],

    # Optional: Configure inference parameters
    "inferenceConfig": {

        # Temperature controls response randomness
        "temperature": 0.5,

        # Maximum tokens (words/characters) in the response
        "maxTokens": 500
    }
}

#----------------------
# 4. Send the Request
#----------------------

try:
    # Send the request and wait for the streaming response
    response = client.converse_stream(**request)

    # Process the streaming response
    for chunk in response["stream"]:
        # Check if the chunk contains text content
        if "contentBlockDelta" in chunk:
            # Write each piece of the response as it arrives
            text = chunk["contentBlockDelta"]["delta"]["text"]
            print(text, end="", flush=True)

except Exception as error:
    print(f"Error: {str(error)}")
    # In production, you should handle specific exceptions:
    # - AccessDeniedException: Missing access permissions
    # - ValidationException: Invalid request parameters
    # - etc.
