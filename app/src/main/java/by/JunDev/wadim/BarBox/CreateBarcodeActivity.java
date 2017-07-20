package by.JunDev.wadim.BarBox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import by.JunDev.wadim.BarBox.package_for_import.IntentIntegrator;
import by.JunDev.wadim.BarBox.package_for_import.IntentResult;

import java.io.FileOutputStream;

/**
 * Created by Wadim on 16.06.2017.
 */

public class CreateBarcodeActivity extends AppCompatActivity {
    private TextView formatTxt, contentTxt;
    EditText nameCode, comment;
    String nameGroup="";
    Button scan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(by.JunDev.wadim.BarBox.R.layout.create_barcodes_lat);
        setTitle("  Create new barcode");
        nameGroup = getIntent().getExtras().getString("name");
        scan = (Button) findViewById(by.JunDev.wadim.BarBox.R.id.btnScan);


        formatTxt = (TextView) findViewById(by.JunDev.wadim.BarBox.R.id.tvType);
        contentTxt = (TextView) findViewById(by.JunDev.wadim.BarBox.R.id.tvCode);

        nameCode = (EditText) findViewById(by.JunDev.wadim.BarBox.R.id.edtName);
        comment = (EditText) findViewById(by.JunDev.wadim.BarBox.R.id.edtComment);
    }
    public void onClick(View v) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
        nameCode.setText("");
        comment.setText("");
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
            formatTxt.setText("Code type : "+scanFormat);
            contentTxt.setText("Barcode : "+scanContent);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onClickSave(View view) {
        if(formatTxt.getText().toString().equals("")){
            Toast.makeText(CreateBarcodeActivity.this, "First scan the barcode", Toast.LENGTH_SHORT).show();
        }else {
            try {
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

                FileOutputStream fileOutputStream = openFileOutput(nameGroup, MODE_APPEND);
                fileOutputStream.write(createDataString().getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String createDataString(){
        String data="";
        if(comment.getText().toString().equals("")) {
            data = "><" + nameCode.getText().toString() + "><" + getTime() + "><" + formatTxt.getText().toString() + "><" + contentTxt.getText().toString() + "><" + " - " + "><";
        }else{
            data = "><" + nameCode.getText().toString() + "><" + getTime() + "><" + formatTxt.getText().toString() + "><" + contentTxt.getText().toString() + "><" + comment.getText().toString() + "><";

        }
        return data;
    }
    public String getTime(){
        long date = System.currentTimeMillis();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM , yyyy h:mm a");
        String dateString = sdf.format(date);
        return dateString;
    }
}
