package client;

import dto.OrderCreateRequest;
import io.restassured.response.Response;
public class OrderClient extends RestClient {
    private static final String ORDER_API = "api/orders";
    private static final String GET_INGREDIENTS = "api/ingredients";

    public Response createOrder(OrderCreateRequest orderCreateRequest) {
        return defaultRestSpecification()
                .body(orderCreateRequest)
                .when()
                .post(ORDER_API);
    }
    public Response createOrderWithToken(String accessToken, OrderCreateRequest orderCreateRequest) {
        return defaultRestSpecification()
                .header("authorization", accessToken)
                .body(orderCreateRequest)
                .when()
                .post(ORDER_API);
    }
    public Response getOrderWithToken(String accessToken) {
        return defaultRestSpecification()
                .header("authorization", accessToken)
                .get(ORDER_API);
    }
    public Response getOrder() {
        return defaultRestSpecification()
                .get(ORDER_API);
    }
}
