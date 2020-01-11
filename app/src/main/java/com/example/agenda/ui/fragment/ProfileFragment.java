package com.example.agenda.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.agenda.R;
import com.example.agenda.ui.activity.DrawActivity;
import com.example.agenda.ui.data.DatabaseInstance;
import com.example.agenda.ui.data.EventDAO;
import com.example.agenda.ui.data.EventService;
import com.example.agenda.ui.data.UserPrefs;
import com.example.agenda.ui.model.Event;
import com.example.agenda.ui.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private int RESULT_LOAD_IMAGE = 100;
    private int RESULT_DRAW_IMAGE = 200;
    private static int MY_PERMISSIONS_REQUEST = 300;
    private ImageView avatarImage;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText ageEditText;
    private TextView eventsTextView;
    private Button loadImageButton;
    private Button drawButton;


    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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

        loadImage();

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
                String r = getRealPathFromURI(getContext(), imageUri);
                UserPrefs.getInstance().setAvatar(r);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                loadImage();
            }
        }
    }

    private void loadImage() {
        String avatar = UserPrefs.getInstance().getAvatar();
        if (avatar != null && !avatar.isEmpty() && getActivity() != null) {
            try {
                String tt = "file://" + avatar;
                //Uri imageUri = Uri.parse(tt);
                if (ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)
                            && shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,
                                        READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST);

                        // MY_PERMISSIONS_REQUEST is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    File imgFile = new File(tt );
                    if(imgFile.exists()){
                         avatarImage.setImageURI(Uri.fromFile(imgFile));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Utils.loadImageFromURLNoPlaceholders("https://publicdomainvectors.org/photos/abstract-user-flat-4.png", avatarImage);
                Toast.makeText(getActivity(), "Unable to load image", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Utils.loadImageFromURLNoPlaceholders("https://publicdomainvectors.org/photos/abstract-user-flat-4.png", avatarImage);
        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor == null) return "";
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("Path", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
