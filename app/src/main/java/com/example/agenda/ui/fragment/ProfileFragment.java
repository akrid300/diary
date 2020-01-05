package com.example.agenda.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.agenda.R;
import com.example.agenda.ui.data.UserPrefs;
import com.example.agenda.ui.utils.Utils;

public class ProfileFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView avatar = root.findViewById(R.id.profileAvatar);
        final EditText nameEditText = root.findViewById(R.id.profileName);
        final EditText emailEditText = root.findViewById(R.id.profileEmail);
        final EditText ageEditText = root.findViewById(R.id.profileAge);
        final TextView eventsEditText = root.findViewById(R.id.profileEvents);

        Utils.loadImageFromURLNoPlaceholders("https://publicdomainvectors.org/photos/abstract-user-flat-4.png", avatar);

        String name = UserPrefs.getInstance().getUserName();
        String email = UserPrefs.getInstance().getUserEmail();
        Integer age = UserPrefs.getInstance().getUserAge();

        if (name != null && !name.trim().isEmpty()) {
            nameEditText.setText(name);
        }

        if (email != null && !email.trim().isEmpty()) {
            emailEditText.setText(email);
        }

        if (age != null) {
            ageEditText.setText(age);
        }




        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String newName = nameEditText.getText() != null ? nameEditText.getText().toString() : "";
                    if (!Utils.isStringNullOrEmpty(newName)) {
                        UserPrefs.getInstance().setUserNmae(newName);
                    }
                }
            }
        });

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newEmail = emailEditText.getText() != null ? emailEditText.getText().toString() : "";
                    if (!Utils.isStringNullOrEmpty(newEmail)) {
                        UserPrefs.getInstance().setUserEmail(newEmail);
                    }
                }
            }
        });

        ageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newAge = ageEditText.getText() != null ? ageEditText.getText().toString() : "";

                    if (!Utils.isStringNullOrEmpty(newAge)){
                        UserPrefs.getInstance().setUserAge(Integer.parseInt(newAge));
                    }
                }
            }
        });

//        eventsEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    String newEvents = eventsEditText.getText() != null ? eventsEditText.getText().toString() : "";
//                    if (!Utils.isStringNullOrEmpty(newEvents)){
//                        UserPrefs.getInstance().setUserAge(Integer.parseInt(newEvents));
//                    }
//                }
//            }
//        });

        return root;
    }
}
