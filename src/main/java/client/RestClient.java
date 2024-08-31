package client;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static config.RestConfig.BASE_URI;
import static io.restassured.RestAssured.given;
public abstract class RestClient {
    protected RequestSpecification defaultRestSpecification(){
        return given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON);
    }
}
