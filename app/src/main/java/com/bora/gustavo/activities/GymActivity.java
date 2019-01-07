package com.bora.gustavo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bora.gustavo.R;
import com.bora.gustavo.activities.adapters.GymImagesRecyclerViewAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class GymActivity extends BackActivity implements GymImagesRecyclerViewAdapter.ItemClickListener {
    private static final int RECYCLER_VIEW_NUMBER_OF_COLUMNS = 3;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = "GymActivity";
    private GymImagesRecyclerViewAdapter mListImagesAdapter;
    private String mCurrentPhotoPath;

    @BindView(R.id.gym_images_picture_taken)
    ImageView mPictureTaken;
    @BindView(R.id.gym_images_recycler_view)
    RecyclerView mImagesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
        ButterKnife.bind(this);

        mImagesRecyclerView.setLayoutManager(new GridLayoutManager(this, RECYCLER_VIEW_NUMBER_OF_COLUMNS));
        Drawable[] data = {getDrawable(R.drawable.ic_accessible),
                /*getDrawable(R.drawable.ic_menu_manage),
                getDrawable(R.drawable.ic_person),
                getDrawable(R.drawable.ic_menu_share),
                getDrawable(R.drawable.ic_menu_send),
                getDrawable(R.drawable.ic_thumb_down_not_voted),
                getDrawable(R.drawable.ic_menu_gallery),
                getDrawable(android.R.drawable.bottom_bar)*/};

        mListImagesAdapter = new GymImagesRecyclerViewAdapter(this, data);
        mListImagesAdapter.setClickListener(this);
        mImagesRecyclerView.setAdapter(mListImagesAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + mListImagesAdapter.getItem(position)
                + ", which is at cell position " + position);
    }

    @OnClick(R.id.gym_fab)
    public void onCameraClicked(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ioe) {
                Log.e(TAG, "Error creating file", ioe);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.bora.gustavo.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            if (extras == null) {
                Log.e(TAG, "Got extras = null");
            } else {
                Log.d(TAG, "Got extras: " + extras);
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mPictureTaken.setImageBitmap(imageBitmap);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null && !storageDir.exists()) {
            boolean folderCreated = storageDir.createNewFile();
            Log.d(TAG, "folderCreated: " + folderCreated);
        }
        File image = File.createTempFile(imageFileName,".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        galleryAddPic();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
