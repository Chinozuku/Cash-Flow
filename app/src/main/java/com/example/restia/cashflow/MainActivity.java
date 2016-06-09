package com.example.restia.cashflow;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//the first activity that the application loaded
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //standard widget by android
    ListView listView;
    ArrayList<Model> model;
    CustomAdapter adapter;
    Spinner spinner;
    ArrayAdapter<CharSequence> spinnerAdapter;

    //database property
    private Database db;
    private SQLiteDatabase database;
    private String[] column = { "id", "transactionType", "amount", "title", "name", "due", "note", "pic" };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //by default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //end by default

        //initializing the database
        db = new Database(this);
        database = db.getReadableDatabase();
        //end initialisation

        bindAdapter();
        loadFromDatabase();
        initCustomListView();
        initSpinner();
    }

    private void bindAdapter() //to bind custom and or standard adapter to its widget respectively
    {
        //custom listview
        listView = (ListView)findViewById(R.id.listView);
        model = new ArrayList<>();
        adapter = new CustomAdapter(MainActivity.this, model);
        listView.setAdapter(adapter);

        //simple spinner
        spinner = (Spinner)findViewById(R.id.spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.months, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void initSpinner() //to set the current month and year (year is to be developed later)
    {
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MMMM");

        for(int i = 0; i < spinnerAdapter.getCount(); i++)
        {
            if(month.format(d).equals(spinnerAdapter.getItem(i).toString()))
            {
                System.out.println(i);
                spinner.setSelection(i);
            }
        }
    }

    private void initCustomListView()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //well, still don't know what to change with current data
                //still thinking about it
                Intent i = new Intent(MainActivity.this, Edit.class);
                LinearLayout l = (LinearLayout) view;
                i.putExtra("id", ((TextView)l.getChildAt(0)).getText().toString());
                startActivityForResult(i, 2);
            }
        });
    }

    private void loadFromDatabase()
    {
        //manual initialisation and clear all current data
        adapter.clear();
        model = new ArrayList<>();
        Cursor cursor = database.query("cash", column , null, null, null, null, null);
        cursor.moveToFirst();

        //just temporart variables, to contain the complicated value
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date tmp = null;
        Model m;

        for(int i = 0; i < cursor.getCount(); i++)
        {
            m = new Model();
            tmp = null;

            m.setId(cursor.getInt(0));
            m.setType(cursor.getString(1));
            m.setAmount(cursor.getInt(2));
            m.setTitle(cursor.getString(3));
            m.setName(cursor.getString(4));
            m.setNote(cursor.getString(6));
            m.setPicture(cursor.getBlob(7));

            //convert the string into date format, seems no use, but gonna keep it for a while
            try { tmp = df.parse(cursor.getString(5)); }
            catch (Exception e) { }
            m.setDate(tmp);
            cursor.moveToNext();

            model.add(m);
            adapter.add(m);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1) //AddActivity
        {
            if (resultCode == Activity.RESULT_OK)
            {
                //update the data on adapter, as well as on the Model object
                loadFromDatabase();
            }
        }
        else if (requestCode == 2) //Edit
        {

        }
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add)
        {
            //normally, we don't need to wait for result, but we need to update the custom list view
            //so, we need to wait, if the database has been changed, we need to update the list view as well
            Intent i = new Intent(MainActivity.this, AddActivity.class);
            startActivityForResult(i, 1);
        }
        else if (id == R.id.nav_report)
        {
            //make report
        }
        else if (id == R.id.nav_backup)
        {
            //export the database
        }
        else if (id == R.id.nav_restore)
        {
            //import the database, but need to check the filename and type
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
