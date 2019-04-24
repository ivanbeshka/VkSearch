package com.example.vksearch.utils;

import android.net.Uri;
import android.provider.Settings;

import com.example.vksearch.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkUtils {

    public static final String VK_URL_BASE = "https://api.vk.com";
    public static final String VK_METHOD_USERS_GET = "/method/users.get";
    public static final String PARAM_VK_ID = "user_ids";
    public static final String PARAM_VK_VERSION = "v";
    public static final String PARAM_KEY = "access_token";

    public static URL generateURL(String userID) {
        Uri builtUri = Uri.parse(VK_URL_BASE + VK_METHOD_USERS_GET)
                .buildUpon()
                .appendQueryParameter(PARAM_VK_ID, userID)
                .appendQueryParameter(PARAM_VK_VERSION, "5.8")
                .appendQueryParameter(PARAM_KEY,
                        "65ac19a065ac19a065ac19a0c865c5a4ad665ac65ac19a0390b49114d54be7299a5a6fe")
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }

    public static String getResponseFromURL(URL url) throws IOException {
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
        } catch (UnknownHostException e) {
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
