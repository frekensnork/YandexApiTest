import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

@RunWith(Parameterized.class)
public class YandexTranslateTest {

    /** Слово для перевода */
    private String word;

    /** Ключ API Yandex */
    private String apiKey = "<указать ключ к Yandex API>";

    /**
     * Конструктор
     * @param word слово для перевода
     */
    public YandexTranslateTest(String word) {
        this.word = word;
    }

    @Parameterized.Parameters()
    public static Iterable prepareData() {
        return Arrays.asList("katze", "le chat", "кiшка");
    }

    @Test
    public void runTest() {
        String etalone = "кошка";
        try {
            Translator translator = new Translator(apiKey);
            Assert.assertEquals(etalone, translator.translate(word, translator.detect(word)));
        } catch (URISyntaxException|IOException|InterruptedException|ParseException ex) {
            ex.printStackTrace();
        }
    }
}
