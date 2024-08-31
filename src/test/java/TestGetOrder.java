import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.OrderSteps;
import step.UserSteps;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
public class TestGetOrder {
    UserSteps userSteps;
    OrderSteps orderSteps;
    String email = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
    String password = RandomStringUtils.randomNumeric(9);
    String name = RandomStringUtils.randomAlphabetic(8);
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
        orderSteps = new OrderSteps(new OrderClient());
        userSteps.create(email, password, name);
        ValidatableResponse response = userSteps.login(email, password);
        accessToken = userSteps.getAccessToken(response);
        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f");
        orderSteps.createOrderWithToken(accessToken, ingredients);
    }
    @Test
    @DisplayName("Получение заказа с авторизацией")
    @Description("Проверка успешного получения заказа с авторизацией")
    public void testGetOrderWithToken() {
        orderSteps.getOrderWithToken(accessToken).statusCode(SC_OK).and().body("success", equalTo(true));
    }
    @Test
    @DisplayName("Получение заказа без авторизациеи")
    @Description("Проверка невозможности получения заказа без авторизации")
    public void testGetOrderWithoutToken() {
        orderSteps.getOrderWithoutToken().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }
    @After
    public void tearDown() {
        userSteps.delete();
    }
}
