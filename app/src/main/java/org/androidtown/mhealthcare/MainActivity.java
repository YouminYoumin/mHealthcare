package org.androidtown.mhealthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public EditText uID;
    public EditText uPW;
    public Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uID = (EditText)findViewById(R.id.USERID);
        uPW = (EditText)findViewById(R.id.USERPW);
        loginBtn = (Button)findViewById(R.id.loginBtn);

        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "mhealth.db", null, 1);

        //When user log on
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean accept = false;
                accept = dbHelper.getLogin(uID.getText().toString(),uPW.getText().toString()); // Call getLogin function
                if(accept){ // if getLogin returns true
                    Intent intent = new Intent(MainActivity.this, IndexActivity.class); // Go to IndexActivity and give user_id
                    intent.putExtra("user_id", uID.getText().toString());
                    startActivity(intent);
                }
                else{ // if getLogin returns false
                    Toast.makeText(getApplicationContext(), "Please check your ID or PW", Toast.LENGTH_SHORT).show(); // show toast message
                }
            }
        });
    }
}
