package com.example.testest;

import android.app.Dialog;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ListFragment extends Fragment {

    View rootView;
    MaterialCalendarView materialCalendarView;
    ListView listView;
    ArrayAdapter<String> adapter;
    EditText planEditText;
    ArrayList<String> listItem;
    TextView dateTextView;
    androidx.appcompat.widget.AppCompatButton deleteButton;
    androidx.appcompat.widget.AppCompatButton addyooButton;
    androidx.appcompat.widget.AppCompatButton addmooButton;
    HashMap<String, ArrayList<String>> plan_map = new HashMap<>();
    String selectedDate;
    Dialog yooDialog;
    Dialog mooDialog;
    String exerciseData;

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        materialCalendarView = rootView.findViewById(R.id.calendarView);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2020, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(new OnDateDecorator());
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                dateTextView = rootView.findViewById(R.id.dateTextView);
                selectedDate = materialCalendarView.getSelectedDate().toString().split("\\{|\\}")[1];
                String month = Integer.toString(Integer.parseInt(selectedDate.split("-")[1]) + 1);
                String day = selectedDate.split("-")[2];
                selectedDate = selectedDate.split("-")[0] + "-" + month + "-" + day;
                dateTextView.setText(month + " / " + day);
            }
        });

        // listview
        addyooButton = rootView.findViewById(R.id.yooaddButton);
        addyooButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yooDialog = new YooDialog(getContext());
                yooDialog.show();
            }
        });

        addmooButton = rootView.findViewById(R.id.mooaddButton);
        addmooButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mooDialog = new MooDialog(getContext(), selectedDate);
                mooDialog.show();
            }
        });

        listItem = new ArrayList<String>();
        //adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listItem);
        //listView = rootView.findViewById(R.id.listView);
        //listView.setAdapter(adapter);

        try {
            AssetManager assetManager = getActivity().getAssets();
            InputStream is = assetManager.open("exercise.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }
            // json 파일 내용이 string으로 변환되어 담김
            exerciseData = buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        Gson gson = new Gson();
        final Exercise data1 = gson.fromJson(exerciseData, Exercise.class);

        Log.d("data1.name", data1.name); */


        // rootview로 작성해주어야 갱신된 값이 반영됨
        return rootView;
    }
    }