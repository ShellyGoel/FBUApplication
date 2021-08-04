package com.example.fbuapplication.fragments.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.fbuapplication.R;

public class DecidePinnedWallDialogFragment extends DialogFragment {

    private boolean[] selectedItems = new boolean[]{false, false, false, true,
            true, false};
    private final boolean[] clickedItems = selectedItems;

    public void sendBackResult(boolean[] toSend) {

        DecidePinnedWallDialogListener listener = (DecidePinnedWallDialogListener) getTargetFragment();
        listener.onFinishDecidePinnedWallDialog(toSend);
        dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] languages = getResources().getStringArray(
                R.array.walls_array);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity())
                .setTitle("Which wall do you want to pin the note to?")
                .setMultiChoiceItems(R.array.walls_array, selectedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                clickedItems[which] = isChecked;

                            }
                        })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItems = clickedItems;
                        sendBackResult(selectedItems);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = alertDialogBuilder.create();
        return dialog;
    }

    public interface DecidePinnedWallDialogListener {
        void onFinishDecidePinnedWallDialog(boolean[] toSend);
    }

}


