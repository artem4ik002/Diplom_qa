package ru.netology.test.ui;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.MainPage;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.*;


public class PayDebitCard {
    PaymentPage paymentPage = new PaymentPage();
    MainPage mainPage = new MainPage();


    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {

        open(System.getProperty("website.url"));
    }

    @AfterEach
    void cleanDB() {

        SQLHelper.databaseCleanUp();
    }

    @AfterAll
    public static void tearDownAll() {

        SelenideLogger.removeListener("allure");
    }

    // Тестовый сценарий №1
    @Test
    @SneakyThrows
    @DisplayName("Покупка дебетовой картой со статусом APPROVED, все поля формы заполнены валидными значениями" +
            " и ожидаемой ценой 45_000")
    public void shouldPayDebitValidCard() {
        mainPage.payDebitCard();
        var info = getApprovedCard();
        paymentPage.sendData(info);
        paymentPage.bankApproved();
        var expected = DataHelper.getStatusFirstCard();
        var paymentInfo = SQLHelper.getPaymentInfo();
        var orderInfo = SQLHelper.getOrderInfo();
        var expectedAmount = 45_000;
        assertEquals(expected, getPaymentInfo().getStatus());
        assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
        assertEquals(expectedAmount, paymentInfo.getAmount());
    }

    //Тестовый сценарий №2 БАГ
    @Test
    @SneakyThrows
    @DisplayName("Покупка дебетовой картой со статусом DECLINED, " +
            "все поля формы заполнены валидными значениями")
    void shouldPayDebitDeclinedCard() {
        mainPage.payDebitCard();
        var info = DataHelper.getDeclinedCard();
        paymentPage.sendData(info);
        paymentPage.bankDeclined();
        var paymentStatus = getPaymentInfo();
        assertEquals("DECLINED", paymentStatus);
    }

