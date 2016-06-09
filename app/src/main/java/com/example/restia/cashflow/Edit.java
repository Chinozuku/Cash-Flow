package com.example.restia.cashflow;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        txtViewAmount.setText(cursor.getInt(2) + "");
        txtViewTitle.setText(cursor.getString(3));
        txtViewName.setText(cursor.getString(4));
        txtViewNote.setText(cursor.getString(6));
        txtViewDue.setText(cursor.getString(5));

        //getting the blob data from database
        byte[] tmp = cursor.getBlob(7);
        if(tmp != null)
        {
            Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
            imgViewImage.setImageBitmap(bmp);
        }
    }
}
