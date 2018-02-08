package org.androidtown.mhealthcare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class IndexActivity extends AppCompatActivity{
    public int index = 1; // Using for patient list index
    Context mContext;
    String auth = "";
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listView;
    static String searchInput = "";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "mhealth.db", null, 1);
        dbHelper.checkSize(); // check size of database
        mContext = this;
        final EditText searchBox = (EditText)findViewById(R.id.searchbox);
        Button searchBtn = (Button)findViewById(R.id.searchBtn);
        Button preBtn = (Button)findViewById(R.id.pre);
        Button nextBtn = (Button)findViewById(R.id.next);

        Intent intent = new Intent(this.getIntent());
        final String uID = intent.getStringExtra("user_id");

        TextView userName = (TextView)findViewById(R.id.userName);
        userName.setText(dbHelper.getName(uID)); // Set name of user by getName function

        auth = dbHelper.getAuth(uID); // Call getAuth function and get authority of user from database
        ImageView img = (ImageView)findViewById(R.id.profile);
        if(auth.equals("doctor")) // Set image of user by user authority
            img.setImageResource(R.drawable.doctor);
        else
            img.setImageResource(R.mipmap.nurse);

        //Set ArrayList items for showing patient list
        items = new ArrayList<String>();
        items = dbHelper.getList(index);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        listView = (ListView)findViewById(R.id.patientList);
        listView.setAdapter(adapter);

        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ // User click search button
                searchInput = searchBox.getText().toString();
                items.clear(); // Set list to empty
                adapter.notifyDataSetInvalidated();
                items.addAll(dbHelper.getSearch(searchInput)); // Set patient list by getSearch function
                adapter.notifyDataSetChanged();
            }
        });

        preBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ // User click previous page button
                if(0 < index - 7){ // If previous page exists
                    index = index - 7;
                    items.clear();
                    adapter.notifyDataSetInvalidated();
                    items.addAll(dbHelper.getList(index)); // Call getList function and get previous list
                    Log.d("items", items.get(0)  + ", " + items.size());
                    adapter.notifyDataSetChanged();
                }
                else{ // If it is first page of patient list
                    Toast.makeText(getApplicationContext(), "First Page", Toast.LENGTH_SHORT).show(); // Show toast message
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ // User click next page button
                if(dbHelper.count >= index + 7){ // If next page exists
                    index = index + 7;
                    items.clear();
                    adapter.notifyDataSetInvalidated();
                    items.addAll(dbHelper.getList(index)); // Call getList function and get next page
                    Log.d("items", items.get(0) + ", " + items.size());
                    adapter.notifyDataSetChanged();
                }
                else{ // If it is last page
                    Toast.makeText(getApplicationContext(), "Last Page", Toast.LENGTH_SHORT).show(); // Show toast message
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){ // User click patient in list
                String[] temp = items.get(position).split("-");
                int check = dbHelper.checkContext(uID, auth, temp[0], getApplicationContext()); // Call checkContext function and check whether user can access to patient data
                if(check == 1){ // If user doesn't have relationship with patient
                    Toast.makeText(getApplicationContext(), "unauthority user", Toast.LENGTH_SHORT).show();
                }
                else if(check == 2){ // If time is out of range
                    Toast.makeText(getApplicationContext(), "Cannot access in this time", Toast.LENGTH_SHORT).show();
                }
                else if(check == 3){ // If location is not in La trobe
                    Toast.makeText(getApplicationContext(), "Cannot access in this location", Toast.LENGTH_SHORT).show();
                }
                else{ // If user satisfy all context
                    Intent patientIntent = new Intent(getApplicationContext(), InfoActivity.class);
                    patientIntent.putExtra("user_id", uID);
                    patientIntent.putExtra("patient", items.get(position)); // Go to InfoActivity with user_id and patient number
                    startActivity(patientIntent);
                }
            }
        });
    }
}
