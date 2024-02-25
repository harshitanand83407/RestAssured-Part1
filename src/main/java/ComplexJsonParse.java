import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JsonPath js = new JsonPath(payload.CoursePrice());
		//print number of courses returned by API
		int count = js.getInt("courses.size()");
		System.out.println(count);
		//print purchase amount 
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(totalAmount);
		//print title of first course
		String titleFirstCourse = js.get("courses[0].title");
		System.out.println(titleFirstCourse);
		//print all course titles and their respective prices
		for (int i=0;i<count;i++)
		{
			String courseTitles = js.get("courses["+i+"].title");
			//without storing in variable
			System.out.println(js.get("courses["+i+"].price").toString());
			System.out.println(courseTitles);
		}
		System.out.println("Print no. of copies sold by RPA Course");
		
		for (int i=0;i<count;i++)
		{
			String courseTitles = js.get("courses["+i+"].title");
			if(courseTitles.equalsIgnoreCase("RPA"))
			{
				//copies sold
				int copies = js.get("courses["+i+"].copies");
				System.out.println(copies);
				break;
			}
		}
	
	}

}
