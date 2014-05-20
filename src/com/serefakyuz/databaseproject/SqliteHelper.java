package com.serefakyuz.databaseproject;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class SqliteHelper extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "person_db";

	// Contacts table name
	private static final String TABLE_PERSON = "personal_information_tb";

	// Contacts Table Columns names
	private static final String KEY_PERSON_ID = "id";
	private static final String KEY_NAME = "name";

	private Context mContext;

	public SqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PERSON + "(" + KEY_PERSON_ID + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);

		// Create tables again
		onCreate(db);
	}

	// Add new person
	public void insertPerson(Person person) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PERSON_ID, person.getID());
		values.put(KEY_NAME, person.getName());

		// Inserting Row
		try {
			db.insert(TABLE_PERSON, null, values);
			Toast.makeText(mContext, mContext.getString(R.string.saved_successfully), Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Toast.makeText(mContext, mContext.getString(R.string.failed_saving), Toast.LENGTH_LONG).show();
		} finally {
			db.close(); // Closing database connection
		}
	}

	public void insertPeople(List<Person> people) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values;
		for (int i = 0; i < people.size(); i++) {
			values = new ContentValues();
			values.put(KEY_PERSON_ID, people.get(i).getID());
			values.put(KEY_NAME, people.get(i).getName());
			db.insert(TABLE_PERSON, null, values);
		}
		db.close();
	}

	// Getting single contact
	Person getPersonViaID(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PERSON, new String[] { KEY_PERSON_ID, KEY_NAME }, KEY_PERSON_ID + "=?", new String[] { id }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		Person person = new Person();
		person.setID(cursor.getString(0));
		person.setName(cursor.getString(1));
		cursor.close();
		db.close();
		// return contact
		return person;
	}

	// Get All People
	public List<Person> getAllPeople() {
		List<Person> allPeople = new ArrayList<Person>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PERSON;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Person person = new Person();
				person.setID(cursor.getString(0));
				person.setName(cursor.getString(1));
				allPeople.add(person);
			} while (cursor.moveToNext());
		}

		return allPeople;
	}

	/**
	 * @param person
	 * @return
	 */
	public Person getLastInsertedPerson() {
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_PERSON;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		Person person = null;
		if(cursor.getCount()>0){
			cursor.moveToPosition(cursor.getCount() - 1);
			person = new Person();
			person.setID(cursor.getString(0));
			person.setName(cursor.getString(1));
		}

		return person;
	}

	/**
	 * Updating single person
	 * 
	 * @param person
	 * @return
	 */
	public int updatePerson(Person person) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PERSON_ID, person.getID());
		values.put(KEY_NAME, person.getName());

		// updating row
		int updatedRow = db.update(TABLE_PERSON, values, KEY_PERSON_ID + " = ?", new String[] { person.getID() });
		db.close();
		return updatedRow;
	}

	// Delete single Person
	public void deletePerson(Person person) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PERSON, KEY_PERSON_ID + " = ?", new String[] { person.getID() });
		db.close();
	}

	// Getting people Count
	public int getPeopleCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PERSON;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}

}
