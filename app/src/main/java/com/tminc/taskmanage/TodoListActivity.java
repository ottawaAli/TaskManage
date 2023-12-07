package com.tminc.taskmanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import java.util.List;

import android.app.AlertDialog;
import android.widget.TextView;

public class TodoListActivity extends AppCompatActivity {
    private List<TodoItem> toDoList;
    private List<TodoItem> doneList;
    private TodoAdapter todoAdapter;
    private DoneAdapter doneAdapter;
    private EditText editText;
    private String username;
    private Switch switchUrgent;

    private ProgressBar progressBar;
    private TextView progressText;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Intent fromLogin = getIntent();
        username = fromLogin.getStringExtra("USERNAME");

        databaseHelper = new DatabaseHelper(this);

        toDoList = databaseHelper.getTodoList(username);
        doneList = databaseHelper.getDoneList(username);


        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        updateProgress(); // Update progress initially

        editText = findViewById(R.id.inputEditText);
        switchUrgent = findViewById(R.id.urgentSwitch);
        Button buttonAdd = findViewById(R.id.submitButton);

        getSupportActionBar().setTitle(getString(R.string.app_name) + "  (Hi," + username + ")");

        ListView todoListView = findViewById(R.id.todoListView);
        todoAdapter = new TodoAdapter(this, toDoList);
        todoListView.setAdapter(todoAdapter);

        ListView doneListView = findViewById(R.id.doneListView);
        doneAdapter = new DoneAdapter(this, doneList);
        doneListView.setAdapter(doneAdapter);

        DoneFragment doneFragment = new DoneFragment(username);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.doneFragmentContainer, doneFragment)
                .commit();

        buttonAdd.setOnClickListener(v -> addTodoItem());

        todoListView.setOnItemLongClickListener((adapterView, view, position, l) -> {
            handleTodoItem(position);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_exit) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void addTodoItem() {
        String text = editText.getText().toString();
        boolean isUrgent = switchUrgent.isChecked();
        TodoItem newItem = new TodoItem(username, text, isUrgent, "TODO");
        databaseHelper.saveTodo(newItem);
        toDoList.add(newItem);

        editText.setText("");
        switchUrgent.setChecked(false);
        todoAdapter.notifyDataSetChanged();

        updateProgress();
    }

    private void handleTodoItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.hint_text)
                .setMessage(getString(R.string.row_text, position))
                .setNegativeButton(R.string.exit_button_name, (dialog, id) -> dialog.dismiss())
                .setPositiveButton(R.string.delete_button_name, (dialog, id) -> {
                    databaseHelper.removeTodo(toDoList.get(position));
                    toDoList.remove(position);
                    todoAdapter.notifyDataSetChanged();
                })
                .setNeutralButton(R.string.done_button_name, (dialog, id) -> {
                    databaseHelper.completeTodo(toDoList.get(position));
                    TodoItem removedItem = toDoList.remove(position);
                    removedItem.setStatus("DONE");
                    doneList.add(removedItem);

                    todoAdapter.notifyDataSetChanged();
                    doneAdapter.notifyDataSetChanged();
                    updateProgress();

                })
                .create()
                .show();
    }


    private void updateProgress() {
        int totalItemCount = getTotalItemCount();
        int doneItemCount = doneList.size();

        if (totalItemCount > 0) {
            int progress = (int) ((doneItemCount / (float) totalItemCount) * 100);
            progressBar.setProgress(progress);
            progressText.setText(getString(R.string.progress_text, progress));
        } else {
            progressBar.setProgress(0);
            progressText.setText(getString(R.string.progress_text, 0));
        }

        TextView todoCountTextView = findViewById(R.id.todoTextView);
        todoCountTextView.setText("Done List"+ " (" + (totalItemCount-doneItemCount) + ")");
        TextView doneCountTextView = findViewById(R.id.doneTextView);
        doneCountTextView.setText("Done List"+ " (" + doneItemCount + ")");
    }

    private int getTotalItemCount() {
        return toDoList.size() + doneList.size();
    }

}