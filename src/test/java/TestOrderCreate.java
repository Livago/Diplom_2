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

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
public class TestOrderCreate {
    UserSteps userSteps;
    OrderSteps orderSteps;
    String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
    String password = RandomStringUtils.randomNumeric(8);
    String name = RandomStringUtils.randomAlphabetic(7);
    private String accessToken;
    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
        orderSteps = new OrderSteps(new OrderClient());
        userSteps.create(email, password, name);
        ValidatableResponse response = userSteps.login(email, password);
        accessToken = userSteps.getAccessToken(response);
    }

    /* Для ревьювера.
    Я так понимаю тут так и должно падать тк - Заказ не должен создаваться согласно требованиям: - Только авторизованные пользователи могут делать заказы.
    Структура самих ручек не меняется, но нужно давать токен при запросе к серверу в поле авторизации */
    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка невозможности создания заказа без авторизации")
    public void testOrderCreateWithoutToken() {
        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa71");
        orderSteps.createOrderWithoutToken(ingredients)
                .statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }
    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка успешного создания заказа с авторизацией")
    public void testOrderCreateWithToken() {
        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa74");
        orderSteps.createOrderWithToken(accessToken, ingredients)
                .statusCode(SC_OK).and().body("success", equalTo(true));
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка невозможности создания заказа без ингредиентов")
    public void testOrderCreateWithoutIngredients() {
        List<String> ingredients = Arrays.asList();
        orderSteps.createOrderWithToken(accessToken, ingredients)
                .statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка невозможности создания заказа без ингредиентов")
    public void testOrderCreateWithInvalidHashIngredients() {
        List<String> ingredients = Arrays.asList("123", "qwe");
        orderSteps.createOrderWithToken(accessToken, ingredients)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
    @After
    public void tearDown() {
        userSteps.delete();
    }
}
