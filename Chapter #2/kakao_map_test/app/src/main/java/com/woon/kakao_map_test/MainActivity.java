package com.woon.kakao_map_test;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    // asynctask
    Background task;
    JSONArray jsonArray;

    //spinner
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.Map_View);
        mapViewContainer.addView(mapView);

        Spinner sp1 = (Spinner) findViewById(R.id.spinner1);
        Spinner sp2 = (Spinner) findViewById(R.id.spinner2);
        Spinner sp3 = (Spinner) findViewById(R.id.spinner3);

        final String url = "http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";
        task = new Background();

        try {
            jsonArray = task.execute(url).get();
            arrayList = new ArrayList<>();

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrayList.add(jsonObject.getString("value"));
            }

            arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item,arrayList);

            sp1.setAdapter(arrayAdapter);

        }
         catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //스피너 클릭시 이벤트
        sp1.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

    }
}
