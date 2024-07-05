package ru.netology.data;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;


public class ApiHelper {
    private static final Gson gson = new Gson();
    private static DataHelper.CardInfo cardInfo;
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(System.getProperty("app.url"))
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void payDebitCard() {
        cardInfo = DataHelper.getApprovedCard();
        var body = gson.toJson(cardInfo);
        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/payment")
                .then()
                .statusCode(200);
    }

    public static void payCreditCard() {
        cardInfo = DataHelper.getApprovedCard();
        given()
                .spec(requestSpec)
                .body(cardInfo)
                .when()
                .post("/credit")
                .then()
                .statusCode(200);

    }

    public static void createPaymentError() {
        cardInfo = DataHelper.getDeclinedCard();
        given()
                .spec(requestSpec)
                .body(cardInfo)
                .when()
                .post("/payment")
                .then()
                .statusCode(200);
    }

    public static void createCreditError() {
        cardInfo = DataHelper.getDeclinedCard();
        given()
                .spec(requestSpec)
                .body(cardInfo)
                .when()
                .post("/credit")
                .then()
                .statusCode(200);
    }
}


