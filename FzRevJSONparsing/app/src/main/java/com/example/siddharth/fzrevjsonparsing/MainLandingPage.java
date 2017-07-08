package com.example.siddharth.fzrevjsonparsing;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;

public class MainLandingPage extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

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
        setContentView(R.layout.activity_main_landing_page);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        recyclerView = (RecyclerView) findViewById(R.id.AllPostsRecylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        headingTextView = (TextView) findViewById(R.id.HeadingTextView);
        excerpt = (TextView) findViewById(R.id.TextExcerpt);
        featuredImage = (ImageView) findViewById(R.id.FeaturedImage);
        postsModelClasses = new ArrayList<>();
        logo = (ImageView)findViewById(R.id.fzRevLogo);
        progressDialog = (ProgressBar)findViewById(R.id.progressBar);


        if (savedInstanceState == null){
            new JsonTask().execute("http://www.fzrev.com/wp-json/wp/v2/posts?fields=id,title,content,better_featured_image,link");
        }
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

                    final String postsURL = finalJSONObject.optString("link");

                    PostsModelClass postsModelClass = new PostsModelClass();
                    postsModelClass.setTitle(titleRendered);
                    postsModelClass.setContent(contentRendered);
                    postsModelClass.setSource_url(featuredImageURL);
                    postsModelClass.setPost_link(postsURL);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //if (mDrawerToggle.onOptionsItemSelected(item)) {
         //   return true;
        //}

        if (item.getItemId() == R.id.menu_refresh) {
            //Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            new JsonTask().execute("http://www.fzrev.com/wp-json/wp/v2/posts?fields=id,title,content,better_featured_image,link");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_landing_page, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainLandingPage) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
