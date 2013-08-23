package com.uestc.newone;

import com.uestc.newone.db.DatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class AddJobsActivity extends Activity {
	
	private TextView jobTypeLabel;
	private TextView jobTimeLabel;
	private TextView jobTimeValue;
	private EditText jobType;
	private SeekBar jobTimeSeekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addjobs);
		
		jobTypeLabel = (TextView)findViewById(R.id.job_type);
		jobTimeLabel = (TextView)findViewById(R.id.job_time);
		jobTimeValue = (TextView)findViewById(R.id.job_time_value);
		jobType = (EditText)findViewById(R.id.edit_job_type);
		jobTimeSeekBar = (SeekBar)findViewById(R.id.job_time_seekbar);
		
		jobTypeLabel.setText(R.string.job_type_label);
		jobTimeLabel.setText(R.string.job_time_label);
		jobTimeValue.setText(R.string.time_original);
		jobTimeSeekBar.setOnSeekBarChangeListener(new JobTimeSeekBarListener());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_addjobs, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.menu_save_jobs:
			if(saveJobs()){
				Toast.makeText(this, getString(R.string.save_job_success, jobType.getText().toString()), Toast.LENGTH_SHORT).show();
				jobType.setText("");
			}

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public boolean saveJobs(){
		String jobTypeText = jobType.getText().toString();
		int jobTimeValue = jobTimeSeekBar.getProgress() + 1;
		
		if(jobTypeText.length() < 2 ){
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle(R.string.invalid_job_title);
			dialog.setMessage(R.string.invalid_job_message);
			dialog.show();
			
			return false;	
		}
		
		DatabaseHelper jobData = new DatabaseHelper(this);
		jobData.insert(jobTypeText, jobTimeValue);
		jobData.close();
		
		return true;
	}

	public class JobTimeSeekBarListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			jobTimeValue.setText((progress + 1) + "m");
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		
	}

}
