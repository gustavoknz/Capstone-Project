package com.bora.gustavo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

public class NewGymDialogFragment extends DialogFragment {
    private static final String TAG = "NewGymDialogFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() == null || getContext() == null) {
            Log.e(TAG, "Could not retrieve activity or context");
            return null;
        } else {
            // https://developer.android.com/guide/topics/ui/dialogs
            LayoutInflater inflater = getActivity().getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            @SuppressLint("InflateParams")
            View dialogView = inflater.inflate(R.layout.dialog_form_gym, null);

            final GridView equipmentsListGrid = dialogView.findViewById(R.id.form_gym_equipments_grid);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_multiple_choice,
                    getResources().getStringArray(R.array.equipments_list));
            equipmentsListGrid.setAdapter(arrayAdapter);
            equipmentsListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                    Object clickItemObj = adapterView.getAdapter().getItem(index);
                    Toast.makeText(getContext(), "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            builder.setView(dialogView);
            builder.setMessage(R.string.new_gym_dialog_message);
            builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.d(TAG, "User tapped save on the dialog");
                    SparseBooleanArray checked = equipmentsListGrid.getCheckedItemPositions();
                    int selected = 0;
                    for (int i = 0; i < arrayAdapter.getCount(); i++) {
                        if (checked.get(i)) {
                            ++selected;
                        }
                    }
                    Toast.makeText(getContext(), selected + " items selected", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.d(TAG, "User cancelled the dialog");
                }
            });
            return builder.create();
        }
    }
}
