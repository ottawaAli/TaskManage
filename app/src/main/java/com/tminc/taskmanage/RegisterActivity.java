package com.tminc.taskmanage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText user_name;
    private EditText password;
    private EditText re_password;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements and the DatabaseHelper
        Button register_btn = findViewById(R.id.register_button);
        user_name = findViewById(R.id.new_user_name);
        password = findViewById(R.id.new_password);
        re_password = findViewById(R.id.reenter_password);

        databaseHelper = new DatabaseHelper(this);

        // Set OnClickListener for the Register button
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = user_name.getText().toString();
                String passWord = password.getText().toString();
                String rePassWord = re_password.getText().toString();


                // Check if the entered username already exists in the database
                if (databaseHelper.checkUserName(userName)) {
                    Toast.makeText(RegisterActivity.this, "This username has been taken, please change another one", Toast.LENGTH_SHORT).show();
                } else if (!passWord.equals(rePassWord)) {
                    Toast.makeText(RegisterActivity.this, "The passwords are different, please enter again", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.saveUser(userName, passWord);
                    Toast.makeText(RegisterActivity.this, "Register successfully!", Toast.LENGTH_SHORT).show();

                    // Redirect to TodoListActivity with the registered username
                    Intent goTodoList = new Intent(RegisterActivity.this, TodoListActivity.class);
                    goTodoList.putExtra("USERNAME", userName);
                    startActivity(goTodoList);
                }
            }
        });
    }
}