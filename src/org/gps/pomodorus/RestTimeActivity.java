package org.gps.pomodorus;

import org.gps.databases.TaskDbAdapter;
import org.gps.twitter.AppTwitter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RestTimeActivity extends Activity {

	private static int RESTTIMETEST = 5;
	private static int RESTTIME = 0;
	private TaskDbAdapter dbHelper = new TaskDbAdapter(this);
	private TimeThread timeThread;
	protected MediaPlayer restTicking;
	protected MediaPlayer alarmRinging;
	private int pomoActual;
	private String tweetName;
	private long taskId;
	private boolean pomodoroTerminat;
	private static final CharSequence[] items = { "Twittejar últim pomodoro",
		"Tornar a fer un pomodoro", "Finalitzar tasca", "Tornar al menú" };
	
	private static final CharSequence[] times = { "5 minuts", "15 minuts" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rest);
		Bundle extras = getIntent().getExtras();
		taskId = extras.getLong("taskId");
		tweetName = extras.getString("name");
		pomoActual = extras.getInt("pomoactual");
		pomodoroTerminat = extras.getBoolean("terminat");
		AlertDialog.Builder pomodoroOverMenu = new AlertDialog.Builder(
				RestTimeActivity.this);
		pomodoroOverMenu.setTitle("Pomodoro is over!");
		pomodoroOverMenu.setItems(times,
				new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int item) {

						int temps = 0;
						switch (item) {
						case 0:
							temps = 5;
							break;
						case 1:
							temps = 15;
							break;
						}
						setUpTime(temps);
					}
		});
		
		AlertDialog ad = pomodoroOverMenu.create();
		ad.show();
	}

	private void setUpTime(int temps) {
		
		RESTTIMETEST = temps;
		RESTTIME = temps*60;
		ProgressBar pomoProgress = (ProgressBar) findViewById(R.id.RestProgressBar);
		timeThread = new TimeThread(this, pomoProgress, RESTTIMETEST);
		timeThread.setRunning(true);
		timeThread.start();
		restTicking = MediaPlayer.create(RestTimeActivity.this, R.raw.tictac);
		restTicking.start();
	}

	public void onTimeOut() {

		this.runOnUiThread(stopRest);
		this.runOnUiThread(showRestIsOverDialog);
	}

	private void finishTask() {
		
		dbHelper.open();
		dbHelper.finishTask(taskId);
		dbHelper.close();
	}

	private Runnable stopRest = new Runnable() {

		public void run() {
			timeThread.setRunning(false);
			restTicking.stop();
			alarmRinging = MediaPlayer.create(RestTimeActivity.this,
					R.raw.clock_ringing);
			alarmRinging.start();
		}
	};
	
	private Runnable showRestIsOverDialog = new Runnable(){
		
		public void run() {
			
			AlertDialog.Builder pomodoroOverMenu = new AlertDialog.Builder(
					RestTimeActivity.this);
			pomodoroOverMenu.setTitle("S'ha acabat el descans!");
			pomodoroOverMenu.setItems(items,
					new android.content.DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int item) {

							switch (item) {
							case 0:
								if (pomodoroTerminat)
									finishTask();
								if(AppTwitter.Conectat()) {
									Intent tweetPomodoro = new Intent(
											getBaseContext(), TweetPomodoro.class);
									tweetPomodoro.putExtra("name", tweetName);
									tweetPomodoro
											.putExtra("pomoactual", pomoActual);
									startActivity(tweetPomodoro);
								}
								else {

									Toast.makeText(RestTimeActivity.this, "You are not logged in!", 1).show();
								}
								break;
							case 1:
								finish();
								break;
							case 2:
								finishTask();
								Intent menuPrincipal = new Intent(getBaseContext(),
										GPSActivity.class);
								startActivity(menuPrincipal);
								finish();
								break;
							case 3:
								if (pomodoroTerminat)
									finishTask();
								Intent menu = new Intent(getBaseContext(),
										GPSActivity.class);
								startActivity(menu);
								finish();
								break;
							}
						}

					});

			AlertDialog ad = pomodoroOverMenu.create();
			ad.show();			
		}
	};

}