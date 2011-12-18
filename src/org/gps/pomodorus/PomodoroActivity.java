package org.gps.pomodorus;

import org.gps.databases.TaskDbAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PomodoroActivity extends Activity implements
		android.view.View.OnClickListener, OnItemClickListener {

	private TaskDbAdapter dbHelper = new TaskDbAdapter(this);
	private static boolean START = true;
	private static boolean STOP = false;
	private static int POMODORO = 25 * 60;
	private static int POMODOROTEST = 10;
	private boolean isStarting = START;
	private TimeThread timeThread;
	private int remainingTime = 0;
	private MediaPlayer pomodoroTicking;
	private MediaPlayer alarmRinging;
	private Handler handler = new Handler();
	private boolean finishedPomodoro;
	private long id;
	private String name;
	private String description;
	private int totalPomodoros;
	private int remainingPomodoros;
	private static final CharSequence[] items = { "Twittejar últim pomodoro",
			"Començar descans", "Extendre pomodoros" };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setUpLayoutContent();
	}

	private void setUpLayoutContent() {

		setContentView(R.layout.pomodoro);

		this.id = getIntent().getExtras().getLong("id");

		TextView name = (TextView) findViewById(R.id.TaskName);
		this.name = (String) getTaskAttribute("name");
		name.setText(this.name);

		TextView description = (TextView) findViewById(R.id.TaskDescription);
		this.description = (String) getTaskAttribute("description");
		description.setText(this.description);

		TextView totalPomodoros = (TextView) findViewById(R.id.TotalPomodoros);
		this.totalPomodoros = getIntent().getExtras().getInt("totalPomodoros");
		totalPomodoros.setText(String.valueOf(this.totalPomodoros));

		TextView remainingPomodoros = (TextView) findViewById(R.id.RemainingPomodoros);
		this.remainingPomodoros = getIntent().getExtras().getInt(
				"remainingPomodoros");
		remainingPomodoros.setText(String.valueOf(this.remainingPomodoros));

		View button = findViewById(R.id.PomodoroButton);
		button.setOnClickListener(this);
	}

	public void onClick(View v) {

		if (isStarting) {
			startPomodoro();
			((TextView) v).setText("Stop pomodoro");
			isStarting = STOP;
		} else {
			
			this.runOnUiThread(stopPomodoro);
			((TextView) v).setText("Start pomodoro");
			isStarting = START;
		}
	}

	private Runnable stopPomodoro = new Runnable() {

		public void run() {
			((TextView) findViewById(R.id.PomodoroButton))
					.setText("Start pomodoro");
			isStarting = START;
			timeThread.setRunning(false);
			pomodoroTicking.stop();
			if (finishedPomodoro)
				startRinging();
		}
	};

	private void startPomodoro() {

		ProgressBar pomoProgress = (ProgressBar) findViewById(R.id.progressBar1);
		timeThread = new TimeThread(this, pomoProgress, POMODOROTEST);
		if (remainingTime > 0)
			timeThread.setTime(remainingTime);
		timeThread.setRunning(true);
		timeThread.start();
		pomodoroTicking = MediaPlayer.create(this, R.raw.tictac);
		pomodoroTicking.setLooping(true);
		finishedPomodoro = false;
		pomodoroTicking.start();
	}

	protected void startRinging() {

		alarmRinging = MediaPlayer.create(PomodoroActivity.this,
				R.raw.clock_ringing);
		alarmRinging.start();
	}

	private CharSequence getTaskAttribute(String attribute) {

		CharSequence attributeValue = getIntent().getExtras().getCharSequence(
				attribute);
		return attributeValue;
	}

	public void onTimeOut() {

		finishedPomodoro = true;
		remainingTime = 0;
		this.runOnUiThread(stopPomodoro);
		this.runOnUiThread(decrementRemainingPomodoros);
		this.runOnUiThread(showPomodoroIsOverDialog);
	}

	private Runnable decrementRemainingPomodoros = new Runnable() {

		public void run() {
			TextView remainingPomodorosView = (TextView) findViewById(R.id.RemainingPomodoros);
			--remainingPomodoros;
			dbHelper.open();
			dbHelper.updateTask(id, name, description, totalPomodoros,
					remainingPomodoros);
			dbHelper.close();
			remainingPomodorosView.setText(String.valueOf(remainingPomodoros));
		}
	};

	private Runnable showPomodoroIsOverDialog = new Runnable() {

		public void run() {

			Intent restTime = new Intent(getBaseContext(),
					RestTimeActivity.class);
			restTime.putExtra("taskId", id);
			restTime.putExtra("name", name);
			restTime.putExtra("pomoactual",
					totalPomodoros - remainingPomodoros);
			restTime.putExtra("terminat", isFinished());
			startActivity(restTime);
		}
	};

	private boolean isFinished() {

		return remainingPomodoros == 0;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}
	
	public void onResume() {
		
		super.onResume();
		refreshLayoutContent();
	}

	private void refreshLayoutContent() {

		if (isFinished()) {
			dbHelper.open();
			dbHelper.extendPomodoro(id);
			dbHelper.close();
			++remainingPomodoros;
			++totalPomodoros;
			TextView remainingPomodoros = (TextView) findViewById(R.id.TotalPomodoros);
			remainingPomodoros.setText(String.valueOf(totalPomodoros));
		}
		TextView remainingPomodoros = (TextView) findViewById(R.id.RemainingPomodoros);
		remainingPomodoros.setText(String.valueOf(this.remainingPomodoros));
	}

}