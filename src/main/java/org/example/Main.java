package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.config.RequestConfig;

import java.io.*;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        // ВАШ API-КЛЮЧ от NASA
        String apiKey = "DNHhhb4zJLkb8J1TfqszHP6A6XnbI8KipyuBH6Tq"; // Получите ключ на https://api.nasa.gov/
        String apiUrl = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey;

        // Настройка HttpClient
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // Таймаут на подключение
                        .setSocketTimeout(30000)    // Таймаут на получение ответа
                        .setRedirectsEnabled(true)  // Разрешение редиректов
                        .build())
                .build();

        try {
            // Шаг 1: Запрос к API NASA
            HttpGet request = new HttpGet(apiUrl);
            CloseableHttpResponse response = httpClient.execute(request);

            // Чтение JSON-ответа
            ObjectMapper mapper = new ObjectMapper();
            NasaApodResponse nasaResponse = mapper.readValue(
                    response.getEntity().getContent(), NasaApodResponse.class);
            System.out.println("Ответ от NASA: " + nasaResponse);

            // Закрытие ответа
            response.close();

            // Шаг 2: Скачивание файла по URL
            String fileUrl = nasaResponse.getUrl();
            System.out.println("Скачивание файла по URL: " + fileUrl);

            // Извлечение имени файла из URL
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            System.out.println("Имя файла: " + fileName);

            // Запрос для скачивания файла
            HttpGet fileRequest = new HttpGet(fileUrl);
            HttpResponse fileResponse = httpClient.execute(fileRequest);

            // Сохранение файла
            try (InputStream inputStream = fileResponse.getEntity().getContent();
                 FileOutputStream outputStream = new FileOutputStream(fileName)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("Файл успешно сохранён как: " + fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
