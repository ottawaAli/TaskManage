package com.tminc.taskmanage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity implements RegisterButtonFragment.OnRegisterButtonClickListener {

    private EditText user_name;
    private EditText password;
    private DatabaseHelper databaseHelper;
    private ImageView passwordVisibilityIconMain;
    private boolean isPasswordVisible = false;
    private CheckBox rememberUserCheckbox;



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

        rememberUserCheckbox = findViewById(R.id.rememberuser_checkbox); // Initialize the CheckBox

        // Check if the "Remember my user name" preference is set
        SharedPreferences preferences = getSharedPreferences("user_settings", MODE_PRIVATE);
        boolean rememberUser = preferences.getBoolean("remember_user", false);

        // If the preference is set and a saved user name exists, automatically fill in
        if (rememberUser) {
            String savedUser = preferences.getString("saved_user", "");

            // Check if there is a saved user name
            if (!TextUtils.isEmpty(savedUser)) {
                user_name.setText(savedUser);  // Corrected line
                rememberUserCheckbox.setChecked(true);
            }
        }


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = user_name.getText().toString();
                String passWord = password.getText().toString();
                password.setText("");

                // Save the user name if "Remember my user name" is checked
                if (rememberUserCheckbox.isChecked()) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("saved_user", userName);
                    editor.putBoolean("remember_user", true);
                    editor.apply();
                }

                if (databaseHelper.checkUser(userName, passWord)) {
                    Intent goTodoList = new Intent(MainActivity.this, TodoListActivity.class);
                    goTodoList.putExtra("USERNAME", userName);
                    startActivity(goTodoList);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid user name or password. Please try again.", Toast.LENGTH_SHORT).show();
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
