import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.ReusableMethods;
import files.payload;
public class Basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//validate if Add Place API is working as expected
		//Add place -> Update place with New Address -> Get place to validate if new Address is present in response
		//given - all Input Details
		//when - Submit the API - resoure , Http Method
		//Then - validate the response
     RestAssured.baseURI = "https://rahulshettyacademy.com";
     String response = given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
     .body(payload.AddPlace()).when().post("maps/api/place/add/json").then().assertThat().statusCode(200).body("scope",equalTo("APP"))
                 .header("server","Apache/2.4.52 (Ubuntu)").extract().response().asString(); 
     // removing .log().all() b/w .then and .statuscode as I need only response not status
     
     System.out.println(response);
     // how to parse the value as string
     // jsonpath take input as a string and covert into a JSON
     JsonPath js = new JsonPath(response); // for parsing Json
     //if we have parent - child relation then it can be parsed by putting dot for e.g location.latitude
     String placeId = js.getString("place_id");
     System.out.println(placeId);
     
     //update place
     String newaddress = "Jaypee Aman Noida";
     given().log().all().queryParam("Key","qaclick123").header("Content-Type","application/json")
     .body("{\r\n"
     		+ "\"place_id\":\""+placeId+"\",\r\n"
     		+ "\"address\":\""+newaddress+"\",\r\n"
     		+ "\"key\":\"qaclick123\"\r\n"
     		+ "}").
       when().put("maps/api/place/update/json").then().assertThat().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));
  // get Place

     String getPlaceResponse=	given().log().all().queryParam("key", "qaclick123")
    			.queryParam("place_id",placeId)
    			.when().get("maps/api/place/get/json")
    			.then().assertThat().log().all().statusCode(200).extract().response().asString();
    		//JsonPath js1=new JsonPath(getPlaceResponse);
     JsonPath js1=ReusableMethods.rawToJson(getPlaceResponse);
    		String actualAddress =js1.getString("address");
    		System.out.println(actualAddress);
    	    Assert.assertEquals(actualAddress,newaddress);
	}
	
	


}
