package com.fenixedu.freeroomsfenix;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class SearchDialogFragment extends SherlockDialogFragment {

	public SearchDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstance) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				getSherlockActivity());

		LayoutInflater inflater = getSherlockActivity().getLayoutInflater();

		builder.setView(inflater.inflate(R.layout.rooms_search_dialog, null));

		builder.setMessage(R.string.dialog_rooms_search_title);

		builder.setPositiveButton(R.string.button_ok, new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});

		return builder.create();
	}

}
