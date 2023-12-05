package com.tminc.taskmanage;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TodoAdapter extends BaseAdapter{
    private Context context;
    private List<TodoItem> todoList;
    public TodoAdapter(Context context, List<TodoItem> todoList) {
        this.context = context;
        this.todoList = todoList;
    }

    @Override
    public int getCount() {
        return todoList.size();
    }

    @Override
    public Object getItem(int position) {
        return todoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_item_todo, parent, false);
        }

        TextView textViewTodoItem = convertView.findViewById(R.id.textViewTodoItem);
        TodoItem currentTodo = todoList.get(position);

        textViewTodoItem.setText(currentTodo.getTodoText()+" ["+currentTodo.getStatus()+"]");

        if (currentTodo.getIsUrgent()) {
            convertView.setBackgroundColor(Color.RED);
            textViewTodoItem.setTextColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
            textViewTodoItem.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}
