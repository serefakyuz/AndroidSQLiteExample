package com.serefakyuz.databaseproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView twRecordCount;
	
	// Ekleme ve güncellemede kullanılacak arayüz bileşenleri
	EditText etTC;
	EditText etName;
	Button btnSave;
	Button btnUpdate;
	Button btnDelete;
	

	// Son güncellenen kaydın gösterilmesinde kullanılacak arayüz bileşenleri
	TextView twTC;
	TextView twName;
	Button btnShowLastUpdated;
	

	
	OnClickListener clickListener;
	SqliteHelper mSqliteHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSqliteHelper = new SqliteHelper(this);
		clickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String TC = "";
				String name = "";
				switch (v.getId()) {
				case R.id.buttonSave:
					TC = etTC.getText().toString();
					name = etName.getText().toString();
					if(TC.equals("") || name.equals("")){
						Toast.makeText(MainActivity.this, getString(R.string.fill_all_fields), Toast.LENGTH_LONG).show();
					}else{
						Person person = new Person();
						person.setID(TC);
						person.setName(name);
						mSqliteHelper.insertPerson(person);
					}
					break;
					
				case R.id.buttonUpdate:
					TC = etTC.getText().toString();
					name = etName.getText().toString();
					if(TC.equals("") || name.equals("")){
						Toast.makeText(MainActivity.this, getString(R.string.update_warning), Toast.LENGTH_LONG).show();
					}else{
						Person person = new Person();
						person.setID(TC);
						person.setName(name);
						mSqliteHelper.updatePerson(person);
					}
					break;
					
				case R.id.buttonDelete:
					TC = etTC.getText().toString();
					if(TC.equals("")){
						Toast.makeText(MainActivity.this, getString(R.string.delete_warning), Toast.LENGTH_LONG).show();
					}else{
						Person person = new Person();
						person.setID(TC);
						person.setName(name);
						mSqliteHelper.deletePerson(person);
					}
					break;

				case R.id.buttonShowLastUpdated:

					final Person person = mSqliteHelper.getLastUpdatedPerson();
					if(person != null){
								twTC.setText("TC: " + person.getID());
								twName.setText("İsim: " + person.getName());								
							
					}else{
						Toast.makeText(MainActivity.this, getString(R.string.no_records_to_show), Toast.LENGTH_LONG).show();
					}
					break;
					
					
					
				default:
					break;
				}
				twRecordCount.setText("Kayıt Sayısı: " + Integer.toString(mSqliteHelper.getPersonsCount()));
			}
		};
		initViews();
		
	}
	
	private void initViews(){

		twRecordCount = (TextView)findViewById(R.id.textViewRecordCount);
		twRecordCount.setText("Kayıt Sayısı: " + Integer.toString(mSqliteHelper.getPersonsCount()));
		
		etTC = (EditText)findViewById(R.id.editTextId);
		etName = (EditText)findViewById(R.id.editTextName);
		btnSave = (Button)findViewById(R.id.buttonSave);
		btnUpdate = (Button)findViewById(R.id.buttonUpdate);
		btnDelete = (Button)findViewById(R.id.buttonDelete);

		twTC = (TextView)findViewById(R.id.textViewTC);
		twName = (TextView)findViewById(R.id.textViewName);
		btnShowLastUpdated = (Button)findViewById(R.id.buttonShowLastUpdated);
		
		btnSave.setOnClickListener(clickListener);
		btnUpdate.setOnClickListener(clickListener);
		btnDelete.setOnClickListener(clickListener);
		btnShowLastUpdated.setOnClickListener(clickListener);
	}

}
