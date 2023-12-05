package com.tminc.taskmanage;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tminc.taskmanage.R;

public class RegisterButtonFragment extends Fragment {

    private OnRegisterButtonClickListener callback;

    public interface OnRegisterButtonClickListener {
        void onRegisterButtonClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnRegisterButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRegisterButtonClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_button, container, false);
        Button registerButton = view.findViewById(R.id.button_register);
        registerButton.setOnClickListener(v -> callback.onRegisterButtonClick());
        return view;
    }
}