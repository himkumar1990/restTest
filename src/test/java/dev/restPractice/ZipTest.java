package dev.restPractice;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ZipTest {
	@BeforeSuite
	public void setBaseUri() {
		RestAssured.baseURI = "https://api.zippopotam.us/";
	}

	@Test(enabled = true, dataProvider = "stateByZip")
	public void testPlaceByZip(String zip, String state) {
		RequestSpecification request = RestAssured.given().pathParam("zip", zip);
		Response response = request.get("/us/{zip}");
		System.out.println(response.asString());
		response.then().assertThat().statusCode(200).and().statusLine("HTTP/1.1 200 OK").and().assertThat()
				.body("places.state[0]", Matchers.equalTo(state)).and().assertThat()
				.header("Content-Type", "application/json").and().assertThat().time(Matchers.lessThan(3000L));

	}

	@DataProvider(name = "stateByZip")
	public String[][] getStateByZip() {
		return new String[][] { { "90001", "California" }, { "19701", "Delaware" }, { "06001", "Connecticut" } };
	}
}
