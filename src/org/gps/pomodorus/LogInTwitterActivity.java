package org.gps.pomodorus;

import org.gps.twitter.AppTwitter;

import twitter4j.TwitterException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class LogInTwitterActivity extends Activity{
	private String ConsumerKey = "2audUQ4EasGSnr4JMlzw";
	private String ConsumerSecret = "h6S6ayEnyqewiKUR1jT1ESgupl1t75o9jQa4akMztq4";
	private String CallBackUrl = "myapp://twitter";
	private TextView missatge;
	private TextView usuariTwitter;
	private TextView estatTwitter;
	private WebView web;
	
	private class WebTwitterClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith(CallBackUrl)) {
				view.clearView();
				view.setVisibility(View.GONE);
				if (!url.contains("denied")) {
					try {
						AppTwitter.Autoritzar(url);
					} catch (TwitterException e) {}
					usuariTwitter.setText(AppTwitter.ObtenirNomUsuari());
					estatTwitter.setText("Conectat");
					missatge.setText("Log in correcte!");
				}
				else missatge.setText("Log in Incorrecte");
				return true;
			}
			return super.shouldOverrideUrlLoading(view, url);
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.logintwitter);
        missatge = (TextView)findViewById(R.id.Missatge);
        usuariTwitter = (TextView)findViewById(R.id.Usuari);
        estatTwitter = (TextView)findViewById(R.id.Estat);
        web = (WebView)findViewById(R.id.webView);
        web.requestFocus(View.FOCUS_DOWN);
        web.setWebViewClient(new WebTwitterClient());
        if (!AppTwitter.Conectat()) {
        	AppTwitter.Configurar(ConsumerKey, ConsumerSecret, CallBackUrl);
            usuariTwitter.setText("");
            estatTwitter.setText("Desconectat");	
        }
        else {
        	usuariTwitter.setText(AppTwitter.ObtenirNomUsuari());
        	estatTwitter.setText("Conectat");
        }
    }
    
    public void onOauthTwitterClick(View button) {
    	if (!AppTwitter.Conectat()) {
    		web.clearView();
    		web.setVisibility(View.VISIBLE);
    		try {
        		web.loadUrl(AppTwitter.ObtenirUrlAutoritzacio());
			} catch(TwitterException e) {}
       	}
    	else missatge.setText("Ja estas logejat!");
    }
    
    public void onEsborrarCredencialsClick(View button) {
    	if (AppTwitter.Conectat()) {
	    	AppTwitter.Desautoritzar();
		    usuariTwitter.setText("");
		    estatTwitter.setText("Desconectat");
		    missatge.setText("Credencials esborrades!");
    	}
    	else missatge.setText("No estas logejat!");
    }
}