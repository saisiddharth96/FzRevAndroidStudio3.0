package com.example.siddharth.fzrevjsonparsing;

import android.app.Activity;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllPosts extends Activity {

    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;

    public static final String URL_DATA = "http://www.fzrev.com/wp-json/wp/v2/posts?fields=id,title,content,better_featured_image,link";

    private List<PostsModelClass> postsModelClasses;
    private ImageView featuredImage;
    private TextView headingTextView, excerpt;
    private ImageView logo;
    private ProgressBar progressDialog;

    public SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);


        recyclerView = (RecyclerView) findViewById(R.id.AllPostsRecylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        headingTextView = (TextView) findViewById(R.id.HeadingTextView);
        excerpt = (TextView) findViewById(R.id.TextExcerpt);
        featuredImage = (ImageView) findViewById(R.id.FeaturedImage);
        postsModelClasses = new ArrayList<>();
        logo = (ImageView)findViewById(R.id.fzRevLogo);
        progressDialog = (ProgressBar)findViewById(R.id.progressBar);


        new JsonTask().execute("http://www.fzrev.com/wp-json/wp/v2/posts?fields=id,title,content,better_featured_image,link");
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new JsonTask().execute("http://www.fzrev.com/wp-json/wp/v2/posts?fields=id,title,content,better_featured_image,link");
            }
        });

        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.side_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_refresh:
                swipeRefreshLayout.setRefreshing(true);
                new JsonTask().execute("http://www.fzrev.com/wp-json/wp/v2/posts?fields=id,title,content,better_featured_image,link");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class JsonTask extends AsyncTask<String, String, List<PostsModelClass>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           progressDialog.setVisibility(View.VISIBLE);
        }


        @Override
        protected List<PostsModelClass> doInBackground(String... params) {
            HttpURLConnection connection = null;

            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);

                }


                String finalJSON = buffer.toString();
                JSONArray parentArray = new JSONArray(finalJSON);

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalJSONObject = parentArray.getJSONObject(i);
                    JSONObject parentTitle = finalJSONObject.getJSONObject("title");
                    final String titleRendered = parentTitle.getString("rendered");

                    JSONObject parentContent = finalJSONObject.getJSONObject("content");
                    final String contentRendered = parentContent.getString("rendered");

                    JSONObject featuredImage = finalJSONObject.getJSONObject("better_featured_image");
                    final String featuredImageURL = featuredImage.getString("source_url");

                    PostsModelClass postsModelClass = new PostsModelClass();
                    postsModelClass.setTitle(titleRendered);
                    postsModelClass.setContent(contentRendered);
                    postsModelClass.setSource_url(featuredImageURL);
                    postsModelClasses.add(postsModelClass);
                }

                return postsModelClasses;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(List<PostsModelClass> s) {
            super.onPostExecute(s);
            logo.setVisibility(View.GONE);
            progressDialog.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            adapter = new AllPostsRecyclerViewAdapter(postsModelClasses, getApplicationContext());
            recyclerView.setAdapter(adapter);
        }
    }

}






