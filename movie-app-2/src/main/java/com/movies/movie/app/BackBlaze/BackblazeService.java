package com.movies.movie.app.BackBlaze;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

@Service
public class BackblazeService {
    public ResponseEntity<String> getUploadUrl() throws IOException {
        String accountId = "005be9b76c3b0ee0000000001";
        String applicationKey = "K005qnOqGKFQ5dUSMF8htvkfI68DrfE";
        String bucketId = "2bae096bd7e6cc638bc00e1e";
        // Encode Account ID and Application Key
        String encodedAuth = Base64.getEncoder().encodeToString((accountId + ":" + applicationKey).getBytes());

        // Create HTTP client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Authenticate with Backblaze
        HttpGet authRequest = new HttpGet("https://api.backblazeb2.com/b2api/v2/b2_authorize_account");
        authRequest.setHeader("Authorization", "Basic " + encodedAuth);
        HttpResponse authResponse = httpClient.execute(authRequest);
        String authResponseJson = EntityUtils.toString(authResponse.getEntity());
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> authResponseMap = objectMapper.readValue(authResponseJson, HashMap.class);

        System.out.println(authResponseMap.get("apiUrl") );
        // Get upload URL
        HttpPost uploadUrlRequest = new HttpPost(authResponseMap.get("apiUrl") + "/b2api/v2/b2_get_upload_url");
        uploadUrlRequest.setHeader("Authorization", authResponseMap.get("authorizationToken"));
        StringEntity params = new StringEntity("{\"bucketId\":\"" + bucketId + "\"}");
        uploadUrlRequest.setEntity(params);
        uploadUrlRequest.setHeader("Content-Type", "application/json");
        HttpResponse uploadUrlResponse = httpClient.execute(uploadUrlRequest);
        String uploadUrlResponseJson = EntityUtils.toString(uploadUrlResponse.getEntity());

        // Close the client
        httpClient.close();

        return ResponseEntity.ok(uploadUrlResponseJson);
    }

}
