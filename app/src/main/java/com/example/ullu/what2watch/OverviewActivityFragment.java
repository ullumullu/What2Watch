package com.example.ullu.what2watch;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.example.ullu.what2watch.adapter.MovieAdapter;
import com.example.ullu.what2watch.dto.MovieDTO;
import com.example.ullu.what2watch.logic.MovieLogic;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class OverviewActivityFragment extends Fragment {

    MovieAdapter mMoviesAdapter;

    public OverviewActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        FetchMoviesTask task = new FetchMoviesTask();
        task.execute(7);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMoviesAdapter =
                new MovieAdapter(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_movies, // The name of the layout ID.
                        R.id.list_item_movie_grid_view, // The ID of the textview to populate.
                        new ArrayList<MovieDTO>());

        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView listView = (GridView) rootView.findViewById(R.id.grid_overview_movies);
        listView.setAdapter(mMoviesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieDTO movie = (MovieDTO) adapterView.getItemAtPosition(i);
                Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
                detailActivity.putExtra(Intent.EXTRA_TEXT, movie);
                startActivity(detailActivity);
            }
        });

        return rootView;
    }

    private class FetchMoviesTask extends AsyncTask<Integer, Void, List<MovieDTO>> {

        final MovieLogic movieLogic = new MovieLogic();

        @Override
        protected List<MovieDTO> doInBackground(Integer... days) {
            final int fetchMoviesForTheNextDays;
            if(days.length > 0) {
                fetchMoviesForTheNextDays = days[0];
            } else {
                fetchMoviesForTheNextDays = 7;
            }
            return movieLogic.getCurrentMovies(fetchMoviesForTheNextDays);
        }

        @Override
        protected void onPostExecute(List<MovieDTO> movies) {
            super.onPostExecute(movies);
            mMoviesAdapter.clear();
            mMoviesAdapter.addAll(movies);
        }
    }

}
