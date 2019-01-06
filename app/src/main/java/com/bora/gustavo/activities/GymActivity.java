package com.bora.gustavo.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bora.gustavo.R;
import com.bora.gustavo.activities.adapters.GymImagesRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GymActivity extends BackActivity implements GymImagesRecyclerViewAdapter.ItemClickListener {
    private static final int RECYCLER_VIEW_NUMBER_OF_COLUMNS = 3;
    private GymImagesRecyclerViewAdapter mListImagesAdapter;

    @BindView(R.id.gym_images_recycler_view)
    RecyclerView mImagesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
        ButterKnife.bind(this);

        mImagesRecyclerView.setLayoutManager(new GridLayoutManager(this, RECYCLER_VIEW_NUMBER_OF_COLUMNS));
        Drawable[] data = {getDrawable(R.drawable.ic_accessible),
                getDrawable(R.drawable.ic_menu_manage),
                getDrawable(R.drawable.ic_person),
                getDrawable(R.drawable.ic_menu_share),
                getDrawable(R.drawable.ic_menu_send),
                getDrawable(R.drawable.ic_thumb_down_not_voted),
                getDrawable(R.drawable.ic_menu_gallery),
                getDrawable(android.R.drawable.bottom_bar)};

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
        
    }
}
