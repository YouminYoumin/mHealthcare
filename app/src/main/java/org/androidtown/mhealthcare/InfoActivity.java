package org.androidtown.mhealthcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity{
    ArrayList<String> patientitems;
    ArrayAdapter<String> pAdapter;
    ListView infoView;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "mhealth.db", null, 1);
        dbHelper.checkSize();
        Intent intent = new Intent(this.getIntent());
        final String patientInfo = intent.getStringExtra("patient");
        final String uID = intent.getStringExtra("user_id");
        String[] data = patientInfo.split("-"); // Parsing patient number and name

        TextView patientName = (TextView)findViewById(R.id.patientName);
        patientName.setText(data[1]);

        patientitems = new ArrayList<String>();
        patientitems = dbHelper.getResult(data[0], uID); // Call getResult function and show patient information by authority of user
        pAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patientitems);

        ListView feature = (ListView)findViewById(R.id.feature);
        feature.setAdapter(pAdapter);
    }
}
