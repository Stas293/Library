package ua.org.training.library.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.CaptchaException;

public class CaptchaValidator {
    private static final Logger LOGGER = LogManager.getLogger(CaptchaValidator.class);
    private final String requestMethod;
    private final String secretKey;
    private final String requestUrl;

    public CaptchaValidator() {
        requestMethod = Utility.getApplicationProperty("captcha.method");
        secretKey = Utility.getApplicationProperty("captcha.secret");
        requestUrl = Utility.getApplicationProperty("captcha.url");
    }

    public void checkCaptcha(String captchaResponse) throws CaptchaException {
        if (captchaResponse == null || captchaResponse.isEmpty()) {
            LOGGER.error("Captcha is empty");
            throw new CaptchaException("Captcha is empty");
        }
        LOGGER.info(captchaResponse);
        String url = requestUrl + "?secret=" + secretKey + "&response=" + captchaResponse;
        String response = Utility.sendRequest(url, requestMethod);
        if (response == null || !response.contains("true")) {
            LOGGER.error("Captcha is not valid");
            throw new CaptchaException("Captcha is not valid");
        }
    }
}
