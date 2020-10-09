package com.earsnot.rooms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.earsnot.rooms.model.Person;
import com.earsnot.rooms.model.Repository;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_FOR_CLICKS = "SHARED_PREFS_FOR_CLICKS";
    private static final String CLICK_COUNT = "CLICK_COUNT";
    private static final String TAG ="MainActivity";
    // widgets
    private TextView txtMain;
    private EditText edtFirstName, edtLastName, edtSearch;
    private Button btnAdd, btnSearch;
    private int buttonClicks;
    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initUI();
        
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FOR_CLICKS, MODE_PRIVATE);
        buttonClicks = prefs.getInt(CLICK_COUNT, 0);
        Log.d(TAG, "onCreate click count loaded from sprefs");

        // state
        repo.getPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {
                txtMain.setText(printList(people, null));
            }
        });

    }
    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS_FOR_CLICKS, MODE_PRIVATE).edit();
        editor.putInt(CLICK_COUNT, buttonClicks);
        editor.apply();
        Log.d(TAG, "onStop: saved click count");
        super.onStop();
    }
    private void initUI() {
        txtMain = findViewById(R.id.txtMain);
        repo = new Repository(getApplication());
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtSearch = findViewById(R.id.edtSearch);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPerson();
                buttonClicks++;
            }
        });
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
                buttonClicks++;
            }
        });
    }

    private void search() {
        String srch = edtSearch.getText().toString();
        List<Person> multiPersonResult = repo.searchForPersons(srch);
        searchResultPopup(multiPersonResult);
    }

    private void searchResultPopup(final List<Person> resultSet) {
        // dialog for when no matches are found
        if (resultSet == null || resultSet.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage(R.string.no_matches)
                    .setTitle(R.string.search_result);
            builder.create().show();
        }
        //dialog for when more than 0 matches are found
        else {
            String result = printList(resultSet, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage(result)
                    .setTitle(R.string.search_result)
                    .setPositiveButton(R.string.top_result, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //pass 0th index to view
                            viewPerson(resultSet.get(0));
                        }
                    })
                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            repo.deleteAll(resultSet);
                        }
                    });
            builder.create().show();
        }
    }

    private void viewPerson(Person person) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.person)
                .setMessage(printList(null, person));
        builder.create().show();
    }

    private void addPerson() {
        String fName = edtFirstName.getText().toString();
        String lName = edtLastName.getText().toString();
        if(!fName.equals("") && !lName.equals("")){
            repo.addPerson(fName, lName);
            Toast.makeText(MainActivity.this, R.string.add_person_success, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
        }
    }

    private String printList(@Nullable List<Person> people, @Nullable Person person) {
        String print = "Persons:";
        if(people != null){
            print += people.size();
            for(Person p : people){
                print += "\n #" + p.getUid() + ":" + p.getFirstName() + " " + p.getLastName() + ", " + p.getEmail();
            }
        }
        if(person != null) {
            print += "\n #" + person.getUid() + ":" + person.getFirstName() + " " + person.getLastName() + ", " + person.getEmail();
        }

        return print;
    }
}