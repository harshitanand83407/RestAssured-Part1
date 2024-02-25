package files;
import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
public class JiraTest {
	public static void main(String[] args)
	{
		RestAssured.baseURI="http://localhost:8080";
		//login
		SessionFilter session = new SessionFilter();
		String response = given().header("Content-Type","application/json").
		body("{ \"username\": \"harshitanand\", \"password\": \"H@rshit01\" }").log().all()
		.filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
		//add comment
		String expectedMessage = "Hi!, How are you";
		String addCommentResponse = given().pathParam("key","10008").log().all().header("Content-Type","application/json")
				
		.body("{\r\n"
				+ "    \"body\":\""+expectedMessage+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\":\"role\",\r\n"
				+ "        \"value\":\"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("/rest/api/2/issue/{key}/comment").then().log().all().assertThat().
		            statusCode(201).extract().response().asString();
		
		JsonPath js = new JsonPath(addCommentResponse);
		String commentId = js.getString("id");
		//add attachement
		// you can also do it as jira.txt no file location as it is on project level
		given().header("X-Atlassian-Token","no-check").filter(session).pathParam("key","10008").multiPart("file",new File("C:\\Users\\hanand\\eclipse-workspace\\DemoRestAssured\\Jira.txt")).when()
		.post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
		
		//Get Issue
		String issueDetails = given().filter(session).pathParam("key","10008").queryParam("fields","comment").when().get("/rest/api/2/issue/{key}").then()
		.log().all().extract().response().asString();
		System.out.println(issueDetails);
		
		JsonPath js1 = new JsonPath(issueDetails);
		int commentsCount = js1.getInt("fields.comment.comments.size()");
		for(int i=0;i<commentsCount;i++)
		{
			String commentIdIssue = js1.get("fields.comment.comments["+i+"].id").toString();
			if(commentIdIssue.equalsIgnoreCase(commentId))
			{
				String message = js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message,expectedMessage);
			}
		}
	}

}
