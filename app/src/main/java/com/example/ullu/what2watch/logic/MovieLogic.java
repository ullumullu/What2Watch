package com.example.ullu.what2watch.logic;

import android.net.Uri;
import android.util.Log;

import com.example.ullu.what2watch.BuildConfig;
import com.example.ullu.what2watch.dto.MovieDTO;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ullu on 25.10.2015.
 */
public class MovieLogic {

    private static final String JSON_RESULTS = "results";
    private final String CLASSNAME = MovieLogic.class.getSimpleName();

    /* "Static" URI Parts for movieDb queries */

    private final String APIKEY_MOVIEDATA = BuildConfig.THE_MOVIE_DB_API_KEY;

    private final String HOST_MOVIEDATA = "http://api.themoviedb.org/3";

    private final String PATH_DISCOVER_MOVIES = "discover/movie";

    private final String PARAM_RELEASEDATE_GTE = "primary_release_date.gte";

    private final String PARAM_RELEASEDATE_LTE = "primary_release_date.lte";

    private final String PARAM_APIKEY = "api_key";

    /* "Static" URI Parts for movie imgage queries */
    private final String HOST_MOVIEIMG = "http://image.tmdb.org/t/p/";

    private final String MOVIEIMG_WIDTH = "w500";

    public List<MovieDTO> getCurrentMovies(final int amount) {

        final Date currentDate = new Date();

        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, amount);
        final Date untilDate = cal.getTime();
        // Transform the date to the expected format of the REST API.
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDateFormatted = dateFormat.format(currentDate);
        final String untilDateFormatted =  dateFormat.format(untilDate);

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String currentMoviesJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            Uri.Builder uriBuilder = Uri.parse(HOST_MOVIEDATA).buildUpon();
            uriBuilder.appendEncodedPath(PATH_DISCOVER_MOVIES);
            uriBuilder.appendQueryParameter(PARAM_RELEASEDATE_GTE, currentDateFormatted);
            uriBuilder.appendQueryParameter(PARAM_RELEASEDATE_LTE, untilDateFormatted);
            uriBuilder.appendQueryParameter(PARAM_APIKEY, APIKEY_MOVIEDATA);
            Log.v(CLASSNAME, "Query URL: " + uriBuilder.build().toString());
            URL url = new URL(uriBuilder.build().toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line);
                buffer.append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            currentMoviesJsonStr = buffer.toString();

            Log.v(CLASSNAME, currentMoviesJsonStr);
        } catch (IOException e) {
            Log.e(CLASSNAME, "An error occured during retrieving of current movies. Exception is:  ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(CLASSNAME, "Error closing stream", e);
                }
            }
        }

        return transformJsonToMovieDtos(currentMoviesJsonStr);
    }

    private List<MovieDTO> transformJsonToMovieDtos(final String jsonInputStr) {
        final List<MovieDTO> result = new LinkedList<>();

        try {
            JSONObject jsonInputObj = new JSONObject(jsonInputStr);
            JSONArray resultsObj = jsonInputObj.getJSONArray(JSON_RESULTS);
            for(int indx = 0; indx < resultsObj.length(); indx++) {
                JSONObject movieObj = resultsObj.getJSONObject(indx);
                final String title = movieObj.getString("original_title");
                final String synopsis = movieObj.getString("overview");
                final String posterPath = movieObj.getString("poster_path");
                if(StringUtils.isEmpty(posterPath) || posterPath.equals("null")) {
                    continue;
                }
                // Build directly the image url. Otherwise we would have to do it later dynamically...
                Uri.Builder imageUri = Uri.parse(HOST_MOVIEIMG).buildUpon();
                imageUri.appendEncodedPath(MOVIEIMG_WIDTH);
                imageUri.appendEncodedPath(posterPath.replaceFirst("/", ""));
                final String releaseDate = movieObj.getString("release_date");
                final double popularity = movieObj.getDouble("popularity");
                final double userRating = movieObj.getDouble("vote_average");
                final int votes = movieObj.getInt("vote_count");

                MovieDTO movieDTO = new MovieDTO();
                movieDTO.setOriginalTitle(title);
                movieDTO.setPlotSynopsis(synopsis);
                movieDTO.setImageUrl(imageUri.toString());
                movieDTO.setReleaseDate(releaseDate);
                movieDTO.setPopularity(popularity);
                movieDTO.setUserRating(userRating);
                movieDTO.setVotes(votes);

                result.add(movieDTO);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}



