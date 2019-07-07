package com.gaur.alarmclock;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class StopWatch extends Fragment implements View.OnClickListener {


    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    Handler handler;

    int Seconds, Minutes, MilliSeconds ;

    ListView listView ;

    String[] ListElements = new String[] {  };

    List<String> ListElementsArrayList ;

    ArrayAdapter<String> adapter ;

    TextView time_view;
    FloatingActionButton play;
    FloatingActionButton pause;
    FloatingActionButton reset;
    TextView Lap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_stop_watch, container, false);

         play = layout.findViewById(R.id.fab_play);
        play.setOnClickListener(this);
         pause = layout.findViewById(R.id.fab_pause);
        pause.setOnClickListener(this);
         reset = layout.findViewById(R.id.fab_reset);
         Lap = layout.findViewById(R.id.Lap);
        Lap.setOnClickListener(this);
        reset.setOnClickListener(this);
        listView = layout.findViewById(R.id.list_view);
        time_view = layout.findViewById(R.id.time_text);

        handler= new Handler();

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_layout,
                ListElementsArrayList
        );


        listView.setAdapter(adapter);
        return layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fab_play:
                onClickPlay();
                break;

            case R.id.fab_pause:
                onClickpause();
                break;

            case R.id.fab_reset:
                onClickReset();
                break;

            case R.id.Lap:

                onClickLap(time_view);
        }
    }

    public void onClickPlay() {

        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        reset.setEnabled(false);

    }

    public void onClickReset() {

        MillisecondTime = 0L ;
        StartTime = 0L ;
        TimeBuff = 0L ;
        UpdateTime = 0L ;
        Seconds = 0 ;
        Minutes = 0 ;
        MilliSeconds = 0 ;

        time_view.setText("00:00:00");

        ListElementsArrayList.clear();

        adapter.notifyDataSetChanged();


    }


    public void onClickpause() {

        TimeBuff += MillisecondTime;

        handler.removeCallbacks(runnable);

        reset.setEnabled(true);


    }

    public void onClickLap(View view){
        //    final TextView  time_view = view.findViewById(R.id.time_text);
        ListElementsArrayList.add(time_view.getText().toString());

        adapter.notifyDataSetChanged();

    }

    public Runnable runnable = new Runnable() {

        View view ;
     //   final TextView  time_view = view.findViewById(R.id.time_text);
        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            time_view.setText(String.format("%d:%s:%s", Minutes, String.format("%02d", Seconds), String.format("%03d", MilliSeconds)));

            handler.postDelayed(this, 0);
        }

    };




    @Override

    public void onPause() {
        super.onPause();



    }

    @Override

    public void onResume() {
        super.onResume();

    }


    }



