package com.bora.gustavo.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bora.gustavo.R;
import com.bora.gustavo.activities.adapters.GymEquipmentsRecyclerViewAdapter;
import com.bora.gustavo.helper.Utils;
import com.bora.gustavo.models.EquipmentParcelable;
import com.bora.gustavo.models.GymParcelable;
import com.bora.gustavo.models.Vote;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GymActivity extends BackActivity {
    private static final String TAG = "GymActivity";
    private boolean mIsFavorite = false;
    private DatabaseReference mDatabaseVotes;
    private GymParcelable mGym;

    @BindView(R.id.gym_favorite)
    ImageView mFavoriteImage;
    @BindView(R.id.gym_recycler_view_equipments)
    RecyclerView mRecyclerViewEquipments;
    @BindView(R.id.gym_accessible_text)
    TextView mAccessibleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
        ButterKnife.bind(this);
        mGym = getIntent().getParcelableExtra(Utils.PARAM_GYM);
        Log.d(TAG, "Got mGym as parameter: " + mGym);

        setFavoriteImage();
        setAccessibleText();

        mRecyclerViewEquipments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        String[] data = new String[mGym.getEquipmentList().size()];
        int i = 0;
        for (EquipmentParcelable ep : mGym.getEquipmentList()) {
            data[i++] = ep.getName();
        }

        Log.d(TAG, String.format("This mGym has %d equipments", data.length));
        GymEquipmentsRecyclerViewAdapter adapter = new GymEquipmentsRecyclerViewAdapter(getApplicationContext(), data);
        mRecyclerViewEquipments.setAdapter(adapter);

        getFavoriteInfo();
    }

    private void getFavoriteInfo() {
        mDatabaseVotes = FirebaseDatabase.getInstance().getReference("votes");
        mDatabaseVotes.orderByChild("userId").equalTo(Utils.getUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange; post: " + post);
                        HashMap voteMap = (HashMap) post.getValue();
                        //noinspection ConstantConditions
                        if (voteMap != null && voteMap.containsKey("gymId") && voteMap.get("gymId").equals(mGym.getId())) {
                            Log.d(TAG, "My favorite! S2");
                            mIsFavorite = true;
                            return;
                        }
                    }
                    mIsFavorite = false;
                    Log.d(TAG, "NOT favorite... yet");
                } finally {
                    setFavoriteImage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled; post: " + databaseError);
            }
        });
    }

    private void setAccessibleText() {
        mAccessibleText.setText(mGym.isPcdAble() ? R.string.gym_accessible_yes : R.string.gym_accessible_no);
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
            String voteKey = mDatabaseVotes.push().getKey();
            if (voteKey == null) {
                Log.wtf(TAG, "voteKey is null. Weird");
            } else {
                mDatabaseVotes.child(voteKey).setValue(vote, (firebaseError, ref) -> {
                    if (firebaseError == null) {
                        Log.d(TAG, "New vote saved successfully.");
                    } else {
                        Log.e(TAG, "New vote could not be saved: " + firebaseError.getMessage());
                    }
                });
            }
        }
    }

    private void removeFavorite() {
        mDatabaseVotes = FirebaseDatabase.getInstance().getReference("votes");
        mDatabaseVotes.orderByChild("userId").equalTo(Utils.getUserUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            HashMap voteMap = (HashMap) postSnapshot.getValue();
                            //noinspection ConstantConditions
                            if (voteMap != null && voteMap.containsKey("gymId")
                                    && voteMap.get("gymId").equals(mGym.getId())) {
                                Log.d(TAG, "Value to be removed: " + voteMap);
                                postSnapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "Error trying to remove value: " + databaseError);
                    }
                });
    }

    private void setFavoriteImage() {
        mFavoriteImage.setImageResource(mIsFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_not);
    }
}
