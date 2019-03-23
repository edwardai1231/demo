import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FakeServer {
    public void getMethod(String url) throws IOException {
        URL restURL = new URL(url);
     
        HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
     
        conn.setRequestMethod("GET"); 
        conn.setRequestProperty("Accept", "application/json");
     
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while((line = br.readLine()) != null ){
          System.out.println(line);
        }
     
        br.close();
    }
    public void postMethod(String url, String query) throws IOException {
        URL restURL = new URL(url);
     
        HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
     
        PrintStream ps = new PrintStream(conn.getOutputStream());
        ps.print(query);
        ps.close();
     
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while((line = br.readLine()) != null ){
          System.out.println(line);
        }
     
        br.close();
      }
    public void updateMethod(String url, String query) throws IOException {
        URL restURL = new URL(url);
     
        HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        
        PrintStream ps = new PrintStream(conn.getOutputStream());
        ps.print(query);
        ps.close();
     
        InputStream in = conn.getInputStream();
        InputStreamReader input = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(input);
        String line;
        while((line = br.readLine()) != null ){
          System.out.println(line);
        }
     
        br.close();
      }
	
	public static void main(String [] args) {
		String url1 = "http://localhost:3000/posts?";
		String url2 = "http://localhost:3000/posts";
		String url3 = "http://localhost:3000/posts/101";
		String post1 = "id_gte=44&id_lte=63";
//		String post2 = "userId=1&id=2";
//		String post3 = "userId=1&id=101";
//		String post4 = "id=101";
		String str1 = "{\"userId\": 1,\"id\": 101," 
					+ "\"title\": \"NYC\","
					+ " \"body\": \"Common Energy\"}"; 
		String str2 = "{\"userId\": 1,\"id\": 101," 
					+ "\"title\": \"NYC\","
					+ " \"body\": \"NYC\"}"; 
		FakeServer server = new FakeServer();
		try {
			server.getMethod(url1.concat(post1));   //get 20 posts in the table
//			server.postMethod(url2, str1);  //create new post
//			server.updateMethod(url3, str2);  //update existing post
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
