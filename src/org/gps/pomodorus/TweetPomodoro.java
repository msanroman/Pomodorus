package org.gps.pomodorus;

import org.gps.twitter.AppTwitter;

import twitter4j.TwitterException;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TweetPomodoro extends Activity{
	private EditText tweettext;
	private TextView caractersrestants;
	private Button tweetbutton;
	private String tweet;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweetpomodoro);
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        int pomo = extras.getInt("pomoactual");
        tweet = "Acabo de finalitzar el pomodoro "+pomo+" de la tasca: "+name+" #Pomodorus";
        tweettext = (EditText)findViewById(R.id.Tweet);
        tweettext.addTextChangedListener(new TextWatcher() {
        	public void afterTextChanged(Editable s) {
        		 int res = 140 - s.toString().length();
        	     caractersrestants = (TextView)findViewById(R.id.caracters);
        	     tweetbutton = (Button)findViewById(R.id.TweetButton);
        	     if (res < 0) tweetbutton.setClickable(false);
        	     if (res >= 0 && !tweetbutton.isClickable()) tweetbutton.setClickable(true);
        		 caractersrestants.setText(String.valueOf(res));
        	}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        tweettext.setText(tweet);
    }
	
	public void onTweetButton(View button) {
		try {
			AppTwitter.PublicarTweet(tweettext.getText().toString());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.finish();
	}
}
