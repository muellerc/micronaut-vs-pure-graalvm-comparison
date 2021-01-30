package com.aws.example.cmr;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.UUID;

public class CreateBookStreamHandler implements RequestStreamHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {

    }

    public CreateBookStreamHandler() {
        try {
            String API_GATEWAY_EVENT= "{\n" +
                    "  \"body\": \"{\\\"name\\\": \\\"test\\\"}\",\n" +
                    "  \"resource\": \"/\",\n" +
                    "  \"path\": \"/\",\n" +
                    "  \"httpMethod\": \"POST\",\n" +
                    "  \"isBase64Encoded\": false,\n" +
                    "  \"queryStringParameters\": {},\n" +
                    "  \"multiValueQueryStringParameters\": {},\n" +
                    "  \"pathParameters\": {},\n" +
                    "  \"stageVariables\": {},\n" +
                    "  \"headers\": {\n" +
                    "    \"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\",\n" +
                    "    \"Accept-Encoding\": \"gzip, deflate, sdch\",\n" +
                    "    \"Accept-Language\": \"en-US,en;q=0.8\",\n" +
                    "    \"Cache-Control\": \"max-age=0\",\n" +
                    "    \"CloudFront-Forwarded-Proto\": \"https\",\n" +
                    "    \"CloudFront-Is-Desktop-Viewer\": \"true\",\n" +
                    "    \"CloudFront-Is-Mobile-Viewer\": \"false\",\n" +
                    "    \"CloudFront-Is-SmartTV-Viewer\": \"false\",\n" +
                    "    \"CloudFront-Is-Tablet-Viewer\": \"false\",\n" +
                    "    \"CloudFront-Viewer-Country\": \"US\",\n" +
                    "    \"Host\": \"1234567890.execute-api.us-east-1.amazonaws.com\",\n" +
                    "    \"Upgrade-Insecure-Requests\": \"1\",\n" +
                    "    \"User-Agent\": \"Custom User Agent String\",\n" +
                    "    \"Via\": \"1.1 08f323deadbeefa7af34d5feb414ce27.cloudfront.net (CloudFront)\",\n" +
                    "    \"X-Amz-Cf-Id\": \"cDehVQoZnx43VYQb9j2-nvCh-9z396Uhbp027Y2JvkCPNLmGJHqlaA==\",\n" +
                    "    \"X-Forwarded-For\": \"127.0.0.1, 127.0.0.2\",\n" +
                    "    \"X-Forwarded-Port\": \"443\",\n" +
                    "    \"X-Forwarded-Proto\": \"https\"\n" +
                    "  },\n" +
                    "  \"multiValueHeaders\": {\n" +
                    "    \"Accept\": [\n" +
                    "      \"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\"\n" +
                    "    ],\n" +
                    "    \"Accept-Encoding\": [\n" +
                    "      \"gzip, deflate, sdch\"\n" +
                    "    ],\n" +
                    "    \"Accept-Language\": [\n" +
                    "      \"en-US,en;q=0.8\"\n" +
                    "    ],\n" +
                    "    \"Cache-Control\": [\n" +
                    "      \"max-age=0\"\n" +
                    "    ],\n" +
                    "    \"CloudFront-Forwarded-Proto\": [\n" +
                    "      \"https\"\n" +
                    "    ],\n" +
                    "    \"CloudFront-Is-Desktop-Viewer\": [\n" +
                    "      \"true\"\n" +
                    "    ],\n" +
                    "    \"CloudFront-Is-Mobile-Viewer\": [\n" +
                    "      \"false\"\n" +
                    "    ],\n" +
                    "    \"CloudFront-Is-SmartTV-Viewer\": [\n" +
                    "      \"false\"\n" +
                    "    ],\n" +
                    "    \"CloudFront-Is-Tablet-Viewer\": [\n" +
                    "      \"false\"\n" +
                    "    ],\n" +
                    "    \"CloudFront-Viewer-Country\": [\n" +
                    "      \"US\"\n" +
                    "    ],\n" +
                    "    \"Host\": [\n" +
                    "      \"0123456789.execute-api.us-east-1.amazonaws.com\"\n" +
                    "    ],\n" +
                    "    \"Upgrade-Insecure-Requests\": [\n" +
                    "      \"1\"\n" +
                    "    ],\n" +
                    "    \"User-Agent\": [\n" +
                    "      \"Custom User Agent String\"\n" +
                    "    ],\n" +
                    "    \"Via\": [\n" +
                    "      \"1.1 08f323deadbeefa7af34d5feb414ce27.cloudfront.net (CloudFront)\"\n" +
                    "    ],\n" +
                    "    \"X-Amz-Cf-Id\": [\n" +
                    "      \"cDehVQoZnx43VYQb9j2-nvCh-9z396Uhbp027Y2JvkCPNLmGJHqlaA==\"\n" +
                    "    ],\n" +
                    "    \"X-Forwarded-For\": [\n" +
                    "      \"127.0.0.1, 127.0.0.2\"\n" +
                    "    ],\n" +
                    "    \"X-Forwarded-Port\": [\n" +
                    "      \"443\"\n" +
                    "    ],\n" +
                    "    \"X-Forwarded-Proto\": [\n" +
                    "      \"https\"\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"requestContext\": {\n" +
                    "    \"accountId\": \"123456789012\",\n" +
                    "    \"resourceId\": \"123456\",\n" +
                    "    \"stage\": \"prod\",\n" +
                    "    \"requestId\": \"c6af9ac6-7b61-11e6-9a41-93e8deadbeef\",\n" +
                    "    \"requestTime\": \"09/Apr/2015:12:34:56 +0000\",\n" +
                    "    \"requestTimeEpoch\": 1428582896000,\n" +
                    "    \"identity\": {\n" +
                    "      \"cognitoIdentityPoolId\": null,\n" +
                    "      \"accountId\": null,\n" +
                    "      \"cognitoIdentityId\": null,\n" +
                    "      \"caller\": null,\n" +
                    "      \"accessKey\": null,\n" +
                    "      \"sourceIp\": \"127.0.0.1\",\n" +
                    "      \"cognitoAuthenticationType\": null,\n" +
                    "      \"cognitoAuthenticationProvider\": null,\n" +
                    "      \"userArn\": null,\n" +
                    "      \"userAgent\": \"Custom User Agent String\",\n" +
                    "      \"user\": null\n" +
                    "    },\n" +
                    "    \"path\": \"/prod/path/to/resource\",\n" +
                    "    \"resourcePath\": \"/{proxy+}\",\n" +
                    "    \"httpMethod\": \"POST\",\n" +
                    "    \"apiId\": \"1234567890\",\n" +
                    "    \"protocol\": \"HTTP/1.1\"\n" +
                    "  }\n" +
                    "}";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(API_GATEWAY_EVENT.getBytes("UTF-8"));

            JsonNode inputNode = objectMapper.readTree(inputStream);
            JsonNode bodyNode = objectMapper.readTree(inputNode.get("body").asText());
            Book book = objectMapper.convertValue(bodyNode, Book.class);

            BookSaved bookSaved = new BookSaved();
            bookSaved.setName(book.getName());
            bookSaved.setIsbn(UUID.randomUUID().toString());

            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("statusCode", 200);
            outputNode.set("body", objectMapper.valueToTree(bookSaved));
            objectMapper.writeValue(new ByteArrayOutputStream(), outputNode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try {
            JsonNode inputNode = objectMapper.readTree(inputStream);
            JsonNode bodyNode = objectMapper.readTree(inputNode.get("body").asText());
            Book book = objectMapper.convertValue(bodyNode, Book.class);

            BookSaved bookSaved = new BookSaved();
            bookSaved.setName(book.getName());
            bookSaved.setIsbn(UUID.randomUUID().toString());

            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("statusCode", 200);
            outputNode.set("body", objectMapper.valueToTree(bookSaved));
            objectMapper.writeValue(outputStream, outputNode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
