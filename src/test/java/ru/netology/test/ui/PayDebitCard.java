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


public class PayDebitCard {
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
    @DisplayName("Покупка дебетовой картой со статусом APPROVED, все поля формы заполнены валидными значениями" +
            " и ожидаемой ценой 45_000")
    public void shouldPayDebitValidCard() {
        paymentPage.payDebitCard();
        var info = getApprovedCard();
        paymentPage.sendingValidData(info);
        paymentPage.bankApproved();
        var expected = DataHelper.getStatusFirstCard();
        var paymentInfo = SQLHelper.getPaymentInfo();
        var orderInfo = SQLHelper.getOrderInfo();
        var expectedAmount = 45_000;
        assertEquals(expected, getPaymentInfo().getStatus());
        assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
        assertEquals(expectedAmount, paymentInfo.getAmount());
    }

    //Тестовый сценарий №2
    @Test
    @SneakyThrows
    @DisplayName("Покупка дебетовой картой со статусом DECLINED, " +
            "все поля формы заполнены валидными значениями")
    void shouldPayDebitDeclinedCard() {
        paymentPage.payDebitCard();
        var info = DataHelper.getDeclinedCard();
        paymentPage.sendingNotValidData(info);
        paymentPage.bankDeclined();
        var paymentStatus = getPaymentInfo();
        assertEquals("DECLINED", paymentStatus);
    }

