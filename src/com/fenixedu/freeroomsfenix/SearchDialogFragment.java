package com.fenixedu.freeroomsfenix;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class SearchDialogFragment extends SherlockDialogFragment {

	FenixFreeRoomsApplication application;

	public SearchDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstance) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				getSherlockActivity());
		application = (FenixFreeRoomsApplication) getSherlockActivity()
				.getApplication();

		LayoutInflater inflater = getSherlockActivity().getLayoutInflater();

		final View view = inflater.inflate(R.layout.rooms_search_dialog, null);

		builder.setView(view);

		builder.setMessage(R.string.dialog_rooms_search_title);

		builder.setPositiveButton(R.string.button_ok, new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				TimePicker timePicker = (TimePicker) view
						.findViewById(R.id.timerpicker_rooms_search);
				application.setSearchHour(timePicker.getCurrentHour());
				application.setSearchMinute(timePicker.getCurrentMinute());

			}
		});

		return builder.create();
	}

}
