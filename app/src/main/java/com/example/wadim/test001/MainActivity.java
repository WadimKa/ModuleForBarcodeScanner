package com.example.wadim.test001;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] arr;
    EditText editText, edtForRename;
    ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lat);
        listView = (ListView) findViewById(R.id.listOfGroup);
        registerForContextMenu(listView);
        try {
            FileOutputStream fileOutputStream = openFileOutput("File2", MODE_PRIVATE);
            deleteFile("File2");
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
                    Intent intent = new Intent(getApplicationContext(), BarcodesActivity.class);
                    intent.putExtra("name", arr[i]);
                    startActivity(intent);
            }
        });



    }
    void onClick(View view){
        showAlertDialogCreate();
    }
    void createNewGroup(String name){
        try {
            FileOutputStream fileOutputStream = openFileOutput(name, MODE_APPEND);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

     void showAlertDialogCreate() {
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_group_barcodes, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.delete : deleteGroup(arr[contextMenuInfo.position]);
                break;
            case R.id.rename : renameGroup(arr[contextMenuInfo.position]);
                break;
        }
        recreate();
        return super.onContextItemSelected(item);
    }

    private void renameGroup(final String name) {
        edtForRename = new EditText(MainActivity.this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter a new name");
        edtForRename.setHint("New name");
        builder.setView(edtForRename);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                renameFile(name, editText.getText().toString());
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }

    private void renameFile(String oldName, String newName) {
        File oldFile = new File(getApplicationInfo().dataDir+"/files/"+oldName);
        File newFile = new File(getApplicationInfo().dataDir+"/files/"+newName);
        oldFile.renameTo(newFile);
    }

    private void deleteGroup(String name) {
        try {
            File file = new File(getApplicationInfo().dataDir+"/files/"+name);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
