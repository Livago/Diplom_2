package client;

import dto.UserCreateRequest;
import dto.UserLoginRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
public class UserClient extends RestClient {
    public static final  String CREATE_USER_HANDLE = "api/auth/register";
    public static final  String LOGIN_USER_HANDLE = "api/auth/login";
    public static final  String AUTH_USER_HANDLE = "api/auth/user";

    public Response create(UserCreateRequest userCreateRequest) {
        return defaultRestSpecification()
                .body(userCreateRequest)
                .when()
                .post(CREATE_USER_HANDLE);

    }
    public Response login(UserLoginRequest userLoginRequest){
        return defaultRestSpecification()
                .body(userLoginRequest)
                .when()
                .post(LOGIN_USER_HANDLE);
    }

    public Response update(String accessToken, String email, String password, String name) {
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        return defaultRestSpecification()
                .header("authorization", accessToken)
                .body(userCreateRequest)
                .when()
                .patch(AUTH_USER_HANDLE);
    }
    public Response updateWithoutToken(String email, String password, String name) {
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        return defaultRestSpecification()
                .body(userCreateRequest)
                .when()
                .patch(AUTH_USER_HANDLE);
    }
    public void deleteUser(String accessToken) {
        given()
                .header("authorization", accessToken)
                .spec(defaultRestSpecification())
                .when()
                .delete(AUTH_USER_HANDLE);
    }
    public void deletingUsersAfterTests(String accessToken) {
        if (accessToken != null) {
            deleteUser(accessToken);
        } else {
            given().spec(defaultRestSpecification())
                    .when()
                    .delete(AUTH_USER_HANDLE);
        }
    }
}
