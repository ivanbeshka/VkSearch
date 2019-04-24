package com.example.vksearch;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.vksearch.utils.NetworkUtils.generateURL;
import static com.example.vksearch.utils.NetworkUtils.getResponseFromURL;


public class MainActivity extends AppCompatActivity {

    private Button buttonSearch;
    private EditText editTextHint;
    private TextView textViewResult;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;

    private void showResultTV() {
        textViewResult.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        textViewResult.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    class VKQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;

            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            String firstName = null;
            String lastName = null;

            if (response != null && !response.equals("")) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");
                    JSONObject userInfo = jsonArray.getJSONObject(0);

                    firstName = userInfo.getString("first_name");
                    lastName = userInfo.getString("last_name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String result = "Name: " + firstName + "\n " + "LastName: " + lastName;
                textViewResult.setText(result);

                showResultTV();

            } else {
                showError();
            }

            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSearch = findViewById(R.id.b_search);
        editTextHint = findViewById(R.id.ed_search_field);
        textViewResult = findViewById(R.id.tv_result);
        errorMessage = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL generatedURL = generateURL(editTextHint.getText().toString());

                new VKQueryTask().execute(generatedURL);
            }
        };

        buttonSearch.setOnClickListener(onClickListener);
    }
}
