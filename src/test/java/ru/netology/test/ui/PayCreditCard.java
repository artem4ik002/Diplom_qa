package ru.netology.test.ui;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.*;

public class PayCreditCard {
    PaymentPage paymentPage = new PaymentPage();

    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {

        open("http://localhost:8080");
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
        paymentPage.buyCreditCard();
        var info = getApprovedCard();
        paymentPage.sendingValidData(info);
        paymentPage.bankApproved();
        var expected = DataHelper.getStatusFirstCard();
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
        paymentPage.buyCreditCard();
        var info = DataHelper.getDeclinedCard();
        paymentPage.sendingNotValidData(info);
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
        paymentPage.buyCreditCard();
        var info = getEmptyCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    // Тестовый сценарий №4
    @Test
    @DisplayName("Покупка кредитной картой при заполнения поля карты одной цифрой, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldCardNumberWithCredit() {
        paymentPage.buyCreditCard();
        var info = getOneNumberCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    // Тестовый сценарий №5
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля карты 15 цифрами, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldFifteenNumberInFieldCardNumberWithCredit() {
        paymentPage.buyCreditCard();
        var info = getFifteenNumberCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    // Тестовый сценарий №6
    @Test
    @DisplayName("Покупка кредитной картой не из Базы данных, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldFakerCardInFieldCardNumberWithCredit() {
        paymentPage.buyCreditCard();
        var info = getFakerNumberCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFakerCardNumber();
    }

    // Тестовый сценарий №7
    @Test
    @DisplayName("Покупка кредитной картой без заполнения поля месяц, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldMonthWithCredit() {
        paymentPage.buyCreditCard();
        var info = getEmptyMonth();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    // Тестовый сценарий №8
    @Test
    @DisplayName("Покупка кредитной картой: в поле месяц " +
            "нулевой 00(не существующий) месяц остальные поля " +
            "формы заполнены валидными значениями")
    public void shouldFieldWithZeroMonthWithCredit() {
        paymentPage.buyCreditCard();
        var info = getZeroMonthInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    // Тестовый сценарий №9
    @Test
    @DisplayName("Покупка кредитной картой:  в поле месяц в верном " +
            "формате тринадцатый (не существующий) месяц" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldFieldWithThirteenMonthWithCredit() {
        paymentPage.buyCreditCard();
        var info = getThirteenMonthInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    // Тестовый сценарий №10
    @Test
    @DisplayName("Покупка кредитной картой: поле месяц одной цифрой, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldMonthWithCredit() {
        paymentPage.buyCreditCard();
        var info = getOneNumberMonth();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    // Тестовый сценарий №11
    @Test
    @DisplayName("Покупка кредитной картой: в поле месяц предыдущий " +
            "от текущего, остальные поля формы заполнены валидными значениями")
    public void shouldFieldWithPreviousMonthWithCredit() {
        paymentPage.buyCreditCard();
        var info = getPreviousMonthInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    // Тестовый сценарий №12
    @Test
    @DisplayName("Покупка кредитной картой без заполнения поля год, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldYearWithCredit() {
        paymentPage.buyCreditCard();
        var info = getEmptyYear();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    // Тестовый сценарий №13
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля год, предыдущим годом от текущего" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldPreviousYearFieldYearWithCredit() {
        paymentPage.buyCreditCard();
        var info = getPreviousYearInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    // Тестовый сценарий №14
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля год, на шесть лет больше чем текущий" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldPlusSixYearFieldYearWithCredit() {
        paymentPage.buyCreditCard();
        var info = getPlusSixYearInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    //Тестовый сценарий №15
    @Test
    @DisplayName("Покупка кредитной картой: данные в поле фамилия и имя записаны в верхнем " +
            "регистре, остальные поля формы заполнены валидными значениями")
    public void shouldSurnameAndFirstNameInUppercaseFromCredit() {
        paymentPage.buyCreditCard();
        var info = DataHelper.getNameInUppercase();
        paymentPage.sendingValidData(info);
        paymentPage.bankApproved();
    }

    //Тестовый сценарий №16
    @Test
    @DisplayName("Покупка кредитной картой: данные в поле  фамилия и имя написано через дефис, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldSurnameOrFirstNameSeparatedByHyphenFromCredit() {
        paymentPage.buyCreditCard();
        var info = DataHelper.getNameSeparatedByHyphen();
        paymentPage.sendingValidData(info);
        paymentPage.bankApproved();
    }

    //Тестовый сценарий №17
    @Test
    @DisplayName("Покупка кредитной картой: поле владелец пустое, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldNameWithCredit() {
        paymentPage.buyCreditCard();
        var info = getApprovedCard();
        paymentPage.sendingEmptyNameValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №18
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поле владелец только фамилией" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldOnlySurnameInFieldNameWithCredit() {
        paymentPage.buyCreditCard();
        var info = getOnlySurnameInFieldName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №19
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поле владелец русскими буквами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldRussianNameInFieldNameWithCredit() {
        paymentPage.buyCreditCard();
        var info = getRusName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №20
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля владелец цифрами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldNumberInFieldNameWithCredit() {
        paymentPage.buyCreditCard();
        var info = getNumberInFieldName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №21
    @Test
    @DisplayName("Покупка кредитной картой: заполнение поля владелец спец. символами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldSpecialSymbolInFieldNameWithCredit() {
        paymentPage.buyCreditCard();
        var info = getSpecialSymbolInFieldName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №22
    @Test
    @DisplayName("Покупка кредитной картой: поле CVV пустое" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldEmptyCVVInFieldCVVWithCredit() {
        paymentPage.buyCreditCard();
        var info = getEmptyCVVInFieldCVV();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №23
    @Test
    @DisplayName("Покупка кредитной картой: поле CVV одним числом" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldCVVWithCredit() {
        paymentPage.buyCreditCard();
        var info = getOneNumberInFieldCVV();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №24
    @Test
    @DisplayName("Покупка кредитной картой: поле CVV двумя числами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldTwoNumberInFieldCVVWithCredit() {
        paymentPage.buyCreditCard();
        var info = getOTwoNumberInFieldCVV();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №25
    @Test
    @DisplayName("Покупка кредитной картой: пустое поле")
    void shouldEmptyFormWithCredit() {
        paymentPage.buyCreditCard();
        paymentPage.pressButtonForContinue();
        paymentPage.emptyForm();

    }
}
































