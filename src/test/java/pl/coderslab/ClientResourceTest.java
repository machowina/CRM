package pl.coderslab;

import java.io.IOException;
import java.io.InputStream;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.internal.util.IOUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@TestPropertySource("/test.properties")
@SpringBootTest(classes = { CrmApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ClientResourceTest {

	@LocalServerPort
	private int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void should_create_client() throws IOException {
		// given
		byte[] body = loadResource("/Client.json");
		RequestSpecification request = RestAssured.given().body(body)
				.header(new Header("content-type", "application/json"));
		// when
		Response response = request.post(getMailEndpoint());
		// then
		response.then().statusCode(200);
	}

	@Test
	public void should_return_created_user() throws IOException {
		// given
		byte[] body = loadResource("/Client.json");
		String id = RestAssured.given().body(body).header(new Header("content-type", "application/json"))
				.post(getMailEndpoint()).getBody().print();
		// when
		Response response = RestAssured.given().auth().basic("kowalski@gmail.com", "pass").get(getIdEndpoint() + id);
		// then
		response.then().statusCode(200).body("name", Matchers.equalTo("Coderslab"))
				.body("status", Matchers.equalTo("lead"))
				.body("contactPerson.lastname", Matchers.equalTo("Nowak"))
				.body("address.city", Matchers.equalTo("Warsaw"));

	}

	private String getIdEndpoint() {
		return "/clients/";
	}
	private String getMailEndpoint() {
		return "/clients/test@mail.pl";
	}

	private byte[] loadResource(String resourcePath) throws IOException {
		InputStream resourceAsStream = this.getClass().getResourceAsStream(resourcePath);
		return IOUtils.toByteArray(resourceAsStream);
	}

}
