<?php

require 'vendor/autoload.php';

use Aws\BedrockRuntime\BedrockRuntimeClient;

$region = "us-east-1";

$modelId = "meta.llama3-8b-instruct-v1:0";

$prompt = "Explain 'rubber duck debugging'";

$client = new BedrockRuntimeClient([
    'profile' => 'default',
    'version' => 'latest',
    'region'  => $region
]);

$request = [
    'modelId' => $modelId,
    'messages' => [
        [
            'role' => 'user',
            'content' => [['text' => $prompt]]
        ]
    ],
    'inferenceConfig' => [
        'temperature' => 0.5,
        'maxTokens' => 500
    ]
];
try {
    $response = $client->converse($request);

    $result = $response->get('output');
    $text = $result['message']['content'][0]['text'];
    echo $text;

} catch (Exception $e) {
    echo "Error: " . $e->getMessage() . "\n";
}