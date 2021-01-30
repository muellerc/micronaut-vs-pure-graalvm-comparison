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
