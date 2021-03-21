import twitter.App;
import twitter4j.TwitterException;

import java.io.IOException;



public class Main{

	public static void main(String[] args) throws IOException, TwitterException {
		
		App app=new App();
		
		String txt = app.getTexte1();
		
		
		System.out.println(txt);
		app.storeToMongoDb();
	}
}

