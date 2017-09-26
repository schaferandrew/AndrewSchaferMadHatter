package edu.schaf170msu.andrewschafermadhatter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class HatterActivity extends AppCompatActivity {
    /**
     * The hatter view object
     */
    private HatterView getHatterView() {
        return (HatterView) findViewById(R.id.hatterView);
    }

    /**
     * The color select button
     */
    private Button getColorButton() {
        return (Button)findViewById(R.id.buttonColor);
    }

    /**
     * The feather checkbox
     */
    private CheckBox getFeatherCheck() {
        return (CheckBox)findViewById(R.id.checkFeather);
    }

    /**
     * The hat choice spinner
     */
    private Spinner getSpinner() {
        return (Spinner) findViewById(R.id.spinnerHat);
    }

    /**
     * Request code when selecting a picture
     */
    private static final int SELECT_PICTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hatter);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    /**
     * Function called when we get a result from some external
     * activity called with startActivityForResult()
     * @param requestCode the request code we sent to the activity
     * @param resultCode a result of from the activity - ok or cancelled
     * @param data data from the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            // Response from the picture selection activity
            Uri imageUri = data.getData();

            // We have to query the database to determine the document ID for the image
            Cursor cursor = getContentResolver().query(imageUri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":")+1);
            cursor.close();

            // Next, we query the content provider to find the path for this
            // document id.
            cursor = getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();

            if(path != null) {
                Log.i("Path", path);
                getHatterView().setImagePath(path);
            }

        }
    }

    /**
     * Handle a Picture button press
     * @param view
     */
    public void onPicture(View view) {
        // Get a picture from the gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }
}
