package com.earsnot.rooms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.earsnot.rooms.model.Person;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private EditText edtFirstName, edtLastName;
    private Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.imgMainHeader);
        img.setImageResource(R.drawable.images);
        Person person = new Person("adolf", "hitler");
    }
}