package com.hanul.team1.triplan.ysh.listview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanul.team1.triplan.R;

public class DayListView extends LinearLayout {
    TextView tvDay, tvDate1, tvDate2, tvPlaceName2, tvCntPlace2;

    public DayListView(Context context) {
        super(context);
        init(context);
    }

    public DayListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sh_listview_day, this, true);

        tvDay = findViewById(R.id.tvDay);
        tvDate1 = findViewById(R.id.tvDate1);
        tvDate2 = findViewById(R.id.tvDate2);
        tvPlaceName2 = findViewById(R.id.tvPlaceName2);
        tvCntPlace2 = findViewById(R.id.tvCntPlace2);
    }

    public void setTvDay(int day) {
        tvDay.setText("DAY"+day);
    }

    public void setTvDate1(String date1) {
        tvDate1.setText(date1);
    }

    public void setTvDate2(String date2) {
        tvDate2.setText(date2);
    }

    public void setTvPlaceName2(String placeName2) {
        tvPlaceName2.setText(placeName2);
    }

    public void setTvCntPlace2(int cntPlace2) {
        tvCntPlace2.setText(""+cntPlace2);
    }
}
