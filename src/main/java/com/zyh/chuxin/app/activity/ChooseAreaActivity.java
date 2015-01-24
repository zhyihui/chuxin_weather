package com.zyh.chuxin.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.zyh.chuxin.app.ApplicationContext;
import com.zyh.chuxin.app.R;
import com.zyh.chuxin.app.db.ChuXinWeatherDB;
import com.zyh.chuxin.app.model.City;
import com.zyh.chuxin.app.model.Country;
import com.zyh.chuxin.app.model.Province;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地区选择界面
 * Created by zhyh on 2015/1/24.
 */
public class ChooseAreaActivity extends Activity {

    private int type = 0;

    private ListView chooseAreaListView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

    private ApplicationContext applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);

        chooseAreaListView = (ListView) findViewById(R.id.choose_area_list);

        applicationContext = (ApplicationContext) getApplicationContext();
        List<Province> provinceList = applicationContext.getWeatherDB().loadProvinces();
        for (Province province : provinceList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", province.getName());
            map.put("code", province.getCode());
            dataList.add(map);
        }
        simpleAdapter = new SimpleAdapter(this, dataList, R.layout.city_list_item, new
                String[]{"name"}, new int[]{R.id.city_item_name});
        chooseAreaListView.setAdapter(simpleAdapter);
        setListViewItemClickListener();
    }

    private void setListViewItemClickListener() {
        chooseAreaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> tm = dataList.get(position);
                String code = tm.get("code");
                ChuXinWeatherDB weatherDB = applicationContext.getWeatherDB();
                if (type == 0) {
                    type = 1;
                    dataList.clear();
                    List<City> cityList = weatherDB.loadCities(code);
                    for (City city : cityList) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", city.getName());
                        map.put("code", city.getCode());
                        dataList.add(map);
                    }
                    simpleAdapter.notifyDataSetChanged();
                } else if (type == 1) {
                    type = 2;
                    dataList.clear();
                    List<Country> countryList = weatherDB.loadCountries(code);
                    for (Country country : countryList) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", country.getName());
                        map.put("code", country.getCode());
                        dataList.add(map);
                    }
                    simpleAdapter.notifyDataSetChanged();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("code", code);
                    setResult(RESULT_OK, intent);
                    ChooseAreaActivity.this.finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
