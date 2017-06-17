package com.example.wadim.test001;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] arr;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lat);
        listView = (ListView) findViewById(R.id.listOfGroup);
        try {
            //String dat="><Name><Date><Type><Code><Comment><><Name1><Date><Type><Code><Comment><><Name2><Date><Type><Code><Comment><><Name3><Date><Type><Code><Comment><";
            FileOutputStream fileOutputStream = openFileOutput("File2", MODE_PRIVATE);
            deleteFile("File2");
            //fileOutputStream.write(dat.getBytes());
            //fileOutputStream.flush();
            //fileOutputStream.close();
            File file = new File(getApplicationInfo().dataDir+"/files");
            arr = file.list();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }


        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.iteam_of_list, arr);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),BarcodesActivity.class);
                intent.putExtra("name",arr[i]);
                startActivity(intent);
            }
        });


    }
    public  void onClick(View view){
        showAlertDialog();
    }
    public void createNewGroup(String name){
        try {
            FileOutputStream fileOutputStream = openFileOutput(name, MODE_APPEND);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialog() {
        editText=new EditText(MainActivity.this);
        AlertDialog.Builder dial = new AlertDialog.Builder(MainActivity.this);
        dial.setTitle("Create new group");
        dial.setMessage("\n"+"Name the group");
        dial.setView(editText);
        dial.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createNewGroup(editText.getText().toString());
                recreate();
            }
        });
        dial.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dial.show();
    }
}
