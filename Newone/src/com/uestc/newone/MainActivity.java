package com.uestc.newone;

import com.uestc.newone.db.DatabaseHelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	private static final String SHARED_PREFS_NAME_COUNT = "job_count_preferences";
	private static final String SHARED_PREFS_JOB_COUNT = "job_count";
	
	private Button increaseTime;
	private Button decreaseTime;
	private Button start;
	private TextView messageLable;
	private TextView numberLable;
	private TextView timeLable;
	private Spinner jobSpinner;
	
	public static DatabaseHelper jobData;
	
	private int jobTime = 0;
	private int jobCount = 0;
	private CountDownTimer jobCountDownTimer;
	private boolean isDoing = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		increaseTime = (Button)findViewById(R.id.time_up);
		decreaseTime = (Button)findViewById(R.id.time_down);
		start = (Button)findViewById(R.id.start);
		messageLable = (TextView)findViewById(R.id.message);
		numberLable = (TextView)findViewById(R.id.number);
		timeLable = (TextView)findViewById(R.id.time);
		jobSpinner = (Spinner)findViewById(R.id.job_spinner);
		jobData = new DatabaseHelper(this);

		messageLable.setText(R.string.message);
		//numberLable.setText(R.string.number_original);
		timeLable.setText(R.string.time_original);
		increaseTime.setText(R.string.time_up);
		decreaseTime.setText(R.string.time_down);
		start.setText(R.string.start);

		//band on the listener
		increaseTime.setOnClickListener(new timeUpButtonListener());
		decreaseTime.setOnClickListener(new timeDownButtonListener());
		start.setOnClickListener(new startButtonListener());
		jobSpinner.setOnItemSelectedListener(new jobSlectedListener());
		
		//add some default work time
		if(jobData.count() == 0){
			jobData.insert("read", 2);
			jobData.insert("write", 3);
			jobData.insert("program", 3);
			jobData.insert("learn", 4);
			jobData.insert("play", 1);
		}
		
		//set up Spinner band to the data
		//适配器的模式。。。
		Cursor cursor = jobData.all(this);
		
		SimpleCursorAdapter jobCursorAdapter = new SimpleCursorAdapter(
			this, android.R.layout.simple_spinner_item, cursor, 
			new String[] {jobData.NAME}, new int[] {android.R.id.text1}	);
		
		jobSpinner.setAdapter(jobCursorAdapter);
		jobCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME_COUNT, MODE_PRIVATE);
		jobCount = sharedPreferences.getInt(SHARED_PREFS_JOB_COUNT, 0);
		numberLable.setText(String.valueOf(jobCount));
		
	}
	

	//这种模式也。。。
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.menu_add_jobs:
			Intent intent = new Intent(this, AddJobsActivity.class);
			startActivity(intent);
			return true;
			
		case R.id.menu_clear_count:
			jobCount = 0;
			numberLable.setText(String.valueOf(jobCount));

			SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS_NAME_COUNT, MODE_PRIVATE).edit();
			editor.putInt(SHARED_PREFS_JOB_COUNT, jobCount);
			editor.commit();
			return true;
			
		case R.id.menu_delete_jobs:
			Intent intent2 = new Intent(this, DeleteJobsActivity.class);
			startActivity(intent2);
			return true;
			
		default:
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	//监听器的模式。。。
	public class jobSlectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> spinner, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Cursor cursor = (Cursor)spinner.getSelectedItem();
			
			jobTime = cursor.getInt(2);
			timeLable.setText(String.valueOf(jobTime) + "m");
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}

	public class timeUpButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			jobTime = jobTime + 1;
			timeLable.setText(String.valueOf(jobTime) + "m");
		}


	}

	public class timeDownButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			jobTime = jobTime - 1;
			timeLable.setText(String.valueOf(jobTime) + "m");
		}

	}

	public class startButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(isDoing){
			    if(jobCountDownTimer != null)
			        jobCountDownTimer.cancel();

			    isDoing = false;
			    start.setText(R.string.start);
			    timeLable.setText(R.string.time_original);
	
			}
			else
			{
				jobCountDownTimer = new CountDownTimer(jobTime * 60 * 1000, 1000){

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						isDoing = false;
						jobCount = jobCount + 1;
						numberLable.setText(String.valueOf(jobCount));
						
						SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS_NAME_COUNT, MODE_PRIVATE).edit();
						editor.putInt(SHARED_PREFS_JOB_COUNT, jobCount);
						editor.commit();

						timeLable.setText(R.string.done);
						start.setText(R.string.start);
						jobTime = 0;

					}   

					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub
						timeLable.setText(String.valueOf(millisUntilFinished / 1000) + "s");

					}   

				};   
				
				jobCountDownTimer.start();
				isDoing = true;
				start.setText(R.string.stop);

			}
	

		}

	}
}
