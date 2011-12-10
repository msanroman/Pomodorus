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


public class PomodoroActivity extends Activity implements android.view.View.OnClickListener, OnItemClickListener{
    
    private TaskDbAdapter dbHelper = new TaskDbAdapter(this);
    private static boolean START = true;
    private static boolean STOP = false;
    private static int POMODORO = 25*60;
    private static int POMODOROTEST = 10;
    private boolean isStarting = START;
    private TimeThread timeThread;
    private int remainingTime = 0;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    long id;
    String name;
    String description;
    int totalPomodoros;
    int remainingPomodoros;
    private static final CharSequence[] items = { "Twittejar últim pomodoro",
        "Començar descans",
        "Extendre pomodoros"
    };
    
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
        this.remainingPomodoros = getIntent().getExtras().getInt("remainingPomodoros");
        remainingPomodoros.setText(String.valueOf(this.remainingPomodoros));
        
        View button = findViewById(R.id.PomodoroButton);
        button.setOnClickListener(this);
    }

    public void onClick(View v) {

            if(isStarting) {
                startPomodoro();
                ((TextView) v).setText("Stop pomodoro");
                isStarting = STOP;
            }
            else {
                this.runOnUiThread(stopPomodoro);
                ((TextView) v).setText("Start pomodoro");
                isStarting = START;
            }
    }

    private Runnable stopPomodoro = new Runnable() {

    	public void run() {
	        ((TextView) findViewById(R.id.PomodoroButton)).setText("Start pomodoro");
	        isStarting = START;
	        timeThread.setRunning(false);
	        mediaPlayer.stop();
    	}
    };

    private void startPomodoro() {

        ProgressBar pomoProgress = (ProgressBar) findViewById(R.id.progressBar1);
        timeThread = new TimeThread(this, pomoProgress, POMODOROTEST);
        if (remainingTime > 0)
            timeThread.setTime(remainingTime);
        timeThread.setRunning(true);
        timeThread.start();
        mediaPlayer = MediaPlayer.create(this, R.raw.tictac);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private CharSequence getTaskAttribute(String attribute) {

        CharSequence attributeValue = getIntent().getExtras().getCharSequence(attribute);
        return attributeValue;
    }

    public void onTimeOut() {
        
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
            dbHelper.updateTask(id, name, description, totalPomodoros, remainingPomodoros);
            dbHelper.close();
            remainingPomodorosView.setText(String.valueOf(remainingPomodoros));
        }
    };

    private Runnable showPomodoroIsOverDialog = new Runnable() {

        public void run() {

            AlertDialog.Builder pomodoroOverMenu = new AlertDialog.Builder(PomodoroActivity.this);
            pomodoroOverMenu.setTitle("Pomodoro is over!");
            pomodoroOverMenu.setItems(items, new android.content.DialogInterface.OnClickListener() {
                
                public void onClick(DialogInterface dialog, int item) {
                    
                    switch(item) {
                        case 0:
                        	Intent intent = new Intent(getBaseContext(), TweetPomodoro.class);
                        	intent.putExtra("name", name);
                        	intent.putExtra("pomoactual", totalPomodoros-remainingPomodoros);
                        	startActivity(intent);
                            break;
                        case 1:
                            showBreakDialog();
                            break;
                        case 2:
                        	showPomoExtenderDialog();
                        	break;
                    }
                }

            });
            
            AlertDialog ad = pomodoroOverMenu.create();
            ad.show();
        }
    };
    
    private void showPomoExtenderDialog() {

        Toast.makeText(PomodoroActivity.this,"Not implemented yet.", 1).show();
    }

    private void showBreakDialog() {
        
    	Toast.makeText(PomodoroActivity.this,"Not implemented yet.", 1).show();
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

}
