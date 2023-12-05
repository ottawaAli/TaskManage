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

    @Override
    public int getCount() {
        return doneList.size();
    }

    @Override
    public Object getItem(int position) {
        return doneList.get(position);
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

        TextView textViewDoneItem = convertView.findViewById(R.id.textViewTodoItem);
        TodoItem currentDone = doneList.get(position);
        textViewDoneItem.setText(currentDone.getTodoText()+" ["+currentDone.getStatus()+"]");

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