    //Тестовый сценарий №3
    @Test
    @DisplayName("Покупка дебетовой картой без заполнения поля карты, " +
            "остальные поля формы заполнены валидными значениями")
    void shouldEmptyFieldCardFormDebit() {
        mainPage.payDebitCard();
        var info = DataHelper.getEmptyCardNumber();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    //Тестовый сценарий №4
    @Test
    @DisplayName("Покупка дебетовой картой при заполнения поля карты одной цифрой, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldCardFormDebit() {
        mainPage.payDebitCard();
        var info = DataHelper.getOneNumberCardNumber();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    //Тестовый сценарий №5
    @Test
    @DisplayName("Покупка дебетовой картой при заполнения поля карты 15 цифрами, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldFifteenNumberInFieldCardNumberFormDebit() {
        mainPage.payDebitCard();
        var info = DataHelper.getFifteenNumberCardNumber();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    //Тестовый сценарий №6
    @Test
    @DisplayName("Покупка картой не из Базы данных, все поля " +
            "формы заполнены валидными значениями")
    public void shouldFakerCardNumberFormDebit() {
        mainPage.payDebitCard();
        var info = DataHelper.getFakerNumberCardNumber();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFakerCardNumber();
    }

    //Тестовый сценарий №7
    @Test
    @DisplayName("Покупка дебетовой картой без заполнения " +
            "поля месяц, остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldMonthFormDebit() {
        mainPage.payDebitCard();
        var info = getEmptyMonth();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    //Тестовый сценарий №8
    @Test
    @DisplayName("Покупка дебетовой картой: в поле месяц нулевой 00(не существующий) месяц" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldFieldWithZeroMonthFormDebit() {
        mainPage.payDebitCard();
        var info = getZeroMonthInField();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    //Тестовый сценарий №9
    @Test
    @DisplayName("Покупка дебетовой картой: в поле месяц тринадцатый (не существующий) месяц" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldFieldWithThirteenMonthFormDebit() {
        mainPage.payDebitCard();
        var info = getThirteenMonthInField();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    //Тестовый сценарий №10
    @Test
    @DisplayName("Покупка дебетовой картой c заполнением " +
            "поля месяц одной цифрой, остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldMonthFormDebit() {
        mainPage.payDebitCard();
        var info = getOneNumberMonth();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    //Тестовый сценарий №11
    @Test
    @DisplayName("Покупка дебетовой картой: в поле месяц " +
            "предыдущий от текущего, остальные поля формы заполнены валидными значениями")
    public void shouldFieldWithPreviousMonthFormDebit() {
        mainPage.payDebitCard();
        var info = getPreviousMonthInField();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    //Тестовый сценарий №12
    @Test
    @DisplayName("Покупка дебетовой картой без заполнения " +
            "поля год, остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldYearFormDebit() {
        mainPage.payDebitCard();
        var info = getEmptyYear();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    //Тестовый сценарий №13
    @Test
    @DisplayName("Покупка дебетовой картой: заполнение " +
            "поля год, предыдущим годом от текущего" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldPreviousYearFieldYearFormDebit() {
        mainPage.payDebitCard();
        var info = getPreviousYearInField();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    //Тестовый сценарий №14
    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля год, на шесть лет больше чем текущий" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldPlusSixYearFieldYearFormDebit() {
        mainPage.payDebitCard();
        var info = getPlusSixYearInField();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    //Тестовый сценарий №15

    @Test
    @DisplayName("Покупка дебетовой картой: данные в поле фамилия и имя записаны в верхнем " +
            "регистре, остальные поля формы заполнены валидными значениями")
    public void shouldSurnameAndFirstNameInUppercaseFromDebit() {
        mainPage.payDebitCard();
        var info = DataHelper.getNameInUppercase();
        paymentPage.sendData(info);
        paymentPage.bankApproved();
    }

    //Тестовый сценарий №16
    @Test
    @DisplayName("Покупка дебетовой картой: данные в поле  фамилия и имя написано через дефис, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldSurnameOrFirstNameSeparatedByHyphenFromDebit() {
        mainPage.payDebitCard();
        var info = DataHelper.getNameSeparatedByHyphen();
        paymentPage.sendData(info);
        paymentPage.bankApproved();
    }

    //Тестовый сценарий №17
    @Test
    @DisplayName("Покупка дебетовой картой: поле владелец пустое," +
            " остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldNameFormDebit() {
        mainPage.payDebitCard();
        var info = getApprovedCard();
        paymentPage.sendingEmptyNameValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №18
    @Test
    @DisplayName("Покупка дебетовой картой: поле владелец только" +
            " фамилия, остальные поля формы заполнены валидными значениями")
    public void shouldOnlySurnameFormDebit() {
        mainPage.payDebitCard();
        var info = DataHelper.getOnlySurnameInFieldName();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №19
    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля владелец рус буквами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldRusNameInFieldNameFormDebit() {
        mainPage.payDebitCard();
        var info = DataHelper.getRusName();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №20
    @Test
    @DisplayName("Покупка дебетовой картой: заполнение  поля владелец цифрами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldNumberInFieldNameFormDebit() {
        mainPage.payDebitCard();
        var info = getNumberInFieldName();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №21
    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля владелец спец. символами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldSpecialSymbolInFieldNameFormDebit() {
        mainPage.payDebitCard();
        var info = getSpecialSymbolInFieldName();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №22
    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV пустое" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldEmptyCVVInFieldCVVFormDebit() {
        mainPage.payDebitCard();
        var info = getEmptyCVVInFieldCVV();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №23
    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV одно число" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldCVVFormDebit() {
        mainPage.payDebitCard();
        var info = getOneNumberInFieldCVV();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №24
    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV двумя числами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldTwoNumberInFieldCVVАFormDebit() {
        mainPage.payDebitCard();
        var info = getOTwoNumberInFieldCVV();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №25
    @Test
    @DisplayName("Покупка дебетовой картой без заполнения полей")
    void shouldEmptyFormDebitCard() {
        mainPage.payDebitCard();
        paymentPage.pressButtonForContinue();
        paymentPage.emptyForm();
    }
}
























