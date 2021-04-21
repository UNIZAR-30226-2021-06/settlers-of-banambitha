package es.susangames.catan.service;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.*;


public class HttpService {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static OkHttpClient client = new OkHttpClient();

    JSONObject post(String url, String json) throws IOException {
        JSONObject jsonObject;
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();
        try (Response response = client.newCall(request).execute()) {
           try {
                jsonObject = new JSONObject(response.body().string());
           }catch (JSONException err){
                return null;
           }
           return jsonObject;
        }
    }

    JSONObject get(String url) throws IOException {
        JSONObject jsonObject;
        Request request = new Request.Builder()
            .url(url)
            .build();
        try (Response response = client.newCall(request).execute()) {
           try {
                jsonObject = new JSONObject(response.body().string());
           }catch (JSONException err){
                return null;
           }
           return jsonObject;
        }
    }

    JSONArray getArr(String url) throws IOException {
        JSONArray jsonObject;
        Request request = new Request.Builder()
            .url(url)
            .build();
        try (Response response = client.newCall(request).execute()) {
           try {
                jsonObject = new JSONArray(response.body().string());
           }catch (JSONException err){
                return null;
           }
        }
         return jsonObject;
     }
}