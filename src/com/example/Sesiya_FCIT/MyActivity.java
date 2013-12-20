package com.example.Sesiya_FCIT;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.Sesiya_FCIT.DatabaseHelper;
import com.example.Sesiya_FCIT.R;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity
{
    /**
     * Called when the activity is first created.
     */

    DatabaseHelper myDbHelper;
    SQLiteDatabase myDB;

    List<String> teachers;
    List<String> groups;

    List<Object[]> lessons = new ArrayList<Object[]>();

    ListView lvDate;
    ListView lvGroup;
    ListView lvTeacher;
    ListView lvSubject;
    ListView lvRoom;
    ListView lvTypeOfLesson;

    Spinner spnrTeachers;
    Spinner spnrGroups;

    TextView tvShowOnlyName;

    boolean isFirstListEnabled = true;
    boolean isSecondListEnabled = true;
    boolean isThirdListEnabled = true;
    boolean isFourthListEnabled = true;
    boolean isFifthListEnabled = true;
    boolean isSixthListEnabled = true;
    private String groupFromSpinner;
    private int listViewGroupWidth;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tvShowOnlyName = (TextView)findViewById(R.id.tvShowOnlyName);

        lvDate = (ListView) findViewById(R.id.lvDate);
        lvGroup = (ListView) findViewById(R.id.lvGroup);
        lvTeacher = (ListView) findViewById(R.id.lvTeacher);
        lvSubject = (ListView) findViewById(R.id.lvSubject);
        lvRoom = (ListView) findViewById(R.id.lvRoom);
        lvTypeOfLesson = (ListView)findViewById(R.id.lvTypeOfLesson);

        lvGroup.setCacheColorHint(Color.TRANSPARENT);
        lvDate.setCacheColorHint(Color.TRANSPARENT);
        lvRoom.setCacheColorHint(Color.TRANSPARENT);
        lvTypeOfLesson.setCacheColorHint(Color.TRANSPARENT);
        lvSubject.setCacheColorHint(Color.TRANSPARENT);
        lvTeacher.setCacheColorHint(Color.TRANSPARENT);

        spnrTeachers = (Spinner)findViewById(R.id.spnrTeachers);
        spnrGroups = (Spinner) findViewById(R.id.spnrGroups);


        final ListView[] allLv = {lvGroup, lvDate, lvSubject, lvTypeOfLesson, lvTeacher, lvRoom};

        myDbHelper = new DatabaseHelper(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }


        try
        {
            myDbHelper.openDataBase();
        } catch (SQLException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        myDB = this.openOrCreateDatabase("tte.db", MODE_PRIVATE, null);


        Cursor rdrTeachers = myDB.query(true, "timetable", new String[] {"teacher"}, null, null, null, null, "teacher", null);
        Cursor rdrGroups = myDB.query(true, "timetable", new String[]{"st_group"}, null, null, null, null, "st_group", null);

        teachers = new ArrayList<String>();
        groups = new ArrayList<String>();

        while (rdrTeachers.moveToNext())
        {
            teachers.add(rdrTeachers.getString(0));
        }
        teachers.remove(0);
        teachers.add(0, "Оберіть викладача: ");


        while (rdrGroups.moveToNext())
            groups.add((rdrGroups.getString(0)));
        groups.add(0, "Оберіть групу: ");

        ArrayAdapter<String> spnrAdapterTeacher = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teachers);
        spnrTeachers.setAdapter(spnrAdapterTeacher);

        ArrayAdapter<String> spnrAdapterGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groups);
        spnrGroups.setAdapter(spnrAdapterGroup);

        //rdrTeachers = myDB.query("timetable",null,null,null,null,null,"lesson_time");

        spnrTeachers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                lessons.clear();
                for (int k = 0; k < allLv.length; k++)
                {
                    allLv[k].setAdapter(new LessonAdapter(getApplicationContext(), lessons, k));
                }
                String teacherFromSpinner = spnrTeachers.getSelectedItem().toString();

                Cursor rdrGlobal = myDB.rawQuery("select * from timetable where teacher = ? order by lesson_time", new String[]{teacherFromSpinner});

                while (rdrGlobal.moveToNext())
                {
                    Object[] lesson = new Object[6];
                    for (int k = 0; k < lesson.length; k++)
                        lesson[k] = rdrGlobal.getString(1 + k);
                    lessons.add(lesson);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        spnrGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                lessons.clear();
                for (int k = 0; k < allLv.length; k++)
                {
                    allLv[k].setAdapter(new LessonAdapter(getApplicationContext(), lessons, k));
                }
                groupFromSpinner = spnrGroups.getSelectedItem().toString();

                Cursor rdrGlobal = myDB.rawQuery("select * from timetable where st_group = ? order by lesson_time", new String[]{groupFromSpinner});

                while (rdrGlobal.moveToNext())
                {
                    Object[] lesson = new Object[6];
                    for (int k = 0; k < lesson.length; k++)
                        lesson[k] = rdrGlobal.getString(1 + k);
                    lessons.add(lesson);
                }
//
//                if(!(groupFromSpinner.equals("Оберіть групу: ")))
//                {
//
//                listViewGroupWidth = lvGroup.getWidth();
//
//                tvShowOnlyName.setWidth(listViewGroupWidth);
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

    }

}
