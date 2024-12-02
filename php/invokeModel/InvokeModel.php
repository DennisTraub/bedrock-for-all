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

$formattedPrompt = "
<|begin_of_text|><|start_header_id|>user<|end_header_id|>
$prompt
<|eot_id|>
<|start_header_id|>assistant<|end_header_id|>
";

$nativeRequest = [
    'prompt' => $formattedPrompt,
    'temperature' => 0.5,
    'max_gen_len' => 512
];

$request = [
    'modelId' => $modelId,
    'body' => json_encode($nativeRequest),
    'contentType' => 'application/json'
];

try {
    $response = $client->invokeModel($request);

    $nativeResponse = json_decode($response->get('body'));

    $text = $nativeResponse->generation;
    echo $text;

} catch (Exception $e) {
    echo "Error: " . $e->getMessage() . "\n";
}