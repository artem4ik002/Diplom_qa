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

public class PayCreditCard {
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
    @DisplayName("Покупка кредитной картой со статусом APPROVED, " +
            "все поля формы заполнены валидными значениями")
    void shouldApproveCreditCard() {
        mainPage.buyCreditCard();
        var info = getApprovedCard();
        paymentPage.sendData(info);
        paymentPage.bankApproved();
        var expected = getStatusFirstCard();
        var creditRequest = getCreditRequestInfo();
        var orderInfo = getOrderInfo();
        assertEquals(expected, getCreditRequestInfo().getStatus());
        assertEquals(orderInfo.getPayment_id(), creditRequest.getBank_id());
    }

    // Тестовый сценарий №2
    @Test
    @SneakyThrows
    @DisplayName("Покупка кредитной картой со статусом DECLINED, " +
            "все поля формы заполнены валидными значениями")
    void shouldPayCreditDeclinedCard() {
        mainPage.buyCreditCard();
        var info = getDeclinedCard();
        paymentPage.sendData(info);
        paymentPage.bankDeclined();
        var expected = getStatusSecondCard();
        var paymentInfo = getPaymentInfo().getStatus();
        assertEquals(expected, paymentInfo);
    }

    // Тестовый сценарий №3
    @Test
    @DisplayName("Покупка кредитной картой без заполнения поля карты, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldCardWithCredit() {
        mainPage.buyCreditCard();
        var info = getEmptyCardNumber();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    // Тестовый сценарий №4
    @Test
    @DisplayName("Покупка кредитной картой при заполнения поля карты одной цифрой, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldCardNumberWithCredit() {
        mainPage.buyCreditCard();
        var info = getOneNumberCardNumber();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    // Тестовый сценарий №5
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля карты 15 цифрами, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldFifteenNumberInFieldCardNumberWithCredit() {
        mainPage.buyCreditCard();
        var info = getFifteenNumberCardNumber();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    // Тестовый сценарий №6
    @Test
    @DisplayName("Покупка кредитной картой не из Базы данных, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldFakerCardInFieldCardNumberWithCredit() {
        mainPage.buyCreditCard();
        var info = getFakerNumberCardNumber();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFakerCardNumber();
    }

    // Тестовый сценарий №7
    @Test
    @DisplayName("Покупка кредитной картой без заполнения поля месяц, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldMonthWithCredit() {
        mainPage.buyCreditCard();
        var info = getEmptyMonth();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    // Тестовый сценарий №8
    @Test
    @DisplayName("Покупка кредитной картой: в поле месяц " +
            "нулевой 00(не существующий) месяц остальные поля " +
            "формы заполнены валидными значениями")
    public void shouldFieldWithZeroMonthWithCredit() {
        mainPage.buyCreditCard();
        var info = getZeroMonthInField();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    // Тестовый сценарий №9
    @Test
    @DisplayName("Покупка кредитной картой:  в поле месяц в верном " +
            "формате тринадцатый (не существующий) месяц" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldFieldWithThirteenMonthWithCredit() {
        mainPage.buyCreditCard();
        var info = getThirteenMonthInField();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    // Тестовый сценарий №10
    @Test
    @DisplayName("Покупка кредитной картой: поле месяц одной цифрой, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldMonthWithCredit() {
        mainPage.buyCreditCard();
        var info = getOneNumberMonth();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    // Тестовый сценарий №11
    @Test
    @DisplayName("Покупка кредитной картой: в поле месяц предыдущий " +
            "от текущего, остальные поля формы заполнены валидными значениями")
    public void shouldFieldWithPreviousMonthWithCredit() {
        mainPage.buyCreditCard();
        var info = getPreviousMonthInField();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    // Тестовый сценарий №12
    @Test
    @DisplayName("Покупка кредитной картой без заполнения поля год, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldYearWithCredit() {
        mainPage.buyCreditCard();
        var info = getEmptyYear();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    // Тестовый сценарий №13
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля год, предыдущим годом от текущего" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldPreviousYearFieldYearWithCredit() {
        mainPage.buyCreditCard();
        var info = getPreviousYearInField();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    // Тестовый сценарий №14
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля год, на шесть лет больше чем текущий" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldPlusSixYearFieldYearWithCredit() {
        mainPage.buyCreditCard();
        var info = getPlusSixYearInField();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    //Тестовый сценарий №15
    @Test
    @DisplayName("Покупка кредитной картой: данные в поле фамилия и имя записаны в верхнем " +
            "регистре, остальные поля формы заполнены валидными значениями")
    public void shouldSurnameAndFirstNameInUppercaseFromCredit() {
        mainPage.buyCreditCard();
        var info = DataHelper.getNameInUppercase();
        paymentPage.sendData(info);
        paymentPage.bankApproved();
    }

    //Тестовый сценарий №16
    @Test
    @DisplayName("Покупка кредитной картой: данные в поле  фамилия и имя написано через дефис, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldSurnameOrFirstNameSeparatedByHyphenFromCredit() {
        mainPage.buyCreditCard();
        var info = DataHelper.getNameSeparatedByHyphen();
        paymentPage.sendData(info);
        paymentPage.bankApproved();
    }

    //Тестовый сценарий №17
    @Test
    @DisplayName("Покупка кредитной картой: поле владелец пустое, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldNameWithCredit() {
        mainPage.buyCreditCard();
        var info = getApprovedCard();
        paymentPage.sendingEmptyNameValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №18
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поле владелец только фамилией" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldOnlySurnameInFieldNameWithCredit() {
        mainPage.buyCreditCard();
        var info = getOnlySurnameInFieldName();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №19
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поле владелец русскими буквами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldRussianNameInFieldNameWithCredit() {
        mainPage.buyCreditCard();
        var info = getRusName();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №20
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля владелец цифрами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldNumberInFieldNameWithCredit() {
        mainPage.buyCreditCard();
        var info = getNumberInFieldName();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №21
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля владелец спец. символами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldSpecialSymbolInFieldNameWithCredit() {
        mainPage.buyCreditCard();
        var info = getSpecialSymbolInFieldName();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №22
    @Test
    @DisplayName("Покупка кредитной картой: поле CVV пустое" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldEmptyCVVInFieldCVVWithCredit() {
        mainPage.buyCreditCard();
        var info = getEmptyCVVInFieldCVV();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №23
    @Test
    @DisplayName("Покупка кредитной картой: поле CVV одним числом" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldCVVWithCredit() {
        mainPage.buyCreditCard();
        var info = getOneNumberInFieldCVV();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №24
    @Test
    @DisplayName("Покупка кредитной картой: поле CVV двумя числами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldTwoNumberInFieldCVVWithCredit() {
        mainPage.buyCreditCard();
        var info = getOTwoNumberInFieldCVV();
        paymentPage.sendData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №25
    @Test
    @DisplayName("Покупка кредитной картой: пустое поле")
    void shouldEmptyFormWithCredit() {
        mainPage.buyCreditCard();
        paymentPage.pressButtonForContinue();
        paymentPage.emptyForm();

    }
}
































