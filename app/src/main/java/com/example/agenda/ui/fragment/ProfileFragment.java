package com.example.agenda.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agenda.R;
import com.example.agenda.ui.activity.DrawActivity;
import com.example.agenda.ui.data.DatabaseInstance;
import com.example.agenda.ui.data.EventDAO;
import com.example.agenda.ui.data.EventService;
import com.example.agenda.ui.data.UserPrefs;
import com.example.agenda.ui.model.Event;
import com.example.agenda.ui.utils.Utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private int RESULT_LOAD_IMAGE = 100;
    private int RESULT_DRAW_IMAGE = 200;
    private ImageView avatarImage;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText ageEditText;
    private TextView eventsTextView;
    private Button loadImageButton;
    private Button drawButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        avatarImage = root.findViewById(R.id.profileAvatar);
        nameEditText = root.findViewById(R.id.profileName);
        emailEditText = root.findViewById(R.id.profileEmail);
        ageEditText = root.findViewById(R.id.profileAge);
        eventsTextView = root.findViewById(R.id.profileEvents);
        loadImageButton = root.findViewById(R.id.buttonLoadImage);
        drawButton = root.findViewById(R.id.buttonnDraw);

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
            String ageString = age.toString();
            ageEditText.setText(ageString);
        }

        String avatar = UserPrefs.getInstance().getAvatar();
        if (avatar != null && !avatar.isEmpty() && getActivity() != null) {
            try {
                Uri imageUri = Uri.parse(avatar);
                InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(selectedImage);
                UserPrefs.getInstance().setAvatar(imageUri.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Utils.loadImageFromURLNoPlaceholders("https://publicdomainvectors.org/photos/abstract-user-flat-4.png", avatarImage);
        }


        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DrawActivity.class);
                startActivityForResult(intent, RESULT_DRAW_IMAGE);
            }
        });
        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Load image from gallery
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        });

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


        DatabaseInstance databaseInstance = DatabaseInstance.getInstance(getContext());
        EventDAO eventDAO = databaseInstance.eventDAO();
        EventService eventService = new EventService(eventDAO);

        List<Event> events = eventService.getEvents();
        Integer count = events != null ? events.size() : 0;
        String eventsNumber = count.toString();


        eventsTextView.setText(eventsNumber);

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Show image on the profile
        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE && data != null && data.getData() != null && getActivity() != null) {
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                avatarImage.setImageBitmap(selectedImage);
                UserPrefs.getInstance().setAvatar(imageUri.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Unable to get image", Toast.LENGTH_LONG).show();
            }

        }
        else if (resultCode == RESULT_OK && requestCode == RESULT_DRAW_IMAGE && data != null && getActivity() != null) {
            byte[] byteArray = data.getByteArrayExtra("drawing");
            if (byteArray != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                avatarImage.setImageBitmap(bmp);
            }
        }
    }
}
