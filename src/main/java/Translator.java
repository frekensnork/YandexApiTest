import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Класс для обращения к сервисам переводчика Yandex и обработки ответов
 */
class Translator {

    /** Ключ API Yandex */
    private String apiKey;

    /** Конструктор */
    Translator(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Определение языка, на котором написан заданный текст
     * @param word текст
     * @return двухбуквенный код языка
     */
    String detect(String word) throws URISyntaxException, IOException, InterruptedException, ParseException {
        String params = "key=" + apiKey + "&text=" + word;

        HttpResponse response = requestYandex("detect", params);

        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.body().toString());
        return jsonObject.get("lang").toString();
    }

    /**
     * Перевод текста на русский язык
     * @param word текст
     * @param lang язык, на котором написан текст
     * @return текст на русском языке
     */
    String translate(String word, String lang) throws URISyntaxException, IOException, InterruptedException, ParseException {
        String params = "key=" + apiKey + "&text=" + word + "&lang=" + lang + "-ru";

        HttpResponse response = requestYandex("translate", params);

        JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.body().toString());
        return String.join(" ", (JSONArray) jsonObject.get("text"));
    }

    /**
     * Отправить запрос к Yandex
     * @param url    относительный URL запроса
     * @param params параметры запроса
     * @return ответ сервера
     */
    private static HttpResponse<String> requestYandex(String url, String params) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://translate.yandex.net/api/v1.5/tr.json/" + url))
                .POST(HttpRequest.BodyPublishers.ofString(params))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
