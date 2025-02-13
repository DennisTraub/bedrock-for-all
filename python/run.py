# /// script
# requires-python = ">=3.12"
# dependencies = [
#     "boto3",
#     "click",
# ]
# ///

import boto3
import click

@click.command()
@click.argument('prompt')
@click.option('--region', default='us-east-1', help='AWS region')
@click.option('--model-id', default="anthropic.claude-3-haiku-20240307-v1:0", help='Model ID')
@click.option('--temperature', default=0.5, help='Temperature for inference')
@click.option('--max-tokens', default=500, help='Maximum tokens in response')
def invoke(prompt, region, model_id, temperature, max_tokens):
    """Send a prompt to Amazon Bedrock and get a response."""
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
            "temperature": temperature,
            "maxTokens": max_tokens
        }
    }

    try:
        response = client.converse(**request)
        text = response["output"]["message"]["content"][0]["text"]
        print(text)

    except Exception as error:
        print(f"Error: {str(error)}")

if __name__ == "__main__":
    invoke()
