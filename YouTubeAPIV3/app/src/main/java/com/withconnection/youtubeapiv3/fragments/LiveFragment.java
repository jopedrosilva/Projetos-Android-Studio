package com.withconnection.youtubeapiv3.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.withconnection.youtubeapiv3.DetailsActivity;
import com.withconnection.youtubeapiv3.R;
import com.withconnection.youtubeapiv3.adapters.VideoPostAdapter;
import com.withconnection.youtubeapiv3.adapters.VideoPostAdapter_1;
import com.withconnection.youtubeapiv3.interfaces.OnItemClickListener;
import com.withconnection.youtubeapiv3.models.YoutubeDataModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class LiveFragment extends Fragment {
    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyAs59xDCNKeuIXsVMDTh4LksZ4zc2lAF9A";//here you should use your api key for testing purpose you can use this api also
    private static String CHANNEL_ID = "UCoMdktPbSTixAyNGwb-UYkQ"; //here you should use your channel id for testing purpose you can use this api also
    private static String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=" + CHANNEL_ID + "&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY + "";


    private RecyclerView mList_videos = null;
    private VideoPostAdapter_1 adapter = null;
    private ArrayList<YoutubeDataModel> mListData = new ArrayList<>();

    private EditText editText = null;

    public LiveFragment() {
        // Required empty public constructor
    }

    private Button btnSend = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        mList_videos = (RecyclerView) view.findViewById(R.id.mList_videos_live);
        initList(mListData);
        new RequestYoutubeAPI().execute();

        btnSend = (Button)view.findViewById(R.id.btnSend);
        editText = (EditText) view.findViewById(R.id.textoPesquisa);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = editText.getText().toString();
                texto = texto.replaceAll(" ","+");
                CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?q="+texto+"&part=snippet&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY + "";
                Log.i("URL FORMADA:", CHANNLE_GET_URL);
                initList(mListData);
                new RequestYoutubeAPI().execute();
            }
        });
        return view;
    }

    public void btnSend(View view){
        editText = (EditText) view.findViewById(R.id.textoPesquisa);
        String texto = editText.getText().toString();
        CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?q="+texto+"&part=snippet&order=date&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY + "";
        mList_videos = (RecyclerView) view.findViewById(R.id.mList_videos_live);
        initList(mListData);
        new RequestYoutubeAPI().execute();
    }

    private void initList(ArrayList<YoutubeDataModel> mListData) {
        mList_videos.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VideoPostAdapter_1(getActivity(), mListData, new OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeDataModel item) {
                YoutubeDataModel youtubeDataModel = item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(YoutubeDataModel.class.toString(), youtubeDataModel);
                startActivity(intent);
            }
        });
        mList_videos.setAdapter(adapter);

    }




    //create an asynctask to get all the data from youtube
    private class RequestYoutubeAPI extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(CHANNLE_GET_URL);
            Log.e("URL", CHANNLE_GET_URL);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    mListData = parseVideoListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<YoutubeDataModel> parseVideoListFromResponse(JSONObject jsonObject) {
        ArrayList<YoutubeDataModel> mList = new ArrayList<>();

        if (jsonObject.has("items")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    if (json.has("id")) {
                        JSONObject jsonID = json.getJSONObject("id");
                        String video_id = "";
                        if (jsonID.has("videoId")) {
                            video_id = jsonID.getString("videoId");
                        }
                        if (jsonID.has("kind")) {
                            if (jsonID.getString("kind").equals("youtube#video")) {
                                YoutubeDataModel youtubeObject = new YoutubeDataModel();
                                JSONObject jsonSnippet = json.getJSONObject("snippet");
                                String title = jsonSnippet.getString("title");
                                String description = jsonSnippet.getString("description");
                                String publishedAt = jsonSnippet.getString("publishedAt");
                                String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

                                youtubeObject.setTitle(title);
                                youtubeObject.setDescription(description);
                                youtubeObject.setPublishedAt(publishedAt);
                                youtubeObject.setThumbnail(thumbnail);
                                youtubeObject.setVideo_id(video_id);
                                mList.add(youtubeObject);

                            }
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return mList;

    }

}