    //Тестовый сценарий №3
    @Test
    @DisplayName("Покупка дебетовой картой без заполнения поля карты, " +
            "остальные поля формы заполнены валидными значениями")
    void shouldEmptyFieldCardFormDebit() {
        paymentPage.payDebitCard();
        var info = DataHelper.getEmptyCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    //Тестовый сценарий №4
    @Test
    @DisplayName("Покупка дебетовой картой при заполнения поля карты одной цифрой, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldCardFormDebit() {
        paymentPage.payDebitCard();
        var info = DataHelper.getOneNumberCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    //Тестовый сценарий №5
    @Test
    @DisplayName("Покупка дебетовой картой при заполнения поля карты 15 цифрами, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldFifteenNumberInFieldCardNumberFormDebit() {
        paymentPage.payDebitCard();
        var info = DataHelper.getFifteenNumberCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCardNumberError();
    }

    //Тестовый сценарий №6
    @Test
    @DisplayName("Покупка картой не из Базы данных, все поля " +
            "формы заполнены валидными значениями")
    public void shouldFakerCardNumberFormDebit() {
        paymentPage.payDebitCard();
        var info = DataHelper.getFakerNumberCardNumber();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFakerCardNumber();
    }

    //Тестовый сценарий №7
    @Test
    @DisplayName("Покупка дебетовой картой без заполнения " +
            "поля месяц, остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldMonthFormDebit() {
        paymentPage.payDebitCard();
        var info = getEmptyMonth();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    //Тестовый сценарий №8
    @Test
    @DisplayName("Покупка дебетовой картой: в поле месяц нулевой 00(не существующий) месяц" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldFieldWithZeroMonthFormDebit() {
        paymentPage.payDebitCard();
        var info = getZeroMonthInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    //Тестовый сценарий №9
    @Test
    @DisplayName("Покупка дебетовой картой: в поле месяц тринадцатый (не существующий) месяц" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldFieldWithThirteenMonthFormDebit() {
        paymentPage.payDebitCard();
        var info = getThirteenMonthInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    //Тестовый сценарий №10
    @Test
    @DisplayName("Покупка дебетовой картой c заполнением " +
            "поля месяц одной цифрой, остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldMonthFormDebit() {
        paymentPage.payDebitCard();
        var info = getOneNumberMonth();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    //Тестовый сценарий №11
    @Test
    @DisplayName("Покупка дебетовой картой: в поле месяц " +
            "предыдущий от текущего, остальные поля формы заполнены валидными значениями")
    public void shouldFieldWithPreviousMonthFormDebit() {
        paymentPage.payDebitCard();
        var info = getPreviousMonthInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldMonthError();
    }

    //Тестовый сценарий №12
    @Test
    @DisplayName("Покупка дебетовой картой без заполнения " +
            "поля год, остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldYearFormDebit() {
        paymentPage.payDebitCard();
        var info = getEmptyYear();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    //Тестовый сценарий №13
    @Test
    @DisplayName("Покупка дебетовой картой: заполнение " +
            "поля год, предыдущим годом от текущего" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldPreviousYearFieldYearFormDebit() {
        paymentPage.payDebitCard();
        var info = getPreviousYearInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    //Тестовый сценарий №14
    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля год, на шесть лет больше чем текущий" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldPlusSixYearFieldYearFormDebit() {
        paymentPage.payDebitCard();
        var info = getPlusSixYearInField();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldYearError();
    }

    //Тестовый сценарий №15

    @Test
    @DisplayName("Покупка дебетовой картой: данные в поле фамилия и имя записаны в верхнем " +
            "регистре, остальные поля формы заполнены валидными значениями")
    public void shouldSurnameAndFirstNameInUppercaseFromDebit() {
        paymentPage.payDebitCard();
        var info = DataHelper.getNameInUppercase();
        paymentPage.sendingValidData(info);
        paymentPage.bankApproved();
    }

    //Тестовый сценарий №16
    @Test
    @DisplayName("Покупка дебетовой картой: данные в поле  фамилия и имя написано через дефис, " +
            "остальные поля формы заполнены валидными значениями")
    public void shouldSurnameOrFirstNameSeparatedByHyphenFromDebit() {
        paymentPage.payDebitCard();
        var info = DataHelper.getNameSeparatedByHyphen();
        paymentPage.sendingValidData(info);
        paymentPage.bankApproved();
    }

    //Тестовый сценарий №17
    @Test
    @DisplayName("Покупка дебетовой картой: поле владелец пустое," +
            " остальные поля формы заполнены валидными значениями")
    public void shouldEmptyFieldNameFormDebit() {
        paymentPage.payDebitCard();
        var info = getApprovedCard();
        paymentPage.sendingEmptyNameValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №18
    @Test
    @DisplayName("Покупка дебетовой картой: поле владелец только" +
            " фамилия, остальные поля формы заполнены валидными значениями")
    public void shouldOnlySurnameFormDebit() {
        paymentPage.payDebitCard();
        var info = DataHelper.getOnlySurnameInFieldName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №19
    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля владелец рус буквами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldRusNameInFieldNameFormDebit() {
        paymentPage.payDebitCard();
        var info = DataHelper.getRusName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №20
    @Test
    @DisplayName("Покупка дебетовой картой: заполнение  поля владелец цифрами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldNumberInFieldNameFormDebit() {
        paymentPage.payDebitCard();
        var info = getNumberInFieldName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №21
    @Test
    @DisplayName("Покупка дебетовой картой: заполнение поля владелец спец. символами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldSpecialSymbolInFieldNameFormDebit() {
        paymentPage.payDebitCard();
        var info = getSpecialSymbolInFieldName();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldNameError();
    }

    //Тестовый сценарий №22
    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV пустое" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldEmptyCVVInFieldCVVFormDebit() {
        paymentPage.payDebitCard();
        var info = getEmptyCVVInFieldCVV();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №23
    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV одно число" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldOneNumberInFieldCVVFormDebit() {
        paymentPage.payDebitCard();
        var info = getOneNumberInFieldCVV();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №24
    @Test
    @DisplayName("Покупка дебетовой картой: поле CVV двумя числами" +
            " остальные поля формы заполнены валидными значениями")
    public void shouldTwoNumberInFieldCVVАFormDebit() {
        paymentPage.payDebitCard();
        var info = getOTwoNumberInFieldCVV();
        paymentPage.sendingValidData(info);
        paymentPage.sendingValidDataWithFieldCVVError();
    }

    //Тестовый сценарий №25
    @Test
    @DisplayName("Покупка дебетовой картой без заполнения полей")
    void shouldEmptyFormDebitCard() {
        paymentPage.payDebitCard();
        paymentPage.pressButtonForContinue();
        paymentPage.emptyForm();
    }
}
























