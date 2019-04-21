package com.example.assignment4.View;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.assignment4.R;

import java.util.Calendar;

public class SearchMenuDialog extends BottomSheetDialogFragment {
    EditText getDateText, getTimeText;
    TextView getCurrentSeek, getMaxSeek;
    SeekBar seekBar;
    Calendar cal;
    int currentHour, currentMinute;
    String amPm;

    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog timePickerDialog;
    public static final String TAG = "SearchMenuDialog";




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_menu,container,false);
        getDateText = view.findViewById(R.id.et_date);
        getTimeText= view.findViewById(R.id.et_time);
        valueSeekbar(view);

        getDateText.setShowSoftInputOnFocus(false);
        getTimeText.setShowSoftInputOnFocus(false);

        getDateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            dateSetListener, year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }

        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                Log.d(TAG, "onDateSet: date "+month+"/"+dayOfMonth+"/"+year);
                String date = month+"/"+dayOfMonth+"/"+year;
                getDateText.setText(date);
            }
        };

        getTimeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cal = Calendar.getInstance();
                    currentHour = cal.get(Calendar.HOUR_OF_DAY);
                    currentMinute = cal.get(Calendar.MINUTE);

                    timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            if (hourOfDay >= 12) {
                                amPm = "PM";
                            } else {
                                amPm = "AM";
                            }
                            getTimeText.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm);
                        }
                    }, currentHour, currentMinute, false);
                    timePickerDialog.show();
                }
            }


        });

        return view;
    }

    public void valueSeekbar(View view){
        seekBar = view.findViewById(R.id.seek_reserve_minutes);
        getCurrentSeek = view.findViewById(R.id.tv_current_seek);
        getMaxSeek = view.findViewById(R.id.tv_max_seek);



        getCurrentSeek.setText(seekBar.getProgress()+" minutes");
        getMaxSeek.setText(seekBar.getMax()+" minutes");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
                getCurrentSeek.setText(progress+" minutes");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                getCurrentSeek.setText(progressVal+" minutes");
            }
        });
    }

}
