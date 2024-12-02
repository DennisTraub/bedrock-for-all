# Amazon Bedrock for All

Educational examples of using Amazon Bedrock across multiple programming languages.

## About

This repository contains code samples demonstrating how to use Amazon Bedrock with various programming languages. It's designed to show that generative AI is accessible to all developers, regardless of their preferred programming language.

Each example is thoroughly documented with inline comments explaining the code's functionality, making it easy to understand and adapt to your own projects.

## Prerequisites

Before running any of the examples, you'll need:

- An AWS account with access to Amazon Bedrock
- AWS credentials configured (via AWS CLI or environment variables)
- The appropriate AWS SDK for your chosen programming language
- Basic familiarity with your chosen programming language

## Languages

- [Python](./python) - Using boto3
- [Java](./java) - Using the AWS SDK for Java v2
- [JavaScript](./javascript) - Using AWS SDK for JavaScript v3
- [C#](./csharp) - Using the AWS SDK for .NET
- [PHP](./php) - Using the AWS SDK for PHP

## Examples
Each language directory contains three core examples:

**1. InvokeModel API**

- Direct model invocation using native model payloads
- Demonstrates low-level API interaction
- Useful for fine-grained control over model parameters

**2. Converse API**

- A typed and unified API that works across different model providers
- Simplified request/response structure
- Ideal for production applications

**3. ConverseStream API**

- Streaming version of the Converse API
- Provides real-time response generation
- Perfect for interactive applications

Each example includes:

- Complete, working code
- Detailed comments explaining each section
- Configuration options

## Getting Started

1. Clone this repository
2. Navigate to your preferred language directory
3. Install the required dependencies
4. Configure your AWS credentials
5. Run the examples

## Usage

This repository is for educational purposes only. The code samples are designed to be:

- Easy to understand
- Ready to run
- Simple to modify
- Adaptable for your own projects

## Contributing

As an educational repository, it does not accept Pull Requests or Issues. For the latest information on Amazon Bedrock, please refer to the [official documentation](https://aws.amazon.com/developer/generative-ai/bedrock/?trk=2483aad2-15a6-4b7a-a1c5-189851586b67&sc_channel=el).

## Additional Resources

- [Amazon Bedrock Documentation](https://aws.amazon.com/developer/generative-ai/bedrock/?trk=2483aad2-15a6-4b7a-a1c5-189851586b67&sc_channel=el)
- [AWS SDK Documentation](https://aws.amazon.com/developer/tools/?trk=2483aad2-15a6-4b7a-a1c5-189851586b67&sc_channel=el)
- [AWS Free Tier](https://aws.amazon.com/free/?trk=2483aad2-15a6-4b7a-a1c5-189851586b67&sc_channel=el)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
