package com.example.Sesiya_FCIT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: WriterMix
 * Date: 11.12.13
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
public class LessonAdapter extends BaseAdapter
{
    public LessonAdapter(Context c, List<Object[]> lessons, int k)
    {
        this.lessons = lessons; this.column = k;
        inflater = LayoutInflater.from(c);
    }

    List<Object[]> lessons;
    int column;
    LayoutInflater inflater;




    @Override
    public int getCount()
    {
        return lessons.size();
    }

    @Override
    public Object getItem(int position) {
        return lessons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object[] lesson =(Object[]) getItem(position) ;
        View v = convertView;
        TextView title;
        if (convertView == null || convertView.getTag()==null){
            v = inflater.inflate(R.layout.row, null);
            title = (TextView) v.findViewById(R.id.title);

            v.setTag(title);
        }
        title = (TextView)v.getTag();
        title.setText(lesson[column].toString());
        return v;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
