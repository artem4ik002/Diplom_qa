package ru.netology.test;

import org.junit.jupiter.api.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.PageTravel;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.SQLHelper.*;

public class TestPay {

    private static DataHelper.PaymentEntity payment;
    private static DataHelper.OrderEntity order;
    private static String url = System.getProperty("app.url");

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open(url);
    }

    @AfterEach
    public void cleanData() {
        cleanDatabase();
    }

    // ### Позитивные сценарии:

    @Test
    @DisplayName("Card number with status APPROVED for payment") // **Номер карты со статусом APPROVED.**
    void shouldSuccessfulPayWithAPPROVEDCard() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        int price = page.getPriceInKops();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationOk();

        payment = SQLHelper.getPaymentEntity();
        order = SQLHelper.getOrderEntity();
        assertEquals(status, payment.getStatus());
        assertEquals(price, payment.getAmount());
        assertEquals(payment.getTransaction_id(), order.getPayment_id());
    }

    @Test
    @DisplayName("Card number with status DECLINED for payment") // **Номер карты со статусом DECLINED.**
    void shouldErrorPayWithDECLINEDCard() {

        String status = "DECLINED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationError();

        assertEquals(status, SQLHelper.getPaymentEntity().getStatus());
    }

 @Test
 @DisplayName("Cardholder's name contains a hyphen") // 4.1. **Имя содержит дефиз**
 void shouldNameContainingHyphenForPay() {

     String status = "APPROVED";
     PageTravel page = new PageTravel();
     page.buy();
     page.inputNumberCard(status);
     page.inputMonth(DataHelper.generateMonthPlus(0));
     page.inputYear(DataHelper.generateYearPlus(0));
     page.inputOwner(DataHelper.getNameContainingHyphen());
     page.inputCVC(3);
     page.clickContinue();
     page.waitNotificationOk();
 }

    @Test
    @DisplayName("Name of the cardholder in uppercase") // 4.1. **Имя Caps Lock**
    void shouldNameOfCardholderInUppercaseForPay() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(0));
        page.inputOwner(DataHelper.getNameOfCardholderInUppercase());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationOk();
    }


    // ### Негативные сценарии:

    @Test
    @DisplayName("Empty form for payment") // **Пустая форма**
    void shouldMessageFilInFieldInPay() {

        PageTravel page = new PageTravel();
        page.buy();
        page.clickContinue();
        page.waitNotificationMessageNumber("Поле обязательно для заполнения");
        page.waitNotificationMessageMonth("Поле обязательно для заполнения");
        page.waitNotificationMessageYear("Поле обязательно для заполнения");
        page.waitNotificationMessageOwner("Поле обязательно для заполнения");
        page.waitNotificationMessageCVC("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Card number with status INVALID for payment") // **Форма с невалидным номером карты**
    void shouldErrorPayWithINVALIDCard() {

        String status = "INVALID";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationError();
        assertEquals(null, SQLHelper.getOrderEntity());
        assertEquals(null, SQLHelper.getPaymentEntity());
    }

    @Test
    @DisplayName("Card number with status ZERO for payment") // **Номер карты все нули**
    void shouldErrorPayWithZEROCard() {

        String status = "ZERO";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationError();
        assertEquals(null, SQLHelper.getOrderEntity());
        assertEquals(null, SQLHelper.getPaymentEntity());
    }

    @Test
    @DisplayName("Card number with status FIFTEEN for payment") // **Форма, где в номере карт цифр меньше 16**
    void shouldErrorPayWithFIFTEENCard() {

        String status = "FIFTEEN";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationMessageNumber("Неверный формат");
    }

    @Test
    @DisplayName("Month ZERO for payment") // **Месяц 00**
    void shouldErrorZeroMonthForPay() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.getZero());
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationMessageMonth("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Month Over for payment") // **Месяц более 12**
    void shouldErrorOverMonthForPay() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.getMonthOver());
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationMessageMonth("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Month One Digit for payment") // **Месяц менее 2 цифр**
    void shouldErrorOneDigitMonthForPay() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.getMonthOneDig());
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationMessageMonth("Неверный формат");
    }

    @Test
    @DisplayName("Year ZERO for payment") // 4.1. **Год 00**
    void shouldErrorZeroYearForPay() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.getZero());
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationMessageYear("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Year More for payment") // **Год более допустимого**
    void shouldErrorMoreYearForPay() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(10));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationMessageYear("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Year Less for payment") // **Год менее допустимого**
    void shouldErrorLessYearForPay() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(-1));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationMessageYear("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Cyrillic Name  for payment") // **В поле Владелец имя и фамилия на кириллице**
    void shouldErrorCyrillicNameForPayment() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolderCyrillic());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationMessageOwner("Неверный формат");
    }

    @Test
    @DisplayName("Number Name for payment") // **В поле Владелец ввести цифры**
    void shouldErrorNumberNameForPayment() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolderNumeric());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationMessageOwner("Неверный формат");
    }

    @Test
    @DisplayName("One letter Name for payment") // **В поле Владелец одна буква**
    void shouldErrorOneLetterNameForPayment() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolderOneSymbol());
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationMessageOwner("Неверный формат");
    }

    @Test
    @DisplayName("Special characters Name for payment") // **В поле Владелец - спецсимволы**
    void shouldErrorSpecCharNameForPayment() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolderSpecChar(5));
        page.inputCVC(3);
        page.clickContinue();
        page.waitNotificationMessageOwner("Неверный формат");
    }

    @Test
    @DisplayName("Two digits CVC for payment") // **В поле "CVC/CVV" две цифры**
    void shouldErrorTwoDigCVCForPayment() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(2);
        page.clickContinue();
        page.waitNotificationMessageCVC("Неверный формат");
    }

    @Test
    @DisplayName("One digits CVC for payment") // **В поле "CVC/CVV" одна цифра;**
    void shouldErrorOneDigCVCForPayment() {

        String status = "APPROVED";
        PageTravel page = new PageTravel();
        page.buy();
        page.inputNumberCard(status);
        page.inputMonth(DataHelper.generateMonthPlus(0));
        page.inputYear(DataHelper.generateYearPlus(1));
        page.inputOwner(DataHelper.generateHolder());
        page.inputCVC(1);
        page.clickContinue();
        page.waitNotificationMessageCVC("Неверный формат");
    }
}