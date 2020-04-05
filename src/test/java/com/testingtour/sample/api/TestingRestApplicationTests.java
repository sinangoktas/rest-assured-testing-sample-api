package com.testingtour.sample.api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(
	classes = TestingRestApplication.class,
	webEnvironment = WebEnvironment.RANDOM_PORT
)
class TestingRestApplicationTests {

	@LocalServerPort
	private int port;

	private String uri = "http://localhost:%s/api/items";

	private static boolean initialized = false;

	@BeforeEach
	public void setup() {
		// port is autowired so we cannot use port from a static method
		if (!initialized) {
			baseURI = String.format(uri, port);
			initialized = true;
		}
	}

	@Test
	public void givenGetItemById_whenValidId_ThenSuccess() {

		// this needs to match the record with id 1 in the database, inserted in test/resources/data.sql
		HashMap<String, Object> expected = getRecordHashMapWithId(1, 5, "Apples", "Fruits", 2.50f);

		HashMap<String, Object> actual = given().
			pathParam("id", expected.get("id")).
		when().
			get("/{id}").
		then().
			statusCode(HttpStatus.OK.value()).
			contentType(ContentType.JSON).
			extract().path("$");

		assertThat(expected).isEqualTo(actual);

	}

	@Test
	public void givenGetItemById_whenInvalidNumericId_ThenReturnNotFound() {

		String invalidId = "500";

		given().
			pathParam("id", invalidId).
		when().
			get("/{id}").
		then().
			statusCode(HttpStatus.NOT_FOUND.value());

	}

	@Test
	public void givenGetItemById_whenValidStringId_ThenReturnBadRequest() {

		String invalidId = "invalid";

		given().
			pathParam("id", invalidId).
		when().
			get("/{id}").
		then().
			statusCode(HttpStatus.BAD_REQUEST.value()).
			body("error", equalTo("Bad Request")).
			body("message", equalTo("Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: \"invalid\""));

	}

	@Test
	public void givenCreateItem_whenValidRecord_ThenSuccess() {

		// this will create a new record in the Item table
		HashMap<String, Object> newRecord = getRecordHashMap(20, "Steak", "Meats", 15.5f);

		// create a new record and extract the ID from the response
		Integer newId = given().
			contentType(ContentType.JSON).
			body(newRecord).
		when().
			post("/").
		then().
			statusCode(HttpStatus.CREATED.value()).
			contentType(ContentType.JSON).
			extract().
			path("id");

		newRecord = setRecordId(newRecord, newId);

		// verify that the new record is created
		HashMap<String, Object> actual = given().
			pathParam("id", newId).
		when().
			get("/{id}").
		then().
			statusCode(HttpStatus.OK.value()).
			contentType(ContentType.JSON).
			extract().path("$");

		assertThat(newRecord).isEqualTo(actual);

	}

	@Test
	public void givenUpdateItem_whenValidRecord_ThenReturnSuccess() {

		HashMap<String, Object> record = getRecordHashMapWithId(5, 3, "Chicken", "Meats", 8.50f);

		// check the original record
		HashMap<String, Object> actualOriginalRecord = given().
			pathParam("id", record.get("id")).
		when().
			get("/{id}").
		then().
			statusCode(HttpStatus.OK.value()).
			contentType(ContentType.JSON).
			extract().path("$");

		assertThat(record).isEqualTo(actualOriginalRecord);

		// update the record with id 5 in the Item table
		// we're changing the count from 3 to 20
		record.put("count", 20);
		// we're changing the price from 8.50 to 10.50
		record.put("price", 10.50f);

		given().
			contentType(ContentType.JSON).
			pathParam("id", record.get("id")).
			body(record).
		when().
			put("/{id}").
		then().
			statusCode(HttpStatus.OK.value()).
			contentType(ContentType.JSON);

		// verify that the record is updated
		HashMap<String, Object> actualUpdatedRecprd = given().
			pathParam("id", record.get("id")).
		when().
			get("/{id}").
		then().
			statusCode(HttpStatus.OK.value()).
			contentType(ContentType.JSON).
			extract().path("$");

		assertThat(record).isEqualTo(actualUpdatedRecprd);

	}

	@Test
	public void givenDeleteRecord_whenValidId_ThenSuccess() {

		String deletedId = "2";

		// delete record with id = 5
		given().
			pathParam("id", deletedId).
		when().
			delete("/{id}").
		then().
			statusCode(HttpStatus.OK.value());

		// verify that the record has been deleted
		given().
			pathParam("id", deletedId).
		when().
			get("/{id}").
		then().
			statusCode(HttpStatus.NOT_FOUND.value());

	}

	private HashMap<String, Object> getRecordHashMap(int count, String name, String type, float price) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("price", price);
		hashMap.put("name", name);
		hashMap.put("count", count);
		hashMap.put("type", type);
		return hashMap;
	}

	private HashMap<String, Object> getRecordHashMapWithId(Integer id, int count, String name, String type, float price) {
		HashMap<String, Object> hashMap = getRecordHashMap(count, name, type, price);
		return setRecordId(hashMap, id);
	}

	private HashMap<String, Object> setRecordId(HashMap<String, Object> hashMap, Integer id) {
		hashMap.put("id", id);
		return hashMap;
	}

}