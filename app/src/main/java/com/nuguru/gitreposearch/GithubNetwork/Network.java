package com.nuguru.gitreposearch.GithubNetwork;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Network {

    final static String GITHUB_URL = "https://api.github.com/search/repositories";
    final static String PARAM_Query = "q=";
    final static String PARAM_Sort = "sort";
    final static String PARAM_Order = "order";
    final static String PARAM_Pages = "page";

    final static String creation = "created";
    final static String sortBy = "stars";
    final static String orderBy = "desc";



    public static String CalculateDate() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-30);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        String daythirty = input.format(cal.getTime());

        return daythirty;
    }

    public static URL buildUrl(String p) {

        String query = PARAM_Query + creation + ":>" + CalculateDate() ;
        Uri builtUri = Uri.parse(GITHUB_URL).buildUpon().encodedQuery(query)
                .appendQueryParameter(PARAM_Sort, sortBy)
                .appendQueryParameter(PARAM_Order,orderBy)
                .appendQueryParameter(PARAM_Pages,p)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
