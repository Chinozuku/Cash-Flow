package com.example.restia.cashflow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Edit extends AppCompatActivity
{
    TextView txtViewName;
    TextView txtViewAmount;
    TextView txtViewTitle;
    TextView txtViewDue;
    TextView txtViewNote;
    ImageView imgViewImage;
    private Database db;
    private SQLiteDatabase database;
    private String[] column = { "id", "transactionType", "amount", "title", "name", "due", "note", "pic" };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        txtViewName = (TextView)findViewById(R.id.txtViewName);
        txtViewAmount = (TextView)findViewById(R.id.txtViewAmount);
        txtViewTitle = (TextView)findViewById(R.id.txtViewTitle);
        txtViewDue = (TextView)findViewById(R.id.txtViewDue);
        txtViewNote = (TextView)findViewById(R.id.txtViewNote);
        imgViewImage = (ImageView)findViewById(R.id.imgViewImage);

        loadData();
    }
    private void loadData()
    {
        db = new Database(this);
        database = db.getReadableDatabase();
        Cursor cursor = database.query("cash", column , "id = " + Integer.parseInt(getIntent().getStringExtra("id")), null, null, null, null);
        cursor.moveToFirst();
        txtViewAmount.setText(": Rp." + cursor.getInt(2) + "");
        txtViewTitle.setText(": "+cursor.getString(3));
        txtViewName.setText(": "+cursor.getString(4));
        if(cursor.getString(6).equalsIgnoreCase("")) {
            txtViewNote.setText(": Tidak Ada");
        }else {
            txtViewNote.setText(": "+cursor.getString(6));
        }
        txtViewDue.setText(": "+cursor.getString(5));
        byte[] tmp = cursor.getBlob(7);

        if(tmp != null)
        {
            Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
            imgViewImage.setImageBitmap(bmp);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) //UpdateActivity
        {
            if (resultCode == Activity.RESULT_OK) {
                //update the data on adapter, as well as on the Model object
                loadData();

            }
        }
    }
    public void edit_db(View v) {
        //ketika button edit ditekan
        Intent i = new Intent(this, UpdateActivity.class);
        i.putExtra("id", getIntent().getStringExtra("id"));
        startActivityForResult(i, 3);

    }

    public void delete_db(View v) {
        //ketika button delete ditekan
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        del();

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }
    private void del(){
        db = new Database(this);
        database = db.getReadableDatabase();
        database.delete("cash", "id = ?", new String[]{getIntent().getStringExtra("id")});
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK);
        finish();
    }
}
