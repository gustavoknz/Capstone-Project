package com.bora.gustavo.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.bora.gustavo.R;
import com.bora.gustavo.activities.adapters.GymImagesRecyclerViewAdapter;
import com.bora.gustavo.helper.Utils;
import com.bora.gustavo.models.EquipmentParcelable;
import com.bora.gustavo.models.GymParcelable;
import com.bora.gustavo.models.Vote;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GymActivity extends BackActivity {
    private static final String TAG = "GymActivity";
    private boolean mIsFavorite;
    private DatabaseReference mDatabase;
    private GymParcelable mGym;

    @BindView(R.id.gym_favorite)
    ImageView mFavoriteImage;
    @BindView(R.id.gym_recycler_view_equipments)
    RecyclerView mRecyclerViewEquipments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
        ButterKnife.bind(this);

        mGym = getIntent().getParcelableExtra(Utils.PARAM_GYM);
        Log.d(TAG, "Got mGym as parameter: " + mGym);

        mRecyclerViewEquipments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        String[] data = new String[mGym.getEquipmentList().size()];
        int i = 0;
        for (EquipmentParcelable ep : mGym.getEquipmentList()) {
            data[i++] = ep.getName();
        }

        Log.d(TAG, String.format("This mGym has %d equipments", data.length));
        GymImagesRecyclerViewAdapter adapter = new GymImagesRecyclerViewAdapter(getApplicationContext(), data);
        mRecyclerViewEquipments.setAdapter(adapter);

        setFavoriteImage();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @OnClick(R.id.gym_favorite)
    public void onFavoriteClicked() {
        mIsFavorite = !mIsFavorite;
        if (mIsFavorite) {
            addFavorite();
        } else {
            String userId = Utils.getUserUid();
            Log.d(TAG, "Searching gym with user = " + userId);
            removeFavorite();
        }
        setFavoriteImage();
    }

    private void addFavorite() {
        Vote vote = new Vote();
        String userId = Utils.getUserUid();
        if (userId == null) {
            Log.e(TAG, "Could not vote with userId = null");
        } else {
            vote.setUserId(userId);
            vote.setGymId(mGym.getId());
            vote.setUp(true);
            vote.setDate(new Date());
            Log.d(TAG, "Adding vote " + vote);
            String voteKey = mDatabase.push().getKey();
            mDatabase.child(voteKey).setValue(vote, (firebaseError, ref) -> {
                if (firebaseError != null) {
                    Log.e(TAG, "New vote could not be saved: " + firebaseError.getMessage());
                } else {
                    Log.d(TAG, "New vote saved successfully.");
                }
            });
        }
    }

    private void removeFavorite() {

    }

    private void setFavoriteImage() {
        mFavoriteImage.setImageResource(mIsFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_not);
    }
}
