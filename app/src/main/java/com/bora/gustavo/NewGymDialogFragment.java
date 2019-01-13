package com.bora.gustavo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;

import com.bora.gustavo.activities.MainCallback;
import com.bora.gustavo.helper.LocationHolderSingleton;
import com.bora.gustavo.helper.Utils;
import com.bora.gustavo.models.Equipment;
import com.bora.gustavo.models.Gym;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewGymDialogFragment extends DialogFragment {
    private static final String TAG = "NewGymDialogFragment";
    private DatabaseReference mDatabase;
    private MainCallback mMainCallback;

    @BindView(R.id.form_gym_address)
    EditText mAddressView;

    @BindView(R.id.form_gym_equipments_grid)
    GridView mEquipmentsListGrid;

    @BindView(R.id.form_gym_pcd_able)
    CheckBox mPcdAble;

    public void setCallback(MainCallback mainCallback) {
        mMainCallback = mainCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() == null || getContext() == null) {
            Log.e(TAG, "Could not retrieve activity or context");
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Where is my context? o.O");
            return builder.create();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference("gyms");
        String gymKey = mDatabase.push().getKey();

        // https://developer.android.com/guide/topics/ui/dialogs
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams")
        View dialogView = inflater.inflate(R.layout.dialog_form_gym, null);
        ButterKnife.bind(this, dialogView);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_multiple_choice,
                getResources().getStringArray(R.array.equipments_list));
        mEquipmentsListGrid.setAdapter(arrayAdapter);

        builder.setView(dialogView);
        builder.setMessage(R.string.new_gym_dialog_message);
        builder.setPositiveButton(R.string.new_gym_dialog_save, null);
        builder.setNegativeButton(R.string.new_gym_dialog_cancel, (dialog, id) -> Log.d(TAG, "User cancelled the dialog"));
        Dialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                Log.d(TAG, "User tapped save on the dialog");
                String address = mAddressView.getText().toString().trim();
                if (TextUtils.isEmpty(address)) {
                    Utils.showSnackbar(getActivity().findViewById(android.R.id.content), R.string.snackbar_close, R.string.new_gym_fail_address);
                    return;
                }
                SparseBooleanArray checked = mEquipmentsListGrid.getCheckedItemPositions();
                List<Equipment> equipmentList = new ArrayList<>(checked.size());
                for (int i = 0; i < arrayAdapter.getCount(); i++) {
                    if (checked.get(i)) {
                        equipmentList.add(new Equipment(i, arrayAdapter.getItem(i)));
                    }
                }
                if (equipmentList.isEmpty()) {
                    Utils.showSnackbar(getActivity().findViewById(android.R.id.content), R.string.snackbar_close, R.string.new_gym_fail_equipment);
                    return;
                }
                LocationHolderSingleton locationSingleton = LocationHolderSingleton.getInstance();
                Gym newGym = new Gym();
                newGym.setId(Utils.createUuid());
                if (locationSingleton != null && locationSingleton.getLocation() != null) {
                    newGym.setLatitude(locationSingleton.getLocation().getLatitude());
                    newGym.setLongitude(locationSingleton.getLocation().getLongitude());
                } else {
                    newGym.setLatitude(0f);
                    newGym.setLongitude(0f);
                }
                newGym.setVotesDown(0);
                newGym.setVotesUp(0);
                newGym.setAddress(mAddressView.getText().toString());
                String userId = Utils.getUserUid();
                if (userId == null) {
                    Utils.showSnackbar(getActivity().findViewById(android.R.id.content), R.string.snackbar_close, R.string.new_gym_not_logged);
                    dialog.dismiss();
                }
                newGym.setUserId(userId);
                newGym.setRegisteredAt(new Date());
                newGym.setPcdAble(mPcdAble.isChecked());
                newGym.setEquipmentList(equipmentList);

                if (gymKey == null) {
                    Log.wtf(TAG, "Gym key = null. Weird.");
                    Utils.showSnackbar(getActivity().findViewById(android.R.id.content), R.string.snackbar_close, R.string.new_gym_key_null);
                } else {
                    mDatabase.child(gymKey).setValue(newGym, (firebaseError, ref) -> {
                        if (firebaseError != null) {
                            Log.e(TAG, "New gym could not be saved: " + firebaseError.getMessage());
                        } else {
                            Log.d(TAG, "New gym saved successfully.");
                            mMainCallback.onNewGymAdded(newGym);
                        }
                    });
                }
                Log.d(TAG, "Adding a new gym: " + newGym);
                dialog.dismiss();
            });
        });
        return dialog;
    }
}
