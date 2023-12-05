package com.tminc.taskmanage;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todoDB";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsers = "CREATE TABLE users (id INTEGER PRIMARY KEY, username TEXT, password TEXT)";
        String createTableTodos = "CREATE TABLE todos (id INTEGER PRIMARY KEY, username TEXT, todo TEXT, urgent INTEGER, status TEXT)";
        db.execSQL(createTableUsers);
        db.execSQL(createTableTodos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS todos");
        onCreate(db);
    }

    public void saveUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        db.insert("users", null, values);
    }

    public Boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        String[] selectionArgs = {username, password};
        try (Cursor cursor = db.rawQuery(query, selectionArgs)) {
            return cursor.moveToFirst();
        } catch (Exception e) {
            // Handle or log the exception
            e.printStackTrace();
        }

        return false;
    }

    public Boolean checkUserName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE username = ?";
        String[] selectionArgs = {username};
        try (Cursor cursor = db.rawQuery(query, selectionArgs)) {
            return cursor.moveToFirst();
        } catch (Exception e) {
            // Handle or log the exception
            e.printStackTrace();
        }

        return false;
    }
    public void saveTodo(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", todoItem.getUserName());
        values.put("todo", todoItem.getTodoText());
        if (todoItem.getIsUrgent()) values.put("urgent", 1);
        else values.put("urgent", 0);
        values.put("status", todoItem.getStatus());

        db.insert("todos", null, values);
    }

    public void removeTodo(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "username = ? AND todo = ?";
        String[] whereArgs = {todoItem.getUserName(), todoItem.getTodoText()};

        db.delete("todos", whereClause, whereArgs);
    }

    public void completeTodo(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "DONE");

        db.update(
                "todos",
                values,
                "username=? AND todo=?",
                new String[]{todoItem.getUserName(), todoItem.getTodoText()}
        );

        db.close();
    }

    public List<TodoItem> getTodoList(String username) {
        List<TodoItem> todoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM todos WHERE username = ? and status = 'TODO'";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        int todoIndex = cursor.getColumnIndex("todo");
        int urgentIndex = cursor.getColumnIndex("urgent");

        while (cursor.moveToNext()) {
            if (todoIndex != -1 && urgentIndex != -1) {
                String todoText = cursor.getString(todoIndex);
                boolean isUrgent = cursor.getInt(urgentIndex) == 1;

                TodoItem todoItem = new TodoItem(username, todoText, isUrgent, "TODO");
                todoList.add(todoItem);
            }
        }

        cursor.close();
        return todoList;
    }

    public List<TodoItem> getDoneList(String username) {
        List<TodoItem> doneList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM todos WHERE username = ? and status = 'DONE'";
        String[] selectionArgs = {username};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        int todoIndex = cursor.getColumnIndex("todo");
        int urgentIndex = cursor.getColumnIndex("urgent");

        while (cursor.moveToNext()) {
            if (todoIndex != -1 && urgentIndex != -1) {
                String todoText = cursor.getString(todoIndex);
                boolean isUrgent = cursor.getInt(urgentIndex) == 1;

                TodoItem todoItem = new TodoItem(username, todoText, isUrgent, "DONE");
                doneList.add(todoItem);
            }
        }

        cursor.close();
        return doneList;
    }
}

