package ua.org.training.library.validator;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.InjectProperty;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

@Component
@Slf4j
public class CaptchaValidator {
    @InjectProperty("captcha.method")
    private String requestMethod;
    @InjectProperty("captcha.secret")
    private String secretKey;
    @InjectProperty("captcha.url")
    private String requestUrl;

    public boolean checkCaptcha(String captchaResponse) {
        if (captchaResponse == null || captchaResponse.isEmpty()) {
            log.error("Captcha is empty");
            return false;
        }
        log.info(captchaResponse);
        String url = "%s?secret=%s&response=%s".formatted(requestUrl, secretKey, captchaResponse);
        String response = sendRequest(url, requestMethod);
        if (response == null || !response.contains("true")) {
            log.error("Captcha is not valid");
            return false;
        }
        log.info("Captcha is valid");
        return true;
    }

    @SneakyThrows
    public String sendRequest(String url, String requestMethod) {
        log.info("Sending request to: " + url);
        URI uri = new URI(url);
        URL urlObj = uri.toURL();
        HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.flush();
        dataOutputStream.close();
        StringBuilder response = collectStringFromResponse(connection);
        return response.toString();
    }

    private StringBuilder collectStringFromResponse(HttpsURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        Scanner scanner = new Scanner(connection.getInputStream());
        while (scanner.hasNextLine()) {
            response.append(scanner.nextLine());
        }
        scanner.close();
        return response;
    }
}
