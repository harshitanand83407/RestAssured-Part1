package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {

	//after executing post addbook make sure to change the value of addbook like book and isbn
	@Test(dataProvider="BooksData")
	public void addBook(String isbn,String aisle)
	{
		RestAssured.baseURI="http://216.10.245.166";
		String response = given().header("Content-Type","application/json").body(payload.Addbook(isbn,aisle))
				.when()
				.post("/Library/Addbook.php")
				.then().assertThat().statusCode(200).extract().response().asString();
				JsonPath js = ReusableMethods.rawToJson(response);
				String id = js.get("ID");
				System.out.println(id);
				
				//deletebook			
				
	}
	@DataProvider(name="BooksData")
	public Object[][] getData()
	{   
		//array= coollection of elements;
		//multidimensional arrays = collection of arrays;
		return new Object[][] {{"isbn","9080"},{"huuu","2564"},{"dc","2435"}};
	}

}
