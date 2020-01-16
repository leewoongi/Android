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
    Background1 Gu_task;
    Background2 Gun_task;

    JSONArray jsonArray;

    JSONObject city;
    JSONObject gu;
    JSONObject n_x,n_y;

    //spinner
    ArrayList<String> arrayList, arrayList2, arrayList3;
    ArrayAdapter<String> arrayAdapter, arrayAdapter2, arrayAdapter3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.Map_View);
        mapViewContainer.addView(mapView);

        final Spinner sp1 = (Spinner) findViewById(R.id.spinner1);
        final Spinner sp2 = (Spinner) findViewById(R.id.spinner2);
        final Spinner sp3 = (Spinner) findViewById(R.id.spinner3);

        final String url = "http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";


        // 시의 정보를 담고 있는 파일로부터 구의 정보 json 만들기위해 jsonobject 생성
        city = new JSONObject();
        gu = new JSONObject();

        task = new Background();

        try {
            jsonArray = task.execute(url).get();
            arrayList = new ArrayList<>();

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arrayList.add(jsonObject.getString("value"));

                city.put(jsonObject.getString("value"),jsonObject.getString("code"));
                String data = city.toString();
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


        //도시 스피너  클릭시 이벤트
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            JSONArray Gu_jsonArray;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getItemAtPosition(position).toString();
                System.out.println("#####"+s);
                Gu_task = new Background1();
                try {
                    Gu_jsonArray = Gu_task.execute("http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl." + city.getString(s) + ".json.txt").get();
                    arrayList2 = new ArrayList<>();

                    for (int i = 0; i < Gu_jsonArray.length(); i++) {
                        JSONObject jsonObject = Gu_jsonArray.getJSONObject(i);
                        arrayList2.add(jsonObject.getString("value"));

                        gu.put(jsonObject.getString("value"),jsonObject.getString("code"));
                    }

                    arrayAdapter2 = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, arrayList2);

                    sp2.setAdapter(arrayAdapter2);


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // 구 스피너 클릭했을떄
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            JSONArray Gun_jsonArray;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String s = parent.getItemAtPosition(position).toString();

                Gun_task = new Background2();
                // 위치의 좌표를 얻기위해서
                n_x = new JSONObject();
                n_y = new JSONObject();

                try {
                    Gun_jsonArray = Gun_task.execute("http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf."+gu.getString(s)+".json.txt").get();
                    arrayList3  = new ArrayList<>();

                    for(int i = 0; i<Gun_jsonArray.length();i++){
                        JSONObject jsonObject = Gun_jsonArray.getJSONObject(i);
                        arrayList3.add(jsonObject.getString("value"));

                        n_x.put(jsonObject.getString("value"),jsonObject.getString("x"));
                        n_y.put(jsonObject.getString("value"),jsonObject.getString("y"));
                    }

                    arrayAdapter3 = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList3);
                    sp3.setAdapter(arrayAdapter3);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
