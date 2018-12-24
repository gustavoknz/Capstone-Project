package com.bora.gustavo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.bora.gustavo.models.Equipment;
import com.bora.gustavo.models.Gym;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewGymDialogFragment extends DialogFragment {
    private static final String TAG = "NewGymDialogFragment";
    @BindView(R.id.form_gym_equipments_grid)
    GridView mEquipmentsListGrid;

    @BindView(R.id.form_gym_address)
    EditText mAddressView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() == null || getContext() == null) {
            Log.e(TAG, "Could not retrieve activity or context");
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("What happened? o.O");
            return builder.create();
        }
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
        mEquipmentsListGrid.setOnItemClickListener((adapterView, view, index, l) -> {
            Object clickItemObj = adapterView.getAdapter().getItem(index);
            Toast.makeText(getContext(), "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
        });

        builder.setView(dialogView);
        builder.setMessage(R.string.new_gym_dialog_message);
        builder.setPositiveButton(R.string.save, (dialog, id) -> {
            Log.d(TAG, "User tapped save on the dialog");
            SparseBooleanArray checked = mEquipmentsListGrid.getCheckedItemPositions();
            Toast.makeText(getContext(), checked.size() + " items selected", Toast.LENGTH_SHORT).show();
            Gym newGym = new Gym();
            List<Equipment> equipmentList = new ArrayList<>(checked.size());
            for (int i = 0; i < arrayAdapter.getCount(); i++) {
                if (checked.get(i)) {
                    equipmentList.add(new Equipment(i, arrayAdapter.getItem(i)));
                }
            }
            newGym.setId("0");
            newGym.setLatitude(0f);
            newGym.setLongitude(0f);
            newGym.setVotesDown(0);
            newGym.setVotesUp(0);
            newGym.setAddress(mAddressView.getText().toString());
            newGym.setRegisteredAt(new Date());
            newGym.setEquipmentList(equipmentList);
            Log.d(TAG, "Adding a new gym: " + newGym);
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> Log.d(TAG, "User cancelled the dialog"));
        return builder.create();
    }
}
