package com.tminc.taskmanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText user_name;
    private EditText password;
    private EditText re_password;
    private DatabaseHelper databaseHelper;
    private ImageView passwordVisibilityIcon;
    private ImageView passwordVisibilityIcon2;
    private boolean isPasswordVisible = false;
    private boolean isRePasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        passwordVisibilityIcon = findViewById(R.id.password_visibility_icon);
        passwordVisibilityIcon2 = findViewById(R.id.password_visibility_icon2);
        user_name = findViewById(R.id.new_user_name);
        password = findViewById(R.id.new_password);  // Initialize here
        re_password = findViewById(R.id.reenter_password);

        // Initialize UI elements and the DatabaseHelper
        Button register_btn = findViewById(R.id.register_button);
        user_name = findViewById(R.id.new_user_name);
        password = findViewById(R.id.new_password);
        re_password = findViewById(R.id.reenter_password);

        if (password != null) {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            // click to hide or view the password
            passwordVisibilityIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    togglePasswordVisibility();
                }
            });
        } else {
            // Handle the case where password EditText is not found
            Toast.makeText(this, "Error: Password EditText not found", Toast.LENGTH_SHORT).show();
        }

        if (re_password != null) {
            re_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            passwordVisibilityIcon2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    togglePassword2Visibility();
                }
            });
        } else {
            // Handle the case where password EditText is not found
            Toast.makeText(this, "Error: Password EditText not found", Toast.LENGTH_SHORT).show();
        }

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

                    Intent goTodoLogin = new Intent(RegisterActivity.this, MainActivity.class);
                    //goTodoList.putExtra("USERNAME", userName);
                    startActivity(goTodoLogin);
                }
            }
        });
    }


    // 切换密码可见性
    private void togglePasswordVisibility() {
        if (!isPasswordVisible) {
            // change to view password
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordVisibilityIcon.setImageResource(R.drawable.ic_visibility_on);
            isPasswordVisible = true;
        } else {
            // change to hide password
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordVisibilityIcon.setImageResource(R.drawable.ic_visibility_off);
            isPasswordVisible = false;
        }

        password.requestFocus();
        password.setSelection(password.getText().length());
    }

    private void togglePassword2Visibility() {
        if (!isRePasswordVisible) {
            re_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordVisibilityIcon2.setImageResource(R.drawable.ic_visibility_on);
            isRePasswordVisible = true;
        } else {
            re_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordVisibilityIcon2.setImageResource(R.drawable.ic_visibility_off);
            isRePasswordVisible = false;
        }

        re_password.requestFocus();
        re_password.setSelection(re_password.getText().length());
    }
}