package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class ReplanDeliveryTest {

    @Test
    void shouldReplanMeeting() {
        var user = DataGenerator.Registration.generateUser("ru");
        open("http://localhost:9999");

        // первая дата
        var firstDate = DataGenerator.generateDate(4);

        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstDate);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $("button.button").click();

        // проверяем УСПЕХ первой даты
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstDate),
                        Duration.ofSeconds(15));

        // вторая дата (перепланирование)
        var secondDate = DataGenerator.generateDate(7);

        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondDate);
        $("button.button").click();

        // подтверждение перепланирования
        $("[data-test-id=replan-notification] button")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .click();

        // проверяем УСПЕХ второй даты
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondDate),
                        Duration.ofSeconds(15));
    }
}
