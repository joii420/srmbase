package com.step.tool.utils;

import com.step.model.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class HttpUtil {

    public static <T> HttpResponse<T> sendFile(String path, File file, Map<String, String> formParams, Class<T> clazz) {
        if (file == null || !file.isFile()) {
            return HttpResponse.error("File is unSupport!");
        }
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .addPart("file", fileBody);
        if (CollectionUtil.isNotEmpty(formParams)) {
            formParams.forEach((k, v) -> {
                builder.addTextBody(k, v, ContentType.TEXT_PLAIN);
            });
        }
        HttpEntity httpEntity = builder.build();
        HttpPost post = new HttpPost(path);
        post.setEntity(httpEntity);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            System.out.println("Response Status: " + response.getStatusLine());
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseString = EntityUtils.toString(responseEntity);
                return HttpResponse.success(response.getStatusLine().getStatusCode(),responseString,clazz);
            }
            return HttpResponse.empty(response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
            return HttpResponse.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String urlString = "http://127.0.0.1:8080/camunda-flow/flow/manager/deploy-upload";
        String filePath = "D:\\test\\test1_flow1_1.bpmn";

        File file = new File(filePath);
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", fileBody)
                .addTextBody("tenantIds", "1,2,3", ContentType.TEXT_PLAIN)
                .build();

        HttpPost post = new HttpPost(urlString);
        post.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println("Response Status: " + response.getStatusLine());
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseString = EntityUtils.toString(responseEntity);
                System.out.println("Response Content: " + responseString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
