package com.golden.youtube.utill;

import android.content.Context;
import android.util.Log;

import com.golden.youtube.model.VideoItem;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataConnector {





    private YouTube youtube;

    private YouTube.Search.List query;


    public static final String KEY = "AIzaSyB3cfE1XPXKjPHNsq39hOhWqts9weudvgo";


    public static final String PACKAGENAME = "com.golden.youtube.utill";


    public static final String SHA1 = "CD:1A:B6:00:AE:95:BB:32:05:57:B6:AA:45:F9:E8:CF:9D:C4:5B:D2";


    private static final long MAXRESULTS = 25;


    public DataConnector(Context context) {

        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {

            //initialize method helps to add any extra details that may be required to process the query
            @Override
            public void initialize(HttpRequest request) throws IOException {

                //setting package name and sha1 certificate to identify request by server
                request.getHeaders().set("X-Android-Package", PACKAGENAME);
                request.getHeaders().set("X-Android-Cert",SHA1);
            }
        }).setApplicationName("SearchYoutube").build();

        try {


            query = youtube.search().list("id,snippet");


            query.setKey(KEY);


            query.setType("video");

            query.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/thumbnails/high/url)");

        } catch (IOException e) {


            Log.d("YC", "Could not initialize: " + e);
        }
    }

    public List<VideoItem> search(String keywords) {


        query.setQ(keywords);

        query.setMaxResults(MAXRESULTS);



        try {


            SearchListResponse response = query.execute();


            List<SearchResult> results = response.getItems();


            List<VideoItem> items = new ArrayList<VideoItem>();


            if (results != null) {


                items = setItemsList(results.iterator());
            }

            return items;

        } catch (IOException e) {


            Log.d("YC", "Could not search: " + e);
            return null;
        }
    }


    private static List<VideoItem> setItemsList(Iterator<SearchResult> iteratorSearchResults) {


        List<VideoItem> tempSetItems = new ArrayList<>();


        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }


        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();


            ResourceId rId = singleVideo.getId();


            if (rId.getKind().equals("youtube#video")) {


                VideoItem item = new VideoItem();


                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getHigh();


                item.setId(singleVideo.getId().getVideoId());
                item.setTitle(singleVideo.getSnippet().getTitle());
                item.setDescription(singleVideo.getSnippet().getDescription());
                item.setThumbnailURL(thumbnail.getUrl());


                tempSetItems.add(item);


                System.out.println(" Video Id" + rId.getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println(" Description: "+ singleVideo.getSnippet().getDescription());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        }
        return tempSetItems;
    }
}













