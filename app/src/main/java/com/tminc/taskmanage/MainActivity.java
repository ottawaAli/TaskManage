package com.tminc.taskmanage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity implements RegisterButtonFragment.OnRegisterButtonClickListener {

    private EditText user_name;
    private EditText password;
    private DatabaseHelper databaseHelper;
    private ImageView passwordVisibilityIconMain; // Add this line
    private boolean isPasswordVisible = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        passwordVisibilityIconMain = findViewById(R.id.password_visibility_icon_main);


        Button login_btn = findViewById(R.id.login_button);
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password);

        databaseHelper = new DatabaseHelper(this);

        RegisterButtonFragment refreshButtonFragment = new RegisterButtonFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, refreshButtonFragment, "RefreshButtonFragment");
        transaction.commit();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = user_name.getText().toString();
                String passWord = password.getText().toString();
                password.setText("");

                if (databaseHelper.checkUser(userName, passWord)) {
                    Intent goTodoList = new Intent(MainActivity.this, TodoListActivity.class);
                    goTodoList.putExtra("USERNAME", userName);
                    startActivity(goTodoList);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Email or password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set OnClickListener for the password visibility toggle icon in MainActivity
        passwordVisibilityIconMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibilityMain();
            }
        });
    }

    @Override
    public void onRegisterButtonClick() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    // Add this method to toggle password visibility in MainActivity
    private void togglePasswordVisibilityMain() {
        if (!isPasswordVisible) {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordVisibilityIconMain.setImageResource(R.drawable.ic_visibility_on);
            isPasswordVisible = true;
        } else {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordVisibilityIconMain.setImageResource(R.drawable.ic_visibility_off);
            isPasswordVisible = false;
        }
        password.setSelection(password.getText().length());
    }
}
