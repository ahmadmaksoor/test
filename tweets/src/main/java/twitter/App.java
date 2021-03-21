package twitter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.text.Normalizer;


public class App{
	public static final String SUBJECT = "Jacques Chirac";
	
	/**
	 * constructor to initialize the access to the API
	 * @throws TwitterException
	 */
	public  App() throws TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("AzgK1voXeIWmBFLKa6uMF13C1")
		.setOAuthConsumerSecret("PY1OJwUGOsynIc1zCm8C7f07Zok3Dr7l6jsbH7m7ImBB0dvljb")
		.setOAuthAccessToken("1305467268038635520-mwc0rY3Lf2Zam7mlc049Kp4ZEr011o")
		.setOAuthAccessTokenSecret("v8XEwErX1g2cjEP55v03aZBtLuqF1lHYRETg5Dfn2iqrZ")
		.setTweetModeExtended(true);



	}


	public String filtres(String text) throws TwitterException {

		text = text.toLowerCase();


		Pattern p1 = Pattern.compile("#[-a-z]*");
		Matcher m1 = p1.matcher(text);
		text=m1.replaceAll("");


		text=text.replaceAll("’", " ");


		text=text.replaceAll("«", " ");

		text=text.replaceAll("»", " ");

		text= text.replaceAll("\n", " ");


		text=text.replaceAll("\t"," ");

		Pattern p2 = Pattern.compile("\\p{Punct}");
		Matcher m2 = p2.matcher(text);
		text=m2.replaceAll(" ");


		return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

	}

	public String getTexte1() throws TwitterException, IOException {
		String res = "";

		Twitter twitter = TwitterFactory.getSingleton();
		Query query = new Query(App.SUBJECT);
		query.setLang("fr");
		QueryResult result = twitter.search(query);
		for (Status status : result.getTweets()) {

			String r = status.getText();
			r = filtres(r);

			res+=  r+ "\n" ;

		}

		return res;
	}

	public void  storeToMongoDb() throws TwitterException, IOException {
		
		 String tweets = getTexte1();

	
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		DB db = mongoClient.getDB( "tweets" );
		DBCollection coll = db.getCollection("tweets_collection");
		ArrayList<String> tags = new ArrayList<String>();
		tags.add(tweets);
		BasicDBObject doc = new BasicDBObject("title", "tweets").
		        append("tweet content", tweets);
		coll.insert(doc);
	

	}

}
