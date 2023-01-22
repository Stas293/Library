package ua.org.training.library.utility.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Date;

public class OrderValidation {
    private static final Logger LOGGER = LogManager.getLogger(OrderValidation.class);

    public static boolean isDateExpireValid(Date dateExpire) {
        LOGGER.info("isDateExpireValid");
        LocalDate localDateExpire = LocalDate.now().atStartOfDay().toLocalDate();
        return dateExpire != null && !dateExpire.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().isBefore(localDateExpire);
    }
}
