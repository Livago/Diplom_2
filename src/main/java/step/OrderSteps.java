package step;

import client.OrderClient;
import dto.OrderCreateRequest;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.List;
public class OrderSteps {
    private final OrderClient orderClient;
    public OrderSteps(OrderClient orderClient) {
        this.orderClient = orderClient;
    }
    @Step("Создать заказ без авторизации")
    public ValidatableResponse createOrderWithoutToken(List<String> ingredients) {
        OrderCreateRequest request = new OrderCreateRequest(ingredients);
        request.setIngredients(ingredients);
        return orderClient.createOrder(request).then();
    }
    @Step("Создать заказ с авторизацией")
    public ValidatableResponse createOrderWithToken(String accessToken, List<String> ingredients) {
        OrderCreateRequest request = new OrderCreateRequest(ingredients);
        request.setIngredients(ingredients);
        return orderClient.createOrderWithToken(accessToken, new OrderCreateRequest(ingredients)).then();
    }
    @Step("Получить заказ пользователя с авторизацией")
    public ValidatableResponse getOrderWithToken(String accessToken) {
        return orderClient.getOrderWithToken(accessToken).then();
    }
    @Step("Получить заказ пользователя без авторизации")
    public ValidatableResponse getOrderWithoutToken() {
        return orderClient.getOrder().then();
    }
}
