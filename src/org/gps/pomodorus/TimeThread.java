package org.gps.pomodorus;

import android.widget.ProgressBar;

public class TimeThread extends Thread {

	private boolean _running = false;
	ProgressBar _timeBar;
	int _currentValue;
	PomodoroActivity _pomodoroMain;
	RestTimeActivity _restMain;

	public TimeThread(PomodoroActivity activity, ProgressBar timeBar,
			int maxValue) {

		_timeBar = timeBar;
		_pomodoroMain = activity;
		_currentValue = maxValue;
		_timeBar.setMax(maxValue);
		_timeBar.setProgress(_currentValue);
	}

	public TimeThread(RestTimeActivity restTimeActivity, ProgressBar timeBar,
			int restTime) {

		_timeBar = timeBar;
		_restMain = restTimeActivity;
		_currentValue = restTime;
		_timeBar.setMax(restTime);
		_timeBar.setProgress(_currentValue);
	}

	public void setRunning(boolean run) {

		_running = run;
	}

	public void setTime(int time) {

		_currentValue = time;
	}

	public int getTime() {

		return _currentValue;
	}

	public void resetTime(int maxValue) {

		_timeBar.setMax(maxValue);
		_currentValue = maxValue;
	}

	@Override
	public void run() {

		long ticksPS = 1000;
		long startTime;
		long sleepTime;
		while (_running) {
			startTime = System.currentTimeMillis();
			--_currentValue;
			_timeBar.setProgress(_currentValue);
			if (_currentValue == 0) {
				_running = false;
				if (_pomodoroMain != null)
					_pomodoroMain.onTimeOut();
				else
					_restMain.onTimeOut();
			}
			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0)
					sleep(sleepTime);
				else
					sleep(10);
			} catch (Exception e) {
			}
		}
	}

}