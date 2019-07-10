package com.gaur.alarmclock;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class Timer extends Fragment implements View.OnClickListener {

    private EditText TextInput;
    private TextView TextCountDown;
    private TextView Set;
    private FloatingActionButton StartPause;
    private FloatingActionButton Reset;
    private FloatingActionButton Stop;
    private CountDownTimer countDownTimer;
    private boolean TimerRunning;
    private long StartTimeInMillis;
    private long TimeLeftInMillis;
    private long EndTime;
     MediaPlayer mpAudio;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_timer, container, false);
        View layout = inflater.inflate(R.layout.fragment_timer, container, false);

        TextInput = layout.findViewById(R.id.input_time);
        Set = layout.findViewById(R.id.set);
        Set.setOnClickListener(this);
        TextCountDown = layout.findViewById(R.id.timer);
        StartPause = layout.findViewById(R.id.startpause);
        StartPause.setOnClickListener(this);
        Reset = layout.findViewById(R.id.reset);
        Reset.setOnClickListener(this);
        Stop = layout.findViewById(R.id.stopsound);
        Stop.setOnClickListener(this);
        Stop.hide();
        setHasOptionsMenu(true);


        return layout;

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }



    @Override

    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.startpause:
                onClickPlayPause();
                break;

            case R.id.set:
                onClickSet();
                break;

            case R.id.reset:
                onClickReset();
                break;



        }
    }





    public void onClickSet(){

        String input = TextInput.getText().toString();
        if (input.length()==0){
            Toast.makeText(getActivity(), "Field Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        long millisInput = Long.parseLong(input)*60000;
        if (millisInput == 0) {
            Toast.makeText(getActivity(), "Please enter a positive number", Toast.LENGTH_SHORT).show();
            return;
        }

        setTime(millisInput);
        TextInput.setText("");

    }

    public void onClickPlayPause(){

        if (TimerRunning) {
            pauseTimer();
        } else {
            startTimer();
        }

    }

    public void onClickReset(){

        resetTimer();

    }

    private void setTime(long milliseconds) {
        StartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer() {
        EndTime = System.currentTimeMillis() + TimeLeftInMillis;

        countDownTimer = new CountDownTimer(TimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeftInMillis = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                TimerRunning = false;
                updateWatchInterface();
                playSound();
            }
        }.start();

        TimerRunning = true;
        updateWatchInterface();
    }

    public void playSound() {
        final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), (R.raw.timer_end));


        mp.start();
        if (mp.isPlaying()){

            Stop.show();
            Stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.stop();
                }
            });
        }
        else {
            Stop.hide();
        }



    }

    private void pauseTimer() {
        countDownTimer.cancel();
        TimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        TimeLeftInMillis = StartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }


    private void updateCountDownText() {
        int hours = (int) (TimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((TimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (TimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        TextCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (TimerRunning) {
            TextInput.setVisibility(View.INVISIBLE);
            Set.setVisibility(View.INVISIBLE);
           // Reset.setVisibility(View.INVISIBLE);
            //StartPause.setText("Pause");
        } else {
            TextInput.setVisibility(View.VISIBLE);
            Set.setVisibility(View.VISIBLE);
            //StartPause.setText("Start");

//            if (TimeLeftInMillis < 1000) {
//                StartPause.setVisibility(View.INVISIBLE);
//            } else {
//                StartPause.setVisibility(View.VISIBLE);
//            }

//            if (mTimeLeftInMillis < mStartTimeInMillis) {
//                mButtonReset.setVisibility(View.VISIBLE);
//            } else {
//                mButtonReset.setVisibility(View.INVISIBLE);
//            }
        }
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", StartTimeInMillis);
        editor.putLong("millisLeft", TimeLeftInMillis);
        editor.putBoolean("timerRunning", TimerRunning);
        editor.putLong("endTime", EndTime);

        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);

        StartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        TimeLeftInMillis = prefs.getLong("millisLeft", StartTimeInMillis);
        TimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (TimerRunning) {
            EndTime = prefs.getLong("endTime", 0);
            TimeLeftInMillis = EndTime - System.currentTimeMillis();

            if (TimeLeftInMillis < 0) {
                TimeLeftInMillis = 0;
                TimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }



}
