package com.tang.fitandchoose.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.format.Time;
import android.text.style.ImageSpan;
import android.util.EventLogTags;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tang.fitandchoose.Calender.bean.DateEntity;
import com.tang.fitandchoose.Calender.view.DataView;
import com.tang.fitandchoose.Data.Data_User_Detail;
import com.tang.fitandchoose.Fit_searchActivity;
import com.tang.fitandchoose.item.Item_user_detail;
import com.tang.fitandchoose.R;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.PercentFormatter;

/**
 * Created by Carson_Ho on 16/5/23.
 */
public class Fragment_fit extends Fragment
{
   // FirebaseDatabase
    DataView dataView;
    ListView ListView_breakfast_kcal,ListView_breakfast_item,ListView_noon_item,ListView_noon_kcal,ListView_night_item,ListView_night_kcal,ListView_others_item,ListView_others_kcal,ListView_sport_item,ListView_sport_kcal;
    TextView TextView_user_bmr,TextView_user_total_kcal,TextView_breakfast_total_kcal,TextView_least,TextView_breakfast_add,TextView_others_add,TextView_noon_add,TextView_night_add,
            TextView_noon_total_kcal,TextView_night_total_kcal,TextView_others_total_kcal,TextView_breakfast_click,TextView_noon_click,TextView_night_click,TextView_others_click,
            TextView_user_total_kcal2,TextView_sport_total_kcal,TextView_sport_add,TextView_sport_click;
    TableRow TableRow_tablerow_beakfast,TableRow_tablerow_noon,TableRow_tablerow_night,TableRow_tablerow_others,TableRow_tablerow_sport;
    PieChart pieChart;
    String[] pieChart_text = new String[] { "攝取的卡路里", "剩餘的卡路里" };
    float[] pieChart_value = new float[2];
    String[] list2_kcal;
    String[] item,kcal,number,user = new String[7],breakfast_item,breakfast_number,noon_item,noon_number,night_item,night_number,others_item,others_number,sport_item,sport_number;
    ArrayAdapter adapter_item,adapter_kcal;
    public Data_User_Detail list1_sql = null;
    List<Item_user_detail> item_user_detail;
    int user_bmr = 1600,item_total_kcal = 0,all_total_kcal = 0;
    Time t;
    int count;
    String now_date,search = "0";
    View view;
    boolean breakfast_list_open = false,noon_list_open = false,night_list_open = false,others_list_open = false,sport_list_open = false;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    SpannableString SpannableDown,SpannableUp,SpannableAdd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fit_main, null);
        user = Objects.requireNonNull(getActivity().getIntent().getExtras()).getStringArray("user");
        now_date = Objects.requireNonNull(getActivity().getIntent().getExtras()).getString("now_date");
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageSpan mImageSpan = new ImageSpan(getActivity(), R.mipmap.icon_down);
        SpannableDown = new SpannableString("   ");
        SpannableDown.setSpan(mImageSpan, 1 , 2 , 0);
        mImageSpan = new ImageSpan(getActivity(), R.mipmap.icon_top);
        SpannableUp = new SpannableString("   ");
        SpannableUp.setSpan(mImageSpan, 1 , 2 , 0);
        mImageSpan = new ImageSpan(getActivity(), R.mipmap.icon_add);
        SpannableAdd = new SpannableString("   ");
        SpannableAdd.setSpan(mImageSpan, 1 , 2 , 0);


        pieChart = (PieChart) view.findViewById(R.id.pieChart);


        dataView = (DataView) view.findViewById(R.id.date_select);

        TextView_user_bmr = (TextView) view.findViewById(R.id.user_bmr);
        TextView_user_total_kcal = (TextView) view.findViewById(R.id.user_total_kcal);
        TextView_user_total_kcal2 = (TextView) view.findViewById(R.id.user_total_kcal2);
        TextView_least = (TextView) view.findViewById(R.id.least);

        ListView_breakfast_item = (ListView)view.findViewById(R.id.breakfast_item);
        ListView_breakfast_kcal = (ListView) view.findViewById(R.id.breakfast_kcal);
        TextView_breakfast_total_kcal = (TextView) view.findViewById(R.id.breakfast_total_kcal);
        TextView_breakfast_add = (TextView) view.findViewById(R.id.breakfast_add);
        TextView_breakfast_add.setText(SpannableAdd);
        TextView_breakfast_add.setTextSize(30);
        TextView_breakfast_click = (TextView) view.findViewById(R.id.breakfast_click);
        TextView_breakfast_click.setText(SpannableDown);
        TableRow_tablerow_beakfast = (TableRow) view.findViewById(R.id.tablerow_beakfast);

        ListView_noon_item = (ListView) view.findViewById(R.id.noon_item);
        ListView_noon_kcal = (ListView) view.findViewById(R.id.noon_kcal);
        TextView_noon_total_kcal = (TextView) view.findViewById(R.id.noon_total_kcal);
        TextView_noon_add = (TextView) view.findViewById(R.id.noon_add);
        TextView_noon_add.setText(SpannableAdd);
        TextView_noon_click = (TextView) view.findViewById(R.id.noon_click);
        TextView_noon_click.setText(SpannableDown);
        TableRow_tablerow_noon = (TableRow) view.findViewById(R.id.tablerow_noon);

        ListView_night_item = (ListView) view.findViewById(R.id.night_item);
        ListView_night_kcal = (ListView) view.findViewById(R.id.night_kcal);
        TextView_night_total_kcal = (TextView) view.findViewById(R.id.night_total_kcal);
        TextView_night_add = (TextView) view.findViewById(R.id.night_add);
        TextView_night_add.setText(SpannableAdd);
        TextView_night_click = (TextView) view.findViewById(R.id.night_click);
        TextView_night_click.setText(SpannableDown);
        TableRow_tablerow_night = (TableRow) view.findViewById(R.id.tablerow_night);

        ListView_others_item = (ListView) view.findViewById(R.id.others_item);
        ListView_others_kcal = (ListView) view.findViewById(R.id.others_kcal);
        TextView_others_total_kcal = (TextView) view.findViewById(R.id.others_total_kcal);
        TextView_others_add = (TextView) view.findViewById(R.id.others_add);
        TextView_others_add.setText(SpannableAdd);
        TextView_others_click = (TextView) view.findViewById(R.id.others_click);
        TextView_others_click.setText(SpannableDown);
        TableRow_tablerow_others = (TableRow) view.findViewById(R.id.tablerow_others);

        ListView_sport_item = (ListView) view.findViewById(R.id.sport_item);
        ListView_sport_kcal = (ListView) view.findViewById(R.id.sport_kcal);
        TextView_sport_total_kcal = (TextView) view.findViewById(R.id.sport_total_kcal);
        TextView_sport_add = (TextView) view.findViewById(R.id.sport_add);
        TextView_sport_add.setText(SpannableAdd);
        TextView_sport_click = (TextView) view.findViewById(R.id.sport_click);
        TextView_sport_click.setText(SpannableDown);
        TableRow_tablerow_sport = (TableRow) view.findViewById(R.id.tablerow_sport);

        dataView.getData(now_date);
        set();
        dataView.setOnSelectListener(new DataView.OnSelectListener() {
            @Override
            public void onSelected(DateEntity date) {
                now_date = date.date;
                set();
            }
        });

    }
    @SuppressLint("SetTextI18n")
    public void set(){
        breakfast_list_open = false;noon_list_open = false;night_list_open = false;others_list_open = false;sport_list_open = false;
        item_total_kcal = 0;all_total_kcal = 0;
        user_bmr = (int)Double.parseDouble(user[6]);
        list1_sql = new Data_User_Detail(getActivity());
        set_item_kcal("早餐",ListView_breakfast_item,ListView_breakfast_kcal,TextView_breakfast_total_kcal,breakfast_item,breakfast_number);
        set_item_kcal("午餐",ListView_noon_item,ListView_noon_kcal,TextView_noon_total_kcal,noon_item,noon_number);
        set_item_kcal("晚餐",ListView_night_item,ListView_night_kcal,TextView_night_total_kcal,night_item,night_number);
        set_item_kcal("點心/其他",ListView_others_item,ListView_others_kcal,TextView_others_total_kcal,others_item,others_number);
        set_item_kcal("熱量消耗",ListView_sport_item,ListView_sport_kcal,TextView_sport_total_kcal,sport_item,sport_number);

        TextView_user_bmr.setText(""+user_bmr);
        TextView_user_total_kcal.setText(""+all_total_kcal);
        TextView_user_total_kcal2.setText(""+all_total_kcal);
        TextView_least.setText(""+((user_bmr - all_total_kcal < 0) ? 0 : (user_bmr - all_total_kcal)));
        pieChart_setData(pieChart_text.length);

        TextView_breakfast_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(breakfast_list_open){
                    TableRow_tablerow_beakfast.setVisibility(View.VISIBLE);
                    TextView_breakfast_click.setText(SpannableUp);
                    breakfast_list_open = !breakfast_list_open;
                }
                else{
                    TableRow_tablerow_beakfast.setVisibility(View.GONE);
                    TextView_breakfast_click.setText(SpannableDown);
                    breakfast_list_open = !breakfast_list_open;
                }
            }
        });
        TextView_noon_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noon_list_open){
                    TableRow_tablerow_noon.setVisibility(View.VISIBLE);
                    TextView_noon_click.setText(SpannableUp);
                    noon_list_open = !noon_list_open;
                }
                else{
                    TableRow_tablerow_noon.setVisibility(View.GONE);
                    TextView_noon_click.setText(SpannableDown);
                    noon_list_open = !noon_list_open;
                }
            }
        });
        TextView_night_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(night_list_open){
                    TableRow_tablerow_night.setVisibility(View.VISIBLE);
                    TextView_night_click.setText(SpannableUp);
                    night_list_open = !night_list_open;
                }
                else{
                    TableRow_tablerow_night.setVisibility(View.GONE);
                    TextView_night_click.setText(SpannableDown);
                    night_list_open = !night_list_open;
                }
            }
        });
        TextView_others_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(others_list_open){
                    TableRow_tablerow_others.setVisibility(View.VISIBLE);
                    TextView_others_click.setText(SpannableUp);
                    others_list_open = !others_list_open;
                }
                else{
                    TableRow_tablerow_others.setVisibility(View.GONE);
                    TextView_others_click.setText(SpannableDown);
                    others_list_open = !others_list_open;
                }
            }
        });
        TextView_sport_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sport_list_open){
                    TableRow_tablerow_sport.setVisibility(View.VISIBLE);
                    TextView_sport_click.setText(SpannableUp);
                    sport_list_open = !sport_list_open;
                }
                else{
                    TableRow_tablerow_sport.setVisibility(View.GONE);
                    TextView_sport_click.setText(SpannableDown);
                    sport_list_open = !sport_list_open;
                }
            }
        });
        TextView_breakfast_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                intent.setClass(getActivity().getApplication(), Fit_searchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, "0");
                bundle.putStringArray("user",user);
                bundle.putString("cate","早餐");
                bundle.putString("now_date",now_date);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        TextView_noon_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                intent.setClass(getActivity().getApplication(), Fit_searchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, "0");
                bundle.putStringArray("user",user);
                bundle.putString("cate","午餐");
                bundle.putString("now_date",now_date);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        TextView_night_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                intent.setClass(getActivity().getApplication(), Fit_searchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, "0");
                bundle.putStringArray("user",user);
                bundle.putString("cate","晚餐");
                bundle.putString("now_date",now_date);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        TextView_others_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                intent.setClass(getActivity().getApplication(), Fit_searchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, "0");
                bundle.putStringArray("user",user);
                bundle.putString("cate","點心/其他");
                bundle.putString("now_date",now_date);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        TextView_sport_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                intent.setClass(getActivity().getApplication(), Fit_searchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, "0");
                bundle.putStringArray("user",user);
                bundle.putString("cate","熱量消耗");
                bundle.putString("now_date",now_date);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    public void set_item_kcal(final String category, ListView listview_item, ListView listview_kcal, TextView Textview_total_kcal, String[] cate_item, String[] cate_number){
        count = list1_sql.search_getCount(now_date,user[0],category);
        item = new String[count];kcal = new String[count];cate_item = new String[count];cate_number = new String[count];
        item_user_detail=list1_sql.search_getAll(now_date,user[0],category);
        count = 0;
        item_total_kcal = 0;
        for(Item_user_detail i:item_user_detail){
            item[count] = ""+i.getItem()+" X "+ i.getNumber();
            kcal[count] = ""+i.getKcal()+" X "+ i.getNumber() + " = "+ (i.getKcal()*i.getNumber());
            cate_item[count] = i.getItem();
            cate_number[count] = ""+i.getNumber();
            item_total_kcal += i.getKcal()*i.getNumber();
            count++;
        }
        adapter_item = new ArrayAdapter<String> (getActivity(), R.layout.list_item_1, item);
        listview_item.setAdapter(adapter_item);
        adapter_kcal = new ArrayAdapter <String> (getActivity(), R.layout.list_item_2, kcal);
        listview_kcal.setAdapter(adapter_kcal);
        final String[] finalCate_item = cate_item;
        final String[] finalCate_number = cate_number;
        listview_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, final int arg2, long arg3) {
                    new AlertDialog.Builder(getActivity()).setTitle("確定刪除 一 "+ finalCate_item[arg2]+" X "+ finalCate_number[arg2]+"？")
                            .setPositiveButton("取消",null).setNegativeButton("確定",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list1_sql.delete(now_date,finalCate_item[arg2],finalCate_number[arg2]);
                            db.collection("user").document(user[0]).collection("data").document(now_date+""+category+""+finalCate_item[arg2]+""+ finalCate_number[arg2]).delete();
                            set();
                        }}).create().show();

            }
        });

        ListAdapter listAdapter = listview_item.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listview_item);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listview_item.getLayoutParams();
        params.height = totalHeight + (listview_item.getDividerHeight() * (listAdapter.getCount()-1));
        ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
        listview_item.setLayoutParams(params);
        params = listview_kcal.getLayoutParams();
        params.height = totalHeight + (listview_kcal.getDividerHeight() * (listAdapter.getCount()-1));
        ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
        listview_kcal.setLayoutParams(params);
        if(!category.equals("熱量消耗"))
            all_total_kcal += item_total_kcal;
        Textview_total_kcal.setText(""+item_total_kcal);
    }


    public void pieChart_setData(int count) {

        // 准备x"轴"数据：在i的位置，显示x[i]字符串
        ArrayList<String> xVals = new ArrayList<String>();

        // 真实的饼状图百分比分区。
        // Entry包含两个重要数据内容：position和该position的数值。
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        pieChart_value[0] = all_total_kcal;
        pieChart_value[1] = Float.parseFloat(""+ TextView_least.getText());

        for (int xi = 0; xi < count; xi++) {
            xVals.add(xi, pieChart_text[xi]);

            // y[i]代表在x轴的i位置真实的百分比占
            yVals.add(new Entry(pieChart_value[xi], xi));
        }

        PieDataSet yDataSet = new PieDataSet(yVals, "");

        // 每个百分比占区块绘制的不同颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#228B22"));
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        yDataSet.setColors(colors);

        // 将x轴和y轴设置给PieData作为数据源
        PieData data = new PieData(xVals, yDataSet);

        // 设置成PercentFormatter将追加%号
        data.setValueFormatter(new PercentFormatter());

        // 文字的颜色
        data.setValueTextColor(Color.BLACK);
        data.setDrawValues(false);     //y none

        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setDescription("");
        // 最终将全部完整的数据喂给PieChart
        pieChart.setDrawSliceText(false);  //x none
        pieChart.setData(data);
        pieChart.invalidate();

    }
}