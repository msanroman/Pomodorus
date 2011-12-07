package org.gps.twitter;

import android.net.Uri;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class AppTwitter {
	private static Twitter mTwitter = null;
	private static RequestToken mRequestToken = null;
	private static String CallBackUrl = "";
	private static String Verifier = ""; 
	private static String Usuari = "";
	private static boolean Conectat = false;
	
	public static void Configurar(String Ck, String Cs, String CbUrl) {
		ConfigurationBuilder confbuilder = new ConfigurationBuilder();
        confbuilder.setOAuthConsumerKey(Ck);
        confbuilder.setOAuthConsumerSecret(Cs);
        Configuration conf = confbuilder.build(); 
		mTwitter = new TwitterFactory(conf).getInstance();
        mTwitter.setOAuthAccessToken(null);
	    mRequestToken = null;
		CallBackUrl = CbUrl;
		Verifier = "";
		Usuari = "";
		Conectat = false;
	}
	
	public static String ObtenirUrlAutoritzacio() throws TwitterException {
		mRequestToken = mTwitter.getOAuthRequestToken(CallBackUrl);
		return mRequestToken.getAuthorizationURL();
	}
	
	public static void Autoritzar(String Url) throws TwitterException {
		Uri uri = Uri.parse(Url);
		Verifier = uri.getQueryParameter("oauth_verifier");
		mTwitter.getOAuthAccessToken(mRequestToken, Verifier);
		Usuari = mTwitter.getScreenName();
		Conectat = true;
	}
	
	public static void Desautoritzar() {
		Verifier = "";
		mTwitter.setOAuthAccessToken(null);
		Usuari = "";
		Conectat = false;
	}
	
	public static boolean Conectat() {
		return Conectat;
	}
	
	public static String ObtenirNomUsuari() {
		return Usuari;
	}
	
	public static void PublicarTweet(String t) throws TwitterException {
		mTwitter.updateStatus(t);
	}
}
