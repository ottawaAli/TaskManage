package com.tminc.taskmanage;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DoneAdapter extends BaseAdapter{
    private Context context;
    private List<TodoItem> doneList;
    public DoneAdapter(Context context, List<TodoItem> doneList) {
        this.context = context;
        this.doneList = doneList;
    }

    // Returns the count of items in the list
    @Override
    public int getCount() {
        return doneList.size();
    }

    // Returns the item at the specified position
    @Override
    public Object getItem(int position) {
        return doneList.get(position);
    }

    @Override
    // Returns the item ID for the specified position
    public long getItemId(int position) {
        return position;
    }

    @Override
    // Creates a new list item view for each item in the data source
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout if it hasn't been done yet
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_item_todo, parent, false);
        }

        // Set the text and color of the todo item based on its properties
        TextView textViewDoneItem = convertView.findViewById(R.id.textViewTodoItem);
        TodoItem currentDone = doneList.get(position);
        textViewDoneItem.setText(currentDone.getTodoText()+" ["+currentDone.getStatus()+"]");

        // Change background and text color based on urgency
        if (currentDone.getIsUrgent()) {
            convertView.setBackgroundColor(Color.RED);
            textViewDoneItem.setTextColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
            textViewDoneItem.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}
