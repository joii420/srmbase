package com.step.tool.utils;

import com.step.api.runtime.exception.base.BaseException;
import com.step.logger.LOGGER;
import com.step.model.Response;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import org.apache.http.HttpStatus;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestUtil {


    public static String sendGet(String address, Map<String, Object> params) {
        Map<String, String> headers = new HashMap<>(0);
        return sendGet(address, headers, params);
    }

    public static Response sendGetResponse(String address, Map<String, Object> params) {
        Map<String, String> headers = new HashMap<>(0);
        return sendGetResponse(address, headers, params);
    }

    public static <T> T sendGet(String address, Map<String, Object> params, Class<T> responseEntity) {
        String send = sendGet(address, params);
        return JsonUtil.parse(send, responseEntity);
    }

    public static Response sendGetResponse(String address, Map<String, String> headers, Map<String, Object> params) {
        headers = Optional.ofNullable(headers).orElse(new HashMap<>());
        if (CollectionUtil.isNotEmpty(params)) {
            {
                StringBuffer paramBuilder = new StringBuffer();
                params.forEach((k, v) -> {
                    if (v != null) {
                        paramBuilder.append("&").append(k).append("=").append(URLEncoder.encode(String.valueOf(v), StandardCharsets.UTF_8));
                    }
                });
                address += paramBuilder.toString().replaceFirst("&", "?");
            }
        }
        return send(address, HttpMethod.GET, headers, "");
    }

    public static String sendGet(String address, Map<String, String> headers, Map<String, Object> params) {
        Response response = sendGetResponse(address, headers, params);
        if (response.getResponseCode() != 200) {
            throw new BaseException("Request failed:" + response.getPath()).record();
        }
        return response.getData();
    }

    public static <T> T sendGet(String address, Map<String, String> headers, Map<String, Object> params, Class<T> responseEntity) {
        String send = sendGet(address, headers, params);
        return JsonUtil.parse(send, responseEntity);
    }


    public static String sendGet(String address, Map<String, String> headers, Map<String, String> cookies, Map<String, Object> params) {
        if (cookies != null) {
            headers = Optional.ofNullable(headers).orElse(new HashMap<>());
            StringBuilder cookieBuilder = new StringBuilder();
            cookies.forEach((k, v) -> cookieBuilder.append(k).append("=").append(v).append("; "));
            headers.put("Cookie", cookieBuilder.toString());
        }
        return sendGet(address, headers, params);
    }


    public static Response sendGetResponse(String address, Map<String, String> headers, Map<String, String> cookies, Map<String, Object> params) {
        if (cookies != null) {
            headers = Optional.ofNullable(headers).orElse(new HashMap<>());
            StringBuilder cookieBuilder = new StringBuilder();
            cookies.forEach((k, v) -> cookieBuilder.append(k).append("=").append(v).append("; "));
            headers.put("Cookie", cookieBuilder.toString());
        }
        return sendGetResponse(address, headers, params);
    }

    public static <T> T sendGet(String address, Map<String, String> headers, Map<String, String> cookies, Map<String, Object> params, Class<T> responseEntity) {
        String send = sendGet(address, headers, cookies, params);
        return JsonUtil.parse(send, responseEntity);
    }

    public static String sendPost(String address, Map<String, Object> body) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=UTF-8");
        headers.put("User-Agent", "Mozilla/5.0");
        return sendPost(address, headers, body);
    }

    public static String sendPost(String address, String body) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=UTF-8");
        headers.put("User-Agent", "Mozilla/5.0");
        return sendPost(address, headers, body);
    }

    public static Response sendPostResponse(String address, Map<String, Object> body) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=UTF-8");
        headers.put("User-Agent", "Mozilla/5.0");
        return sendPostResponse(address, headers, body);
    }


    public static <T> T sendPost(String address, Map<String, Object> body, Class<T> responseEntity) {
        String send = sendPost(address, body);
        return JsonUtil.parse(send, responseEntity);
    }

    public static <T> T sendPost(String address, String body, Class<T> responseEntity) {
        String send = sendPost(address, body);
        return JsonUtil.parse(send, responseEntity);
    }

    public static String sendPost(String address, Map<String, String> headers, Map<String, Object> body) {
        Response response = sendPostResponse(address, headers, body);
        if (response.getResponseCode() != 200) {
            throw new BaseException("Request failed:" + response.getPath()).record();
        }
        return response.getData();
    }

    public static String sendPost(String address, Map<String, String> headers, String body) {
        Response response = sendPostResponse(address, headers, body);
        if (response.getResponseCode() != 200) {
            throw new BaseException("Request failed:" + response.getPath()).record();
        }
        return response.getData();
    }

    public static Response sendPostResponse(String address, Map<String, String> headers, Map<String, Object> body) {
        headers = Optional.ofNullable(headers).orElse(new HashMap<>());
        return send(address, HttpMethod.POST, headers, body);
    }

    public static Response sendPostResponse(String address, Map<String, String> headers, String body) {
        headers = Optional.ofNullable(headers).orElse(new HashMap<>());
        return send(address, HttpMethod.POST, headers, body);
    }

    public static <T> T sendPost(String address, Map<String, String> headers, Map<String, Object> body, Class<T> responseEntity) {
        String send = sendPost(address, headers, body);
        return JsonUtil.parse(send, responseEntity);
    }


    public static String sendPost(String address, Map<String, String> headers, Map<String, String> cookies, Map<String, Object> body) {
        if (cookies != null) {
            headers = Optional.ofNullable(headers).orElse(new HashMap<>());
            StringBuilder cookieBuilder = new StringBuilder();
            cookies.forEach((k, v) -> cookieBuilder.append(k).append("=").append(v).append("; "));
            headers.put("Cookie", cookieBuilder.toString());
        }
        return sendPost(address, headers, body);
    }

    public static Response sendPostResponse(String address, Map<String, String> headers, Map<String, String> cookies, Map<String, Object> body) {
        if (cookies != null) {
            headers = Optional.ofNullable(headers).orElse(new HashMap<>());
            StringBuilder cookieBuilder = new StringBuilder();
            cookies.forEach((k, v) -> cookieBuilder.append(k).append("=").append(v).append("; "));
            headers.put("Cookie", cookieBuilder.toString());
        }
        return sendPostResponse(address, headers, body);
    }

    public static <T> T sendPost(String address, Map<String, String> headers, Map<String, String> cookies, Map<String, Object> body, Class<T> responseEntity) {
        String send = sendPost(address, headers, cookies, body);
        return JsonUtil.parse(send, responseEntity);
    }

    private static Response send(String address, HttpMethod httpMethod, Map<String, String> requestProperty, String bodyJson) {
        Response result = new Response();
        result.setPath(address);
        result.setResponseCode(400);
        result.setSuccess(true);
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(httpMethod.name());
            conn.setConnectTimeout(60);
            if (CollectionUtil.isNotEmpty(requestProperty)) {
                requestProperty.forEach(conn::setRequestProperty);
            }
            if (httpMethod == HttpMethod.POST && StringUtil.isNotEmpty(bodyJson)) {
                conn.setDoOutput(true);
                try (OutputStream outputStream = conn.getOutputStream()) {
                    outputStream.write(bodyJson.getBytes(StandardCharsets.UTF_8));
                }
            }
            // 发送请求并获取响应
            int responseCode = conn.getResponseCode();
            InputStream inputStream;
            if (responseCode == HttpStatus.SC_OK) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }
            result.setResponseCode(responseCode);
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                result.setData(response.toString());
            }
            return result;
        } catch (Exception exception) {
            LOGGER.error("Failed to request [ " + address + " ] : " + exception);
            result.setSuccess(false);
            result.setError(exception.getMessage());
            return result;
        }
    }

    private static Response send(String address, HttpMethod httpMethod, Map<String, String> requestProperty, Map<String, Object> bodys) {
        String bodyJson = JsonUtil.format(bodys);
        return send(address, httpMethod, requestProperty, bodyJson);
    }

    public static String getRequestIp(HttpServerRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtil.isEmpty(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtil.isEmpty(ip)) {
            ip = request.remoteAddress().hostAddress();
        }
        return ip;
    }


    public static String sendFile(String filePath, String targetPath) {
        try {
            File file = new File(filePath);
            BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            if (!file.exists() || !basicFileAttributes.isRegularFile()) {
                throw new BaseException("File not found: " + filePath).record();
            }
            System.out.println("Send file size: " + basicFileAttributes.size());
            FileInputStream fileInputStream = new FileInputStream(file);
            URL url = new URL(targetPath);
            String boundary = "*****" + System.currentTimeMillis() + "*****";
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(HttpMethod.POST.name());
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream outputStream = connection.getOutputStream();
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            printWriter.append("--" + boundary + "\r\n");
            printWriter.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n");
            printWriter.append("Content-Type: application/octet-stream\r\n\r\n");
            printWriter.flush();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            fileInputStream.close();
            outputStream.flush();
            printWriter.append("\r\n").flush();
            printWriter.append("--" + boundary + "--").append("\r\n");
            printWriter.flush();
            printWriter.close();

            int responseCode = connection.getResponseCode();
            outputStream.close();
            System.out.println("访问 [ " + targetPath + " ] 结果: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应内容
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                // 处理错误情况
                throw new BaseException("Failed to fetch data. Response Code: " + responseCode).record();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(e);
        }
    }
}
