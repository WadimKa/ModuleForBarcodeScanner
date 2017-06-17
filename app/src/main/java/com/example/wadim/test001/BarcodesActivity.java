package com.example.wadim.test001;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;

/**
 * Created by Wadim on 16.06.2017.
 */

public class BarcodesActivity extends AppCompatActivity {
    String namePositon = "";
    ListView listView;
    String[] arrForList, names,date, type,code,com;
    EditText nameE, dateE, typeE, codeE, comE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcodes_lat);
        namePositon = getIntent().getExtras().getString("name");
        setTitle(getString(R.string.TitleForGroupLat)+" "+namePositon);


        readList();
        packArrays();
        ArrayAdapter<CharSequence> charSequenceArrayAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.iteam_of_list, createGlobalArray());
        listView = (ListView) findViewById(R.id.listOfCodes);
        listView.setAdapter(charSequenceArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editItem(position);
            }
        });

    }

    public void readList(){
        try {
            int available =0;
            FileInputStream stream = openFileInput(namePositon);
            available=stream.available();
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer,0,available);
            Toast.makeText(getApplicationContext(), namePositon, Toast.LENGTH_SHORT).show();

            StringBuilder stringBuilder=new StringBuilder("");
            for(int i = 0; i<available;i++){
                stringBuilder.append((char)buffer[i]);
            }
            String bubble=String.valueOf(stringBuilder);
            arrForList=bubble.split("><");

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "File not Found", Toast.LENGTH_SHORT).show();
        }
    }
    public void packArrays(){
        if(arrForList.length>0){
            names = new String[arrForList.length/6];
            date = new String[arrForList.length/6];
            type = new String[arrForList.length/6];
            code = new String[arrForList.length/6];
            com = new String[arrForList.length/6];
            for(int i =0, b =0;i<=arrForList.length-6; i=i+6, b++){
                names[b]=arrForList[i+1];
                date[b]=arrForList[i+2];
                type[b]=arrForList[i+3];
                code[b]=arrForList[i+4];
                com[b]=arrForList[i+5];

            }
        }
    }


    public String[] createGlobalArray(){
        String[] global = new String[names.length];

        for (int i = 0; i<names.length;i++){
            global[i]="Name-"+names[i]+" - "+date[i]+"\n"+"Barcode-"+type[i]+"-"+code[i]+"\n"+com[i];
        }
        return global;
    }
    public  void onClick(View view){
        Intent intent = new Intent(BarcodesActivity.this,CreateBarcodeActivity.class);
        intent.putExtra("name", namePositon);
        startActivity(intent);

    }
    public void editItem(int position){
        nameE = new EditText(BarcodesActivity.this);
        dateE = new EditText(BarcodesActivity.this);
        typeE = new EditText(BarcodesActivity.this);
        codeE = new EditText(BarcodesActivity.this);
        comE  = new EditText(BarcodesActivity.this);
        dateE.setText(date[position]);
        typeE.setText(type[position]);
        codeE.setText(code[position]);
        comE.setText(com[position]);
        nameE.setText(names[position]);

        LinearLayout layout = new LinearLayout(BarcodesActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(nameE);
        layout.addView(dateE);
        layout.addView(typeE);
        layout.addView(codeE);
        layout.addView(comE);

        AlertDialog.Builder builder = new AlertDialog.Builder(BarcodesActivity.this);
        builder.setTitle("Edit barcode");
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                names[position] = nameE.getText().toString();
                date[position] = dateE.getText().toString();
                type[position] = typeE.getText().toString();
                code[position] = codeE.getText().toString();
                com[position] = comE.getText().toString();
            }
        });
        builder.show();



        recreate();
    }
}


