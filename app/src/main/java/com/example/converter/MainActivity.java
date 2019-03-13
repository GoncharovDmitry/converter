package com.example.converter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final String APIKEY = "&apiKey=41e09a373344db4152c6";
    Spinner spinnerFrom;
    Spinner spinnerTo;
    EditText number;
    EditText result;
    TextView textView;
    String url;
    float valueToConvert;
    RequestQueue queue;
    Cache cache;
    Network network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        number = findViewById(R.id.number);
        result = findViewById(R.id.result);
        spinnerFrom.setSelection(0);
        spinnerTo.setSelection(1);

        cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);


    }

    public void onClick(View view) {
        if (number.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Введите значение для конвертации",
                    Toast.LENGTH_LONG).show();
            return;
        }
        url = "https://free.currencyconverterapi.com/api/v6/convert?q="
                + spinnerFrom.getSelectedItem().toString() + "_"
                + spinnerTo.getSelectedItem().toString() + "&compact=ultra"
                + APIKEY;

//        Log.d("tt" ,"ss  " +url);

        valueToConvert = Float.parseFloat(number.getText().toString());

        queue.start();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        textView.setText("Response: " + response.toString());
                        try {
                            result.setText(String.valueOf(valueToConvert * response.getDouble(spinnerFrom.getSelectedItem().toString() + "_"
                                    + spinnerTo.getSelectedItem().toString())));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("Error " + error);

                    }
                });
        queue.add(jsonObjectRequest);


    }
}