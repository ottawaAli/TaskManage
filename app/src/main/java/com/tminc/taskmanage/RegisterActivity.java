package com.tminc.taskmanage;

import androidx.appcompat.app.ActionBar;
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
    private ImageView passwordVisibilityIcon; // 声明为类的成员变量



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        passwordVisibilityIcon = findViewById(R.id.password_visibility_icon);
        user_name = findViewById(R.id.new_user_name);
        password = findViewById(R.id.new_password);  // Initialize here
        re_password = findViewById(R.id.reenter_password);

        // Initialize UI elements and the DatabaseHelper
        Button register_btn = findViewById(R.id.register_button);
        user_name = findViewById(R.id.new_user_name);
        password = findViewById(R.id.new_password);
        re_password = findViewById(R.id.reenter_password);

        // 设置密码输入框的初始输入类型为密码
        if (password != null) {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            // 添加显示/隐藏密码功能
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


    // 切换密码可见性
    private void togglePasswordVisibility() {
        int inputType = password.getInputType();
        if ((inputType & InputType.TYPE_TEXT_VARIATION_PASSWORD) != 0) {
            // 密码模式，切换为可见文本
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordVisibilityIcon.setImageResource(R.drawable.ic_visibility_on);
        } else {
            // 可见文本模式，切换为密码
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordVisibilityIcon.setImageResource(R.drawable.ic_visibility_off);
        }

        // 重新获取焦点
        password.requestFocus();

        // 移动光标到末尾
        password.setSelection(password.getText().length());
    }

}