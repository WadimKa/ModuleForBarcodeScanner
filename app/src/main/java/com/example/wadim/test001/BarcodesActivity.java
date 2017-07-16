package com.example.wadim.test001;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by Wadim on 16.06.2017.
 */

public class BarcodesActivity extends AppCompatActivity {
    String namePositon = "";
    ListView listView;
    String[] arrForList, names, date, type, code, com;
    EditText nameE, dateE, typeE, codeE, comE;
    int flag = 0;
    ArrayAdapter<CharSequence> charSequenceArrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                workWithShare();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void workWithShare() {
        int triger = 0;
        for (int i = 0; i <= charSequenceArrayAdapter.getCount(); i++) {
            if (listView.isItemChecked(i)) triger += 1;
        }
        if (triger == 0) {
            Snackbar.make(listView, "Select barcodes", Snackbar.LENGTH_SHORT).show();
        } else {
            SparseBooleanArray sbarray = listView.getCheckedItemPositions();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, createArrForSend(sbarray));
            startActivity(intent);
            triger = 0;
        }
    }

    private String createArrForSend(SparseBooleanArray sbarray) {
        StringBuilder stringBuilder = new StringBuilder("");
        String[] global = createGlobalArray();
        for (int i = 0; i < sbarray.size() - 1; i++) {
            if (sbarray.get(i)) stringBuilder.append(global[i] + "\n -------------------------\n");
        }
        String stringWhichSend = String.valueOf(stringBuilder);
        return stringWhichSend;

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcodes_lat);
        namePositon = getIntent().getExtras().getString("name");
        //setTitle(getString(R.string.TitleForGroupLat)+" #"+namePositon+"#");
        setTitle("  Group \"" + namePositon + "\"");
        readList();
        packArrays();
        charSequenceArrayAdapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.test,  createGlobalArray());
        listView = (ListView) findViewById(R.id.listOfCodes);
        listView.setAdapter(charSequenceArrayAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag == 0) {
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    for (int i = 0; i <= charSequenceArrayAdapter.getCount(); i++) {
                        listView.setItemChecked(i, true);
                    }
                    flag = 1;
                } else {
                    flag = 0;
                    for (int i = 0; i <= charSequenceArrayAdapter.getCount(); i++) {
                        listView.setItemChecked(i, false);
                    }
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                }
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (flag == 0) {
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    editItem(position);
                    listView.setItemChecked(position, false);
                }

            }
        });

    }

    public void readList() {
        try {
            /*старый код, не поддерживал utf8.новый проще
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
            arrForList=bubble.split("><");*/
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(namePositon), "utf8"));
            String line = "";
            StringBuilder builder = new StringBuilder("");
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            arrForList = new String(String.valueOf(builder)).split("><");

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "File not Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void packArrays() {
        if (arrForList.length > 0) {
            names = new String[arrForList.length / 6];
            date = new String[arrForList.length / 6];
            type = new String[arrForList.length / 6];
            code = new String[arrForList.length / 6];
            com = new String[arrForList.length / 6];
            for (int i = 0, b = 0; i <= arrForList.length - 6; i = i + 6, b++) {
                names[b] = arrForList[i + 1];
                date[b] = arrForList[i + 2];
                type[b] = arrForList[i + 3];
                code[b] = arrForList[i + 4];
                com[b] = arrForList[i + 5];

            }
        }
    }


    public String[] createGlobalArray() {
        String[] global = new String[names.length];

        for (int i = 0; i < names.length; i++) {
            global[i] = " " + names[i] + "\n" + date[i] + "\n" + type[i] + "\n" + code[i] + "\n (" + com[i] + ")";
        }
        return global;
    }

    public void overWrite() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            stringBuilder.append("><" + names[i] + "><" + date[i] + "><" + type[i] + "><" + code[i] + "><" + com[i] + "><");
        }
        //Toast.makeText(BarcodesActivity.this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
        try {
            FileOutputStream fileOutputStream = openFileOutput(namePositon, MODE_PRIVATE);
            fileOutputStream.write(stringBuilder.toString().getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            Toast.makeText(BarcodesActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick(View view) {
        Intent intent = new Intent(BarcodesActivity.this, CreateBarcodeActivity.class);
        intent.putExtra("name", namePositon);
        startActivity(intent);
        finish();

    }

    public void editItem(final int position) {
        nameE = new EditText(BarcodesActivity.this);
        dateE = new EditText(BarcodesActivity.this);
        typeE = new EditText(BarcodesActivity.this);
        codeE = new EditText(BarcodesActivity.this);
        comE = new EditText(BarcodesActivity.this);
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
        layout.setPadding(30, 0, 30, 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(BarcodesActivity.this);
        builder.setTitle("       Edit barcode");
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                names[position] = nameE.getText().toString();
                date[position] = dateE.getText().toString();
                type[position] = typeE.getText().toString();
                code[position] = codeE.getText().toString();
                com[position] = comE.getText().toString();
                overWrite();
                recreate();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}


