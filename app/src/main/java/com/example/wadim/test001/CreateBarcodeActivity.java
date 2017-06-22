package com.example.wadim.test001;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Wadim on 16.06.2017.
 */

public class CreateBarcodeActivity extends AppCompatActivity {
    private TextView formatTxt, contentTxt;
    EditText nameCode, comment;
    String nameGroup="";

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_barcodes_lat);
        setTitle("Новый штрих-код");
        nameGroup = getIntent().getExtras().getString("name");

        formatTxt = (TextView) findViewById(R.id.tvType);
        contentTxt = (TextView) findViewById(R.id.tvCode);

        nameCode = (EditText) findViewById(R.id.edtName);
        comment = (EditText) findViewById(R.id.edtComment);
    }
    public void onClick(View v) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, intent);

        if (scanningResult != null) {
            // we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText(scanFormat);
            contentTxt.setText(scanContent);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void onClickSecond(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, createDataString());
        startActivity(intent);
    }

    public void onClickSave(View view) {
        try {
            Toast.makeText(getApplicationContext(), createDataString(), Toast.LENGTH_SHORT).show();

             FileOutputStream fileOutputStream = openFileOutput(nameGroup, MODE_APPEND);
             fileOutputStream.write(createDataString().getBytes());
             fileOutputStream.flush();
             fileOutputStream.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public String createDataString(){
        String data ="><"+nameCode.getText().toString()+"><"+getTime()+"><"+formatTxt.getText().toString()+"><"+contentTxt.getText().toString()+"><"+comment.getText().toString()+"><";
        return data;
    }
    public String getTime(){
        long date = System.currentTimeMillis();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM , yyyy h:mm a");
        String dateString = sdf.format(date);
        return dateString;
    }
}
