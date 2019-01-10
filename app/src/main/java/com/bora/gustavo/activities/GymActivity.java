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
import com.bora.gustavo.models.Gym;
import com.bora.gustavo.models.GymParcelable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GymActivity extends BackActivity {
    private static final String TAG = "GymActivity";
    private boolean mIsFavorite;

    @BindView(R.id.gym_favorite)
    ImageView mFavoriteImage;
    @BindView(R.id.gym_recycler_view_equipments)
    RecyclerView mRecyclerViewEquipments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
        ButterKnife.bind(this);

        GymParcelable gym = getIntent().getParcelableExtra(Utils.PARAM_GYM);
        Log.d(TAG, "Got gym as parameter: " + gym);

        mRecyclerViewEquipments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        String[] data = new String[gym.getEquipmentList().size()];
        int i = 0;
        for (EquipmentParcelable ep : gym.getEquipmentList()) {
            data[i++] = ep.getName();
        }

        Log.d(TAG, String.format("This gym has %d equipments", data.length));
        GymImagesRecyclerViewAdapter adapter = new GymImagesRecyclerViewAdapter(getApplicationContext(), data);
        mRecyclerViewEquipments.setAdapter(adapter);

        setFavoriteImage();
    }

    @OnClick(R.id.gym_favorite)
    public void onFavoriteClicked() {
        setFavoriteImage();
        mIsFavorite = !mIsFavorite;
    }

    private void setFavoriteImage() {
        mFavoriteImage.setImageResource(mIsFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_not);
    }
}
