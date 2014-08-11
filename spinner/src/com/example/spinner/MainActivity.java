package com.example.spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.spinner.R;


import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.os.Build;

@SuppressLint("CutPasteId") public class MainActivity extends ListActivity {
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "first_name";
	private static final String TAG_LASTANME = "last_name";
	private static final String TAG_ORGNAME = "org_name";
	private static final String TAG_SPECIALTY = "specialty";
	private static String url = "http://192.168.2.9/test1.php";
	private static final String TAG_CONTACTS = "contacts";
	private static final String TAG_CONTACTS1 = "contacts1";
	JSONArray contacts = null;

	ArrayList<HashMap<String, String>> contactList;
	//private static final String TAG_GENDER = "gender";
	  private Spinner spinner1,spinner2;
	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactList = new ArrayList<HashMap<String, String>>();
        spinner1 = (Spinner) findViewById(R.id.spinner1);
		List<String> list = new ArrayList<String>();
		list.add("My Contacts");
		list.add("All Contacts");
	//	list.add("list1");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
		//spinner2 = (Spinner) findViewById(R.id.spinner2);
		  
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		List<String> list2 = new ArrayList<String>();
		list2.add("All Profiles");
		list2.add("Basic");
		list2.add("Full");
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list2);
		dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter1);
		//spinner2.setAdapter(dataAdapter);
		// Making a request to url and getting response
		//String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST);

		//Log.d("Response: ", "> " + jsonStr);
        new GetContacts().execute();
    }




	public class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
		
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST);
		
			
			if (jsonStr != null) {
				
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					
					// Getting JSON Array node
					contacts = jsonObj.getJSONArray(TAG_CONTACTS);
					
					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);
						
						String id = c.getString(TAG_ID);
						String first_name = c.getString(TAG_NAME)+" "+c.getString(TAG_LASTANME);
					
						String org_name = c.getString(TAG_ORGNAME);
						String specialty = c.getString(TAG_SPECIALTY);
						//String gender = c.getString(TAG_GENDER);

						// Phone node is JSON Object
						//JSONObject phone = c.getJSONObject(TAG_PHONE);
						//String mobile = phone.getString(TAG_PHONE_MOBILE);
						//String home = phone.getString(TAG_PHONE_HOME);
						//String office = phone.getString(TAG_PHONE_OFFICE);

						// tmp hashmap for single contact
						HashMap<String, String> contact = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						//contact.put(TAG_ID, id);
						contact.put(TAG_NAME, first_name);
						
						contact.put(TAG_ORGNAME, org_name);
						contact.put(TAG_SPECIALTY, specialty);
						//Log.d("Respons4: ", "> " + contact);
						// adding contact to contact list
						contactList.add(contact);
						//Log.d("Response11111: ", "> " + contactList);
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			ListAdapter adapter = new SimpleAdapter(
					MainActivity.this, contactList,
					R.layout.list_item, new String[] { TAG_NAME, TAG_ORGNAME,TAG_SPECIALTY
							 }, new int[] { R.id.first_name,
							R.id.org_name,R.id.specialty });

			setListAdapter(adapter);
		}

	}
}
