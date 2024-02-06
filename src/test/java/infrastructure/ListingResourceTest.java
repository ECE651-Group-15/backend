package infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ListingResourceTest {
//    @Test
//    void testHelloEndpoint() {
//        given().contentType(ContentType.JSON)
//               .body("{\"title\":\"hello\"}")
//               .when().post("/api/listings/create")
//               .then()
//               .statusCode(200)
//               .body(is("hello"));
//    }
}