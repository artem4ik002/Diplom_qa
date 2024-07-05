package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

    public void payDebitCard() {
        buttonBuy.click();
        buttonDebit.shouldBe(visible);
    }

    public void buyCreditCard() {
        buttonBuyWithCredit.click();
        buttonCredit.shouldBe(visible);
    }

    public SelenideElement buttonBuy = $x("//span[text()='Купить']//ancestor::button");
    public SelenideElement buttonBuyWithCredit = $x("//span[text()='Купить в кредит']//ancestor::button");

    public SelenideElement buttonDebit = $(withText("Оплата по карте"));
    public SelenideElement buttonCredit = $(withText("Кредит по данным карты"));



}
