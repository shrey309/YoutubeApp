package com.golden.youtube.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.golden.youtube.Adapter.YoutubeAdapter;
import com.golden.youtube.R;
import com.golden.youtube.model.VideoItem;
import com.golden.youtube.utill.DataConnector;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private EditText searchInput;


    private YoutubeAdapter youtubeAdapter;


    private RecyclerView mRecyclerView;


    private ProgressDialog mProgressDialog;


    private Handler handler;


    private List<VideoItem> searchResults;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        mProgressDialog = new ProgressDialog(this);
        searchInput = (EditText)findViewById(R.id.search_input);
        mRecyclerView = (RecyclerView) findViewById(R.id.videos_recycler_view);


        mProgressDialog.setTitle("Searching...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        handler = new Handler();



        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if((s.toString().length()>=4))
                {

                    searchOnYoutube(searchInput.getText().toString());

                }

            }

            @Override
            public void afterTextChanged(Editable s)
            {




            }
        });


        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {



            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                if(actionId == EditorInfo.IME_ACTION_SEARCH  )

                {


                    mProgressDialog.setMessage("Finding videos for "+v.getText().toString());


                    mProgressDialog.show();


                    searchOnYoutube(v.getText().toString());



                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    //hiding the keyboard once search button is clicked
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    return false;
                }
                return true;
            }
        });

    }


    private void searchOnYoutube(final String keywords){


        new Thread(){


            public void run(){


                DataConnector dc = new DataConnector(MainActivity.this);


                searchResults = dc.search(keywords);


                handler.post(new Runnable(){


                    public void run(){

                        fillYoutubeVideos();


                        mProgressDialog.dismiss();
                    }
                });
            }

        }.start();
    }


    private void fillYoutubeVideos(){


        youtubeAdapter = new YoutubeAdapter(getApplicationContext(),searchResults);

        mRecyclerView.setAdapter(youtubeAdapter);


        youtubeAdapter.notifyDataSetChanged();
    }



}