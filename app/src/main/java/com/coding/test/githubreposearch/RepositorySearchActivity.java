package com.coding.test.githubreposearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RepositorySearchActivity extends AppCompatActivity {

    private ArrayList<Repository> repoList = new ArrayList<>();
    private String query;
    private RepositoryAdapter repositoryAdapter;
    private ImageView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_search);

        final EditText queryText = findViewById(R.id.query_text);
        loader = findViewById(R.id.loader);
        Glide.with(this).load(R.drawable.loader_new).into(loader);

        RecyclerView repositoryView = findViewById(R.id.repo_list);
        repositoryAdapter = new RepositoryAdapter(RepositorySearchActivity.this, repoList);
        repositoryView.setLayoutManager(new LinearLayoutManager(RepositorySearchActivity.this));
        repositoryView.setAdapter(repositoryAdapter);

        queryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        queryText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (queryText.getRight() - queryText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (queryText.getText().toString().trim().equals("")) {
                            queryText.setError("Enter a valid name");
                        } else {
                            repoList.clear();
                            getRepositoryResults(query);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void getRepositoryResults(String query) {
        loader.setVisibility(View.VISIBLE);

        String url = "https://api.github.com/search/repositories?q=" + query + "&sort=stars&order=desc";
        StringRequest getQuery = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    JSONArray itemsArray = jsonResponse.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        Repository repository = new Repository();
                        JSONObject jsonObject = itemsArray.getJSONObject(i);

                        String repoName = jsonObject.getString("name");
                        String description = jsonObject.getString("description");
                        int stars = jsonObject.getInt("watchers_count");
                        JSONObject owner = jsonObject.getJSONObject("owner");
                        String avatar = owner.getString("avatar_url");

                        repository.setAvatar(avatar);
                        repository.setRepoName(repoName);
                        repository.setDescription(description);
                        repository.setStars(stars);

                        repoList.add(repository);
                    }
                    loader.setVisibility(View.GONE);
                    repositoryAdapter.setRepoList(repoList);
                    repositoryAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 15000;//15 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getQuery.setRetryPolicy(policy);
        VolleySingleton.getInstance(RepositorySearchActivity.this).addToRequestQueue(getQuery);
    }
}
