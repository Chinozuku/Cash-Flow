package com.example.restia.cashflow;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity
{
    private static final int FILE_SELECT_CODE = 0;
    private EditText name, amount, title, note;
    private Button browse, submit;
    private ImageView img;
    private Database db;
    private Uri uri;
    private Bitmap bmp;
    private DatePicker date;
    private RadioButton rbIn;
    private RadioButton rbOut;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = (EditText)findViewById(R.id.tbName);
        amount = (EditText)findViewById(R.id.tbAmount);
        title = (EditText)findViewById(R.id.tbTitle);
        note = (EditText)findViewById(R.id.tbNote);
        browse = (Button)findViewById(R.id.btnBrowse);
        img = (ImageView)findViewById(R.id.imgPicture);
        submit = (Button)findViewById(R.id.btnSubmit);
        date = (DatePicker)findViewById(R.id.datePicker);
        db = new Database(this);
        rbIn = (RadioButton)findViewById(R.id.rbIn);
        rbOut = (RadioButton)findViewById(R.id.rbOut);
        database = db.getWritableDatabase();

        initBrowse();
        initSubmit();
        initRadio();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == FILE_SELECT_CODE) //file chooser
        {
            if (resultCode == Activity.RESULT_OK)
            {
                // Get the Uri of the selected file
                uri = data.getData();
                img.setImageURI(uri);
                try
                {
                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (Exception e) { System.out.println(e); }
            }
        }
    }

    private void initBrowse()
    {
        browse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showFileChooser();
            }
        });
    }

    private void initRadio()
    {
        rbIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) rbOut.setChecked(false);
            }
        });
        rbOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) rbIn.setChecked(false);
            }
        });
    }

    private void initSubmit()
    {
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //masukin ke database

                //same to PreparedStatement at C#
                //don't know? ......
                ContentValues values = new ContentValues();

                //converting image to blob, or array of byte to be precise
                byte[] data = null;
                if(bmp != null)
                {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                    data = bos.toByteArray();
                }
                //done converting

                //filling value
                if(amount.getText().toString().equals("") || title.getText().toString().equals("") || name.getText().toString().equals(""))
                {
                    Toast.makeText(AddActivity.this, "Check all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String day = date.getDayOfMonth() + "/";
                    String month = (date.getMonth() + 1) + "/";
                    String year = date.getYear() + "";
                    values.put("transactionType", (rbIn.isChecked() ? "In" : "Out"));
                    values.put("amount", Integer.parseInt(amount.getText().toString()));
                    values.put("title", title.getText().toString());
                    values.put("name", name.getText().toString());
                    values.put("due", day + month + year);
                    values.put("note", note.getText().toString());
                    values.put("pic", data);
                    //end filling

                    database.insert("cash", null, values);
                    Toast.makeText(AddActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void showFileChooser()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try
        {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
