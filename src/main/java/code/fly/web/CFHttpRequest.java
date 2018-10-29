package code.fly.web;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CFHttpRequest {
    private String url;
    private String method;
    private Map<String, String> headers;
    private String data;

    private CFHttpRequest() {
        data = "";
        headers = new HashMap<>();
    }

    public static CFHttpRequest newRequest() {
        return new CFHttpRequest();
    }

    public CFHttpRequest addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public CFHttpRequest addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public CFHttpRequest get(String url) {
        this.url = url;
        method = "GET";
        return this;
    }

    public CFHttpRequest get(String url, Map<String, String> param) {
        return get(url, param, false);
    }

    public CFHttpRequest get(String url, Map<String, String> param, boolean encode) {
        String paramStr = mapParamToString(param, encode);

        url += ("?" + paramStr);

        this.url = url;
        method = "GET";
        return this;
    }

    public CFHttpRequest post(String url, Map<String, String> param) {
        return post(url, param, false);
    }

    public CFHttpRequest post(String url, Map<String, String> param, boolean encode) {
        data = mapParamToString(param, encode);
        this.url = url;
        method = "POST";
        return this;
    }

    public CFHttpResponse execute() {
        checkParam();
        CFHttpResponse result = new CFHttpResponse();

        try {
            URL localURL = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) localURL.openConnection();

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(method.toUpperCase());

            headers.forEach(httpURLConnection::setRequestProperty);
            if (!data.isEmpty()) {
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length()));
            }

            if (method.equals("POST")) {
                try (OutputStream outputStream = httpURLConnection.getOutputStream();
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)) {
                    if (!data.isEmpty()) {
                        outputStreamWriter.write(data);
                        outputStreamWriter.flush();
                    }
                }
            }

            StringBuilder responseBody = new StringBuilder();
            String responseLine;

            result.setCode(httpURLConnection.getResponseCode());
            if (result.getCode() < 300) {
                try (InputStream inputStream = httpURLConnection.getInputStream();
                     InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                     BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    while ((responseLine = reader.readLine()) != null) {
                        responseBody.append(responseLine);
                    }

                    result.setBody(responseBody.toString());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private void checkParam() {
        if (url.isEmpty() || !url.startsWith("http")) {
            throw new IllegalArgumentException("url is invalid");
        }

        if (method.isEmpty()) {
            throw new IllegalArgumentException("method is invalid");
        }
    }

    private String mapParamToString(Map<String, String> param, boolean encode) {
        String paramStr = "";
        if (param != null && param.size() != 0) {
            StringBuilder paramStrBuilder = new StringBuilder();
            for (Map.Entry<String, String> item : param.entrySet()) {
                paramStrBuilder.append("&").append(item.getKey()).append("=").append(item.getValue());
            }
            paramStr = paramStrBuilder.toString();
        }

        if (!paramStr.isEmpty()) {
            paramStr = paramStr.substring(1);
        }

        if (encode) {
            paramStr = URLEncoder.encode(paramStr, StandardCharsets.UTF_8);
        }
        return paramStr;
    }
}
