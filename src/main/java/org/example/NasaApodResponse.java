package org.example;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // Игнорирование неизвестных полей
public class NasaApodResponse {

    @JsonProperty("url")
    private String url;

    @JsonProperty("title")
    private String title;

    // Геттеры и сеттеры
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "NasaApodResponse{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
