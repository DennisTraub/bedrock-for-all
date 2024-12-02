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
    $result = $client->converseStream($request);

    foreach ($result['stream'] as $chunk) {
        if (isset($chunk['contentBlockDelta'])) {
            $text = $chunk['contentBlockDelta']['delta']['text'];
            echo $text;

            if (ob_get_level() > 0) {
                ob_flush();
            }
            flush();
        }
    }

} catch (Exception $e) {
    echo "Error: " . $e->getMessage() . "\n";
}