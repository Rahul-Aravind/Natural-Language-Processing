import java.io.BufferedWriter;
import java.io.FileWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("deprecation")
public class DataSet {

	public static void getSetA() throws Exception {
		//BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		//int i=Integer.parseInt(br.readLine());

		int categoryId = 396545660;
		String categoryName = "Computers and Internet";
		HttpClient httpclient = new DefaultHttpClient();
		JsonParser parser = new JsonParser();
		
		int n = 1;
		int start_i=100;
		int maxQ = 100000;//-start_i;
		int maxResults=50;
		String service_url = "http://answers.yahooapis.com/AnswersService/V1/getByCategory?appid=h3z5tHvV34GAEDaCKL11OJ9nY3Tj4v6AsTep5T8NTwb.abfi5hCbn9vAWnHTmIUjQQ--&type=resolved&output=json&category_id=";
		String start="&start=";
		String Results="&results=";

		System.out.println(categoryName + ":");
		BufferedWriter out=new BufferedWriter(new FileWriter("C:\\Users\\RahulAravind\\workspace\\java\\QABot\\Dataset\\"+categoryName+".txt", true));

		
		for (int start_ = start_i; start_ < maxQ; start_ += maxResults) {	
			String url = service_url + categoryId + start + start_ + Results+ maxResults;
			System.out.println(url);
	
			HttpResponse response = httpclient.execute(new HttpGet(url));
			System.out.println(response);
			JsonObject json_data = (JsonObject) parser.parse(EntityUtils
					.toString(response.getEntity()));
			json_data=(JsonObject) json_data.get("all");
			JsonArray results = (JsonArray) json_data.get("questions");
	
			if (results != null) {
				for (Object planet : results) {
					Object res = ((JsonObject) planet).get("Subject");
					out.write(res.toString().replace("\"", "") + "\n");
					//System.out.println(res.toString());
				}
			}
		}
		out.close();
		System.out.println("DONE!!!");
	}
	
	public static void main(String args[]) throws Exception {
		getSetA();
	}
}
