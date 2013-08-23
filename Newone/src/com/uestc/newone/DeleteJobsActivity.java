package com.uestc.newone;

import com.uestc.newone.db.DatabaseHelper;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteJobsActivity extends Activity {
	
	private TextView jobTypeLabel2;
	private Spinner jobSpinner2;
	private Button delete;
	
	private int jobID;
	private String jobType2 = "noting";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deletejobs);
		
		jobTypeLabel2 = (TextView)findViewById(R.id.job_type2);
		jobSpinner2 = (Spinner)findViewById(R.id.job_spinner2);
		delete = (Button)findViewById(R.id.delete);
		
		jobTypeLabel2.setText(R.string.job_type_label);
		delete.setText(R.string.delete);
		
		delete.setOnClickListener(new deleteButtonListener());
		jobSpinner2.setOnItemSelectedListener(new jobSelectListener2());
		
		//Cursor cursor2 = jobData2.all(this);
		Cursor cursor2 = MainActivity.jobData.all(this);

		SimpleCursorAdapter jobCursorAdapter = new SimpleCursorAdapter(
				this, android.R.layout.simple_spinner_item, cursor2, 
				new String[] {MainActivity.jobData.NAME}, new int[] {android.R.id.text1}	);

		jobSpinner2.setAdapter(jobCursorAdapter);
		jobCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}
	
	public class jobSelectListener2 implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> spinner, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Cursor cursor = (Cursor)spinner.getSelectedItem();
			jobID = cursor.getInt(0);
			jobType2 = cursor.getString(1);
			
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class deleteButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int result;
//			Toast.makeText(getApplicationContext(), getString(R.string.delete_job_failed, "nothing"), Toast.LENGTH_SHORT).show();	
			result = MainActivity.jobData.delete(jobID);
//			Toast.makeText(getApplicationContext(), getString(R.string.delete_job_failed, jobType2), Toast.LENGTH_SHORT).show();	
			if(result == 0){
				Toast.makeText(getApplicationContext(), getString(R.string.delete_job_failed, jobType2), Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), getString(R.string.delete_job_success, jobType2), Toast.LENGTH_SHORT).show();
			}


		}

	}

}
