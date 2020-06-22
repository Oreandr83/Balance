package com.example.balance;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddItemActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText priceText;
    private Button addBtn;
    private Toolbar toolbar;

    public static final String TYPE_KEY = "type";

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        nameText = findViewById(R.id.name_text);
        priceText = findViewById(R.id.price_text);
        addBtn = findViewById(R.id.add_button);
        toolbar = findViewById(R.id.id_bar_add_item);
       setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      type = getIntent().getStringExtra(TYPE_KEY);

      addBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String nameValue = nameText.getText().toString();
              String priceValue = priceText.getText().toString();
               Record record = new Record(nameValue,priceValue,type);
              Intent data = new Intent();

              data.putExtra("recd",record);
              setResult(RESULT_OK,data);
              finish();
          }
      });

       nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              addBtn.setEnabled(!TextUtils.isEmpty(s));
            }
        });

    }
// <- from class AddItemActivity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
