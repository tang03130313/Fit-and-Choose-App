package com.tang.fitandchoose;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tang.fitandchoose.Data.Data_Item;
import com.tang.fitandchoose.Data.Data_Meals;
import com.tang.fitandchoose.Data.Data_Num;
import com.tang.fitandchoose.Data.Data_Sport;
import com.tang.fitandchoose.Data.Data_User_Detail;
import com.tang.fitandchoose.item.Item_item;
import com.tang.fitandchoose.item.Item_meals;
import com.tang.fitandchoose.item.Item_num;
import com.tang.fitandchoose.item.Item_sport;
import com.tang.fitandchoose.item.Item_user_detail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fit_searchActivity extends Activity  implements SearchView.OnQueryTextListener {
    private static String GMS_SEARCH_ACTION = "com.google.android.gms.actions.SEARCH_ACTION";

    TextView TextView_total_kcal;

    SearchView mSearchView;
    LinearLayout Fit_add_finish;

    String mQuery = "",cate,now_date;
    Spinner Spinner_type,Spinner_num,spinner_fit_add_3; //喧告Spinner物件
    ListView Listview_search_list;
    String[] list_catalog = {"早餐","午餐","晚餐","點心/其他"}; //喧告字串陣列
    String[] list_cate_sport = {"熱量消耗"};
    String[] list,item; //喧告字串陣列
    String[] kcal;
    String[] list_num ; //喧告字串陣列
    ArrayAdapter<String> listAdapter;
    ArrayAdapter adapter;
    public Data_Item list1_sql = null;
    public Data_User_Detail sql_data_user_detail = null;
    public Data_Num sql_num = null;
    public Data_Sport list1_sql_sport = null;
    List<Item_item> item_item;
    List<Item_num> item_num;
    List<Item_sport> item_sport;
    String[] user = new String[7];
    int list_i = 0,count,listview_num = 0,spinner_num_num = 0;
    Bundle bundle_pre;
    boolean is_search = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_search);
        bundle_pre = this.getIntent().getExtras();
        if(bundle_pre != null) {
            user = bundle_pre.getStringArray("user");
            cate = bundle_pre.getString("cate");
            now_date = bundle_pre.getString("now_date");
        }

        Spinner_type = (Spinner) findViewById(R.id.spinner_type);
        Spinner_num = (Spinner) findViewById(R.id.spinner_num);
        Listview_search_list = (ListView) findViewById(R.id.listview_search_list);
        TextView_total_kcal = (TextView) findViewById(R.id.total_kcal);
        TextView_total_kcal.setText("0");
        Fit_add_finish = (LinearLayout) findViewById(R.id.finish);
        final SearchView searchView = (SearchView)findViewById(R.id.search_view);

        if(cate.equals("熱量消耗")) {
            searchView.setQueryHint("請輸入運動項目");
            set_sport();
        }
        else {
            searchView.setQueryHint("請輸入餐點");
            set_fit();
        }
        onNewIntent(getIntent());


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final Bundle bundle = new Bundle();
                Intent intent = new Intent(searchView.getContext(), Fit_searchActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, query);
                bundle.putStringArray("user",user);
                bundle.putString("cate",cate);
                bundle.putString("now_date",now_date);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }
    public void set_fit(){
        listAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list_catalog);
        listAdapter.setDropDownViewResource(R.layout.drop_down_item);
        Spinner_type.setAdapter(listAdapter);

        sql_num = new Data_Num(Fit_searchActivity.this);
        count = sql_num.getCount();
        if (count == 0) {
            sql_num.sample();
        }
        list_num = new String[count];
        item_num=sql_num.getAll();
        list_i = 0;
        for(Item_num i:item_num){
            list_num[list_i] = ""+i.getNumber();
            list_i++;
        }
        listAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list_num);
        listAdapter.setDropDownViewResource(R.layout.drop_down_item);
        Spinner_num.setAdapter(listAdapter);

        for(int i = 0;i < list_catalog.length;i++){
            if(list_catalog[i].equals(cate)){
                Spinner_type.setSelection(i);
                break;
            }
        }

        Spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                int i = Spinner_type.getSelectedItemPosition();
                if(!cate.equals("熱量消耗"))
                    cate = list_catalog[i];
            }
            @Override
            public void onNothingSelected(AdapterView arg0) {}
        });
        Fit_add_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextView_total_kcal.getText().equals("0")) {
                    sql_data_user_detail = new Data_User_Detail(Fit_searchActivity.this);
                    Item_user_detail temp = new Item_user_detail(0,user[0],now_date,cate,item[listview_num],Integer.parseInt(kcal[listview_num]),Integer.parseInt(list_num[spinner_num_num]));
                    sql_data_user_detail.insert(temp);
                    Map<String, Object> user_insert = new HashMap<>();
                    user_insert.put("date", now_date);
                    user_insert.put("category", cate);
                    user_insert.put("item", item[listview_num]);
                    user_insert.put("kcal",Integer.parseInt(kcal[listview_num]));
                    user_insert.put("number", Integer.parseInt(list_num[spinner_num_num]));
                    db.collection("user").document(user[0]).collection("data").document(now_date+cate+item[listview_num]+list_num[spinner_num_num])
                            .set(user_insert)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {
                                   //Toast.makeText(Fit_searchActivity.this, "成功.",
                                            //Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(Fit_searchActivity.this, "失敗.",
                                            //Toast.LENGTH_SHORT).show();
                                }
                            });

                    Intent intent = new Intent();
                    final Bundle bundle = new Bundle();
                    intent.setClass(Fit_searchActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    bundle.putStringArray("user", user);
                    bundle.putString("now_date", now_date);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    public void set_sport(){
        listAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list_cate_sport);
        listAdapter.setDropDownViewResource(R.layout.drop_down_item);
        Spinner_type.setAdapter(listAdapter);

        sql_num = new Data_Num(Fit_searchActivity.this);
        count = sql_num.getCount();
        if (count == 0) {
            sql_num.sample();
        }
        list_num = new String[count];
        item_num=sql_num.getAll();
        list_i = 0;
        for(Item_num i:item_num){
            list_num[list_i] = ""+i.getNumber();
            list_i++;
        }
        listAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list_num);
        listAdapter.setDropDownViewResource(R.layout.drop_down_item);
        Spinner_num.setAdapter(listAdapter);

        for(int i = 0;i < list_catalog.length;i++){
            if(list_catalog[i].equals(cate)){
                Spinner_type.setSelection(i);
                break;
            }
        }

        Spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                int i = Spinner_type.getSelectedItemPosition();
                if(!cate.equals("熱量消耗"))
                    cate = list_catalog[i];
            }
            @Override
            public void onNothingSelected(AdapterView arg0) {}
        });
        Fit_add_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextView_total_kcal.getText().equals("0")) {
                    sql_data_user_detail = new Data_User_Detail(Fit_searchActivity.this);
                    Item_user_detail temp = new Item_user_detail(0,user[0],now_date,cate,item[listview_num],Integer.parseInt(kcal[listview_num]),Integer.parseInt(list_num[spinner_num_num]));
                    sql_data_user_detail.insert(temp);
                    Map<String, Object> user_insert = new HashMap<>();
                    user_insert.put("date", now_date);
                    user_insert.put("category", cate);
                    user_insert.put("item", item[listview_num]);
                    user_insert.put("kcal",Integer.parseInt(kcal[listview_num]));
                    user_insert.put("number", Integer.parseInt(list_num[spinner_num_num]));
                    db.collection("user").document(user[0]).collection("data").document(now_date+cate+item[listview_num]+list_num[spinner_num_num])
                            .set(user_insert)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                    Intent intent = new Intent();
                    final Bundle bundle = new Bundle();
                    intent.setClass(Fit_searchActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    bundle.putStringArray("user", user);
                    bundle.putString("now_date", now_date);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        if ((action.equals(Intent.ACTION_SEARCH) ||
                action.equals(GMS_SEARCH_ACTION)) && !is_search) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            if(!mQuery.equals("0")) {
                if(cate.equals("熱量消耗"))
                    doSearch_sport(mQuery);
                else
                    doSearch_fit(mQuery);
            }
            else
                doNothing(mQuery);
        }
    }

    private void doSearch_fit(String search) {
        list1_sql = new Data_Item(Fit_searchActivity.this);
        count = list1_sql.getCount();
        if (count == 0) {
            list1_sql.sample();
        }
        count = list1_sql.search_getCount(search);
        if(count != 0) {
            item = new String[count];
            kcal = new String[count];
            list = new String[count];
            item_item = list1_sql.search_getAll(search);
            count = 0;
            //item_total_kcal = 0;
            for (Item_item i : item_item) {
                list[count] = "" + i.getName() + " - " + i.getKcal() + " 大卡";
                kcal[count] = "" + i.getKcal();
                item[count] = "" + i.getName();
                count++;
            }
            Listview_search_list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        }
        else{
            item = new String[1];
            item[0] = "查無資料.....";
        }
        if(count != 0) {
            adapter = new ArrayAdapter<String> (Fit_searchActivity.this, android.R.layout.simple_list_item_checked, list);
            Listview_search_list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        }
        else {
            adapter = new ArrayAdapter<String>(Fit_searchActivity.this, R.layout.list_item_1, item);
            Listview_search_list.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
        }
        Listview_search_list.setAdapter(adapter);
        //is_search = true;
       Listview_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                if(count != 0) {
                    listview_num = arg2;
                    spinner_num_num = Spinner_num.getSelectedItemPosition();
                    TextView_total_kcal.setText("" + (Integer.parseInt(kcal[arg2]) * (Integer.parseInt(list_num[spinner_num_num]))));
                }
                else
                    TextView_total_kcal.setText("0");
            }
        });
        Spinner_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if(count != 0 || listview_num != 0){
                    spinner_num_num = Spinner_num.getSelectedItemPosition();
                    TextView_total_kcal.setText("" + (Integer.parseInt(kcal[listview_num]) * (Integer.parseInt(list_num[spinner_num_num]))));
                }
                else
                    TextView_total_kcal.setText("0");
            }
            @Override
            public void onNothingSelected(AdapterView arg0) {
            }
        });
    }

    private void doSearch_sport(String search) {
        list1_sql_sport = new Data_Sport(Fit_searchActivity.this);
        count = list1_sql_sport.getCount();
        if (count == 0) {
            list1_sql_sport.sample();
        }
        count = list1_sql_sport.search_getCount(search);
        if(count != 0) {
            item = new String[count];
            kcal = new String[count];
            list = new String[count];
            item_sport = list1_sql_sport.search_getAll(search);
            count = 0;
            //item_total_kcal = 0;
            for (Item_sport i : item_sport) {
                list[count] = "" + i.getName() + " - " + i.getKcal() + " 大卡";
                kcal[count] = "" + i.getKcal();
                item[count] = "" + i.getName();
                count++;
            }
            Listview_search_list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        }
        else{
            item = new String[1];
            item[0] = "查無資料.....";
        }
        if(count != 0) {
            adapter = new ArrayAdapter<String> (Fit_searchActivity.this, android.R.layout.simple_list_item_checked, list);
            Listview_search_list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        }
        else {
            adapter = new ArrayAdapter<String>(Fit_searchActivity.this, R.layout.list_item_1, list);
            Listview_search_list.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
        }
        Listview_search_list.setAdapter(adapter);
        //is_search = true;
        Listview_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                if(count != 0) {
                    listview_num = arg2;
                    spinner_num_num = Spinner_num.getSelectedItemPosition();
                    TextView_total_kcal.setText("" + (Integer.parseInt(kcal[arg2]) * (Integer.parseInt(list_num[spinner_num_num]))));
                }
                else
                    TextView_total_kcal.setText("0");
            }
        });
        Spinner_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                if(count != 0 || listview_num != 0){
                    spinner_num_num = Spinner_num.getSelectedItemPosition();
                    TextView_total_kcal.setText("" + (Integer.parseInt(kcal[listview_num]) * (Integer.parseInt(list_num[spinner_num_num]))));
                }
                else
                    TextView_total_kcal.setText("0");
            }
            @Override
            public void onNothingSelected(AdapterView arg0) {
            }
        });
    }

    public void doNothing(String query) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchview_in_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);

        if (mQuery != null) {
            mSearchView.setQuery(mQuery, false);
        }

        return true;
    }

    private void setupSearchView(MenuItem searchItem) {

        mSearchView.setIconifiedByDefault(false);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            mSearchView.setSearchableInfo(info);
        }

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setFocusable(false);
        mSearchView.setFocusableInTouchMode(false);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
