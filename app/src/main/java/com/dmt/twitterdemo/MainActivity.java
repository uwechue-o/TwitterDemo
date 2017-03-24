package com.dmt.twitterdemo;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.dmt.twitterdemo.adapter.DataAdapter;
import com.dmt.twitterdemo.adapter.HashtagArrayAdapter;
import com.dmt.twitterdemo.database.SQLDatabaseHelper;
import com.dmt.twitterdemo.model.DataUIModel;
import com.dmt.twitterdemo.model.HashtagRowItem;
import com.dmt.twitterdemo.model.UserModel_childNode;
import com.dmt.twitterdemo.model.UserModel_hashtag;
import com.dmt.twitterdemo.model.UserModel_parentNode;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Func1;

import static com.dmt.twitterdemo.Utils.escape;
import static com.dmt.twitterdemo.Utils.hideKeyboard;
import static com.dmt.twitterdemo.database.SQLDatabaseHelper.KEY_DATE_ENTERED;
import static com.dmt.twitterdemo.database.SQLDatabaseHelper.KEY_HASHTAG;
import static com.dmt.twitterdemo.database.SQLDatabaseHelper.KEY_HASHTAG_ID;
import static com.dmt.twitterdemo.database.SQLDatabaseHelper.KEY_ID;
import static com.dmt.twitterdemo.database.SQLDatabaseHelper.KEY_TWEET_STRING;
import static com.dmt.twitterdemo.database.SQLDatabaseHelper.KEY_USER_PIC_URL;
import static com.dmt.twitterdemo.database.SQLDatabaseHelper.KEY_USER_REAL_NAME;
import static com.dmt.twitterdemo.database.SQLDatabaseHelper.KEY_USER_TWITTER_NAME;
import static com.dmt.twitterdemo.database.SQLDatabaseHelper.TABLE_HASHTAGS;
import static com.dmt.twitterdemo.database.SQLDatabaseHelper.TABLE_TWEETDATA;

public class MainActivity extends AppCompatActivity {

    private String credentials;
    private static String RESULT_TYPE = "popular";
    private static String DATA_COUNT = "100";
    private boolean DISTINCT_SELECTED = false;
    private final int DEFAULT_INTERVAL = 60;
    private int REFRESH_INTERVAL = DEFAULT_INTERVAL;   // data refresh interval. measured in seconds
    private long MOST_RECENT_HASHTAG_ROW;   // the db table RowID of the most recently-inserted hashtag
    private HashtagRowItem SELECTED_HASHTAG_HISTORY;

    Button requestTokenButton;
    Button requestUserDetailsButton;
    EditText hashtagEditText;
    TextView hashtagTextView;

    ImageView userpicImageView;
    private Switch duplicatesFilterSwitch;

    ITwitterAPI twitterObj;
    OAuthToken token;
    private View container;
    private DialogSpinner hashtagSpinner;

    private Resources res;

    private CompositeDisposable compositeDisposables;
    private ArrayList<DataUIModel> userdataList;
    private DataAdapter adapter;
    private RecyclerView recyclerView;
    private RadioButton btn1, btn2, btn3, btn4;

    private SqlBrite sqlBrite;
    private BriteDatabase db;
    private SQLDatabaseHelper dbHelper;

    // JNI is used for hiding away sensitive credentials
    static {
        System.loadLibrary("mydata");
    }

    public native String getTwitterSecret();
    public native String getTwitterConsumerKey();

    private void initCreds()
    {
        credentials = Credentials.basic(getTwitterConsumerKey(), getTwitterSecret());
    }

    private void initRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    // initialize database and wrapper
    private void initDB()
    {
        dbHelper = new SQLDatabaseHelper(this);
        sqlBrite = new SqlBrite.Builder().build();
        db = sqlBrite.wrapDatabaseHelper(dbHelper, rx.schedulers.Schedulers.io());    // !!NOTE: need to specify full path of Scheduler because SQLBrite relies on RxJava1
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hashtags);

        requestTokenButton = (Button) findViewById(R.id.request_token_button);
        requestUserDetailsButton = (Button) findViewById(R.id.request_user_details_button);
        hashtagEditText = (EditText) findViewById(R.id.hashtag_edittext);
        hashtagTextView = (TextView) findViewById(R.id.hashtag_textview);

        userpicImageView = (ImageView) findViewById(R.id.user_picture);

        container = findViewById(android.R.id.content);
        hashtagSpinner = (DialogSpinner) findViewById(R.id.hashtags_spinner);
        hashtagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int idx, long id) {
                HashtagRowItem selectedItem = (HashtagRowItem) parent.getSelectedItem();
                SELECTED_HASHTAG_HISTORY = new HashtagRowItem(selectedItem.getTime(),selectedItem.getTag());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn1 = (RadioButton) findViewById(R.id.refresh5sec);
        btn2 = (RadioButton) findViewById(R.id.refresh10sec);
        btn3 = (RadioButton) findViewById(R.id.refresh20sec);
        btn4 = (RadioButton) findViewById(R.id.refresh60sec);

        duplicatesFilterSwitch = (Switch) findViewById(R.id.toggle);
        //attach a listener to check for changes in state
        duplicatesFilterSwitch.setOnCheckedChangeListener((view,isChecked) -> DISTINCT_SELECTED = isChecked);
/*      duplicatesFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {        //  old code. replaced by lambda xpression
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DISTINCT_SELECTED = isChecked;
            }
        });
*/

        res = getResources();   // pre-fetch the Resources instance to avoid multiple method calls

        compositeDisposables = new CompositeDisposable();

        initDB();

        // first retrieve Twitter credentials
        initCreds();

        initRecyclerView();

        createTwitterApi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposables.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.overflow, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // process the overflow menu selection
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.about)
        {
            Snackbar bar = Snackbar.make(container, res.getString(R.string.about_text), Snackbar.LENGTH_INDEFINITE);
            bar.setAction(res.getString(R.string.dismiss), (view)->bar.dismiss());
/*          bar.setAction(res.getString(R.string.dismiss), new View.OnClickListener() {     // old code. now replaced by lambda xpression
                @Override
                public void onClick(View v) {
                    bar.dismiss();
                }
            });
*/

            // enable multiline text
            View barView = bar.getView();
            TextView textView = (TextView)barView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(7);
            textView.setTextSize(12f);
            bar.show();
        }
        else{   // user selected to access hashtag history

            Snackbar.make(container, res.getString(R.string.stored_tags_text), Snackbar.LENGTH_LONG).show();
            fetchAndDisplayPreviousHashtags();
        }

        return true;
    }

    /**
     * Fetches the hashtags info from the db and displays them in dialog window
     */
    private void fetchAndDisplayPreviousHashtags()
    {
        // fetch and display list of previous hashtags
        Observable<List<UserModel_hashtag>> hashtags = fetchPreviousHashtags();
        hashtags.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::displayHashtagsHistorical, this::onErrorHashtags);
    }

    /**
     * Displays a list of hashtag tablerow clickable data
     *
     * @param nodelist     list of hashtag tablerow objects
     */
    private void displayHashtagsHistorical(List<UserModel_hashtag> nodelist)
    {
        List<String> tags = new ArrayList<>(),
                     times = new ArrayList<>();

        List<HashtagRowItem> values = new ArrayList<>();
        for(UserModel_hashtag node : nodelist){
            values.add(new HashtagRowItem(node.getTimestamp(),node.getTag()));
        }

        HashtagArrayAdapter dataAdapter = new HashtagArrayAdapter(this, R.layout.hashtag_db_row, values.stream().toArray(HashtagRowItem[]::new));
        hashtagSpinner.setAdapter(dataAdapter);
        hashtagSpinner.setVisibility(View.VISIBLE);
    }

    /**
     * Retrieve historical tweet data based on user's selected historical hashtag.
     * Display these details in a new activity.
     *
     * Uses ROW ID of selected timestamp/hashtag as foreign key into tweets table
     *
     * @param timestamp
     * @param hashtag
     */
    private void displayTweetsHistorical(String timestamp, String hashtag)
    {


    }


    private void onErrorHashtags(Throwable error) {

        Snackbar.make(container, res.getString(R.string.fail_tags_fetch), Snackbar.LENGTH_SHORT).show();
        Log.w("Database datafetch","Error: "+error.getLocalizedMessage());
    }

    /**
     * Retrieves list of hashtag-related data
     *
     * Note: the RxJava1 observable must be cast into an RxJava2 observable using the interop utilities
     * @return
     */
    private Observable<List<UserModel_hashtag>> fetchPreviousHashtags()
    {
        return RxJavaInterop.toV2Observable(db.createQuery(TABLE_HASHTAGS, "SELECT * FROM "+TABLE_HASHTAGS)
                .map(new Func1<SqlBrite.Query, List<UserModel_hashtag>>()
                {
                    @Override
                    public List<UserModel_hashtag> call(SqlBrite.Query query)
                    {
                        Cursor cursor = query.run();
                        List<UserModel_hashtag> result = new ArrayList<>(cursor.getCount());
                        while (cursor.moveToNext())
                        {
                            try {
                                int tagIndex = cursor.getColumnIndexOrThrow(KEY_HASHTAG);
                                int timestampIndex = cursor.getColumnIndexOrThrow(KEY_DATE_ENTERED);
                                String tag = cursor.getString(tagIndex);
                                String timestamp = cursor.getString(timestampIndex);
                                result.add(new UserModel_hashtag(timestamp, tag));
                            }catch(IllegalArgumentException illex){
                                Log.e("Fetch Hashtags", illex.getMessage());
                            }
                        }
                        cursor.close();
                        return result;
                    }
                }));
    }

    public void onRadioButtonClicked(View view) {
        // determine if radiobutton is now checked
        boolean checked = ((RadioButton) view).isChecked();

        // determine which radio button was clicked, and set refresh interval accordingly
        switch(view.getId()) {
            case R.id.refresh5sec:
                if (checked)
                    REFRESH_INTERVAL = 5;
                    break;
            case R.id.refresh10sec:
                if (checked)
                    REFRESH_INTERVAL = 10;
                    break;
            case R.id.refresh20sec:
                if (checked)
                    REFRESH_INTERVAL = 20;
                    break;
            case R.id.refresh60sec:
                if (checked)
                    REFRESH_INTERVAL = 60;
                    break;
        }
    }

    private void createTwitterApi()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        token != null ? token.getAuthorization() : credentials);

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ITwitterAPI.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())      // @ToDo : RxJava2 integration
                .build();

        twitterObj = retrofit.create(ITwitterAPI.class);
    }

    public void onClickRequestToken(View view)
    {
        twitterObj.postCredentials("client_credentials").enqueue(tokenCallback);
    }

    private Observable<UserModel_parentNode> doDataFetch()
    {
        String editTextInput = hashtagEditText.getText().toString();
        if (!editTextInput.isEmpty())
            return(twitterObj.getUserInfo(editTextInput, RESULT_TYPE, DATA_COUNT));
        else
            return null;
    }

    public void onClickGetTwitterData(View view)
    {
        String editTextInput = hashtagEditText.getText().toString();
        if (!editTextInput.isEmpty()) {

            // persist the hashtag entered
            ContentValues values = new ContentValues();
            values.put(KEY_HASHTAG, editTextInput);
            Observable<Long> storeHashtag = Observable.fromCallable(()->db.insert(TABLE_HASHTAGS,values));

            storeHashtag.subscribe(rowID -> {MOST_RECENT_HASHTAG_ROW = rowID; Snackbar.make(container, res.getString(R.string.data_persisted), Snackbar.LENGTH_SHORT).show();});

            Observable.interval(0, REFRESH_INTERVAL, TimeUnit.SECONDS)
                    .flatMap(tick -> doDataFetch())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .map(datum -> datum.getStatuses())
                    .subscribe(this::handleResponse,this::handleError);

            // twitterObj.getUserInfo(editTextInput, RESULT_TYPE, DATA_COUNT).enqueue(userInfoCallback);    // Old way - using Retrofit2 without RxJava2
        } else {
            Snackbar.make(container, res.getString(R.string.need_username), Snackbar.LENGTH_LONG).show();
        }

        // hide the softkeyboard
        hideKeyboard(this);
    }

    /**
     * Process the fetched Twitter data
     *
     * @ToDo : convert this entire method into more succinct Java8 syntax using streams
     *
     * @param l     list of JSON child nodes
     */
    private void handleResponse(List<UserModel_childNode> l)
    {
        // first, sort the node list by the number of retweets per node (in DESCENDING order)
        Collections.sort(l);

        // now, eliminate duplicate user instances. filter on twitter ID
        if(DISTINCT_SELECTED) {
            //l.stream().filter(distinctByKey(tweet -> tweet.getUser().getScreen_name()));
            l = Utils.distinctByKey(l);
        }

        userdataList = new ArrayList<>();
        for(UserModel_childNode child : l)
        {
            DataUIModel d = new DataUIModel();
            d.setRealName(child.getUser().getName());
            d.setTweet(child.getText());
            d.setTwitterName(child.getUser().getScreen_name());
            d.setRetweetCount(child.getRetweet_count());
            d.setUserPicUrl(child.getUser().getProfile_image_url());

            // populate the user image asynchronously
            fetchUserImage(child.getUser().getProfile_image_url(), d);

            userdataList.add(d);
        }

        adapter = new DataAdapter(userdataList);
        recyclerView.setAdapter(adapter);

        // persist the tweets data
        for(DataUIModel datum : userdataList)
        {
            //@ToDo -- must replace escaped strings with proper Prepared Statements
            String querystring = "INSERT INTO "+TABLE_TWEETDATA
                    +" ("+KEY_HASHTAG_ID+","+KEY_USER_REAL_NAME+","+KEY_USER_TWITTER_NAME+","+KEY_TWEET_STRING+","+KEY_USER_PIC_URL+") "
                    +"VALUES ((SELECT "+KEY_HASHTAG+" FROM "+TABLE_HASHTAGS+" WHERE "+KEY_ID+"='"+MOST_RECENT_HASHTAG_ROW+"') || '','"
                    +escape(datum.getRealName())+"','"
                    +escape(datum.getTwitterName())+"','"
                    +escape(datum.getTweet())+"','"
                    +escape(datum.getUserPicUrl())+"')";

            Observable<SqlBrite.Query> storeTweet = RxJavaInterop.toV2Observable(db.createQuery(TABLE_TWEETDATA, querystring));
            storeTweet.subscribe(query -> { Cursor cursor = query.run();
                                            Snackbar.make(container, res.getString(R.string.data_persisted), Snackbar.LENGTH_SHORT).show();});
/*            storeTweet.subscribe(new Consumer<SqlBrite.Query>() {       // -- old code. replaced by lambda xpression
                @Override
                public void accept(SqlBrite.Query query) {
                    Cursor cursor = query.run();
                    Snackbar.make(container, res.getString(R.string.data_persisted), Snackbar.LENGTH_SHORT).show();
                }
            });
*/
        }
    }

    private void handleError(Throwable error) {

        Snackbar.make(container, res.getString(R.string.fail_data_fetch), Snackbar.LENGTH_SHORT).show();
        Log.w("Retrofit datafetch","Error: "+error.getLocalizedMessage());
    }

    /**
     * Enable or disable all radiobuttons at once
     *
     * @param enableTheButton   TRUE=enable, FALSE=disable
     */
    private void radioButtonsEnable(boolean enableTheButton)
    {
        btn1.setEnabled(enableTheButton);
        btn2.setEnabled(enableTheButton);
        btn3.setEnabled(enableTheButton);
        btn4.setEnabled(enableTheButton);
    }

    Callback<OAuthToken> tokenCallback = new Callback<OAuthToken>() {
        @Override
        public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
            if (response.isSuccessful())
            {
                radioButtonsEnable(false);
                requestTokenButton.setEnabled(false);
                requestUserDetailsButton.setEnabled(true);
                hashtagTextView.setEnabled(true);
                hashtagEditText.setEnabled(true);
                token = response.body();
                Snackbar b = Snackbar.make(container, res.getString(R.string.auth_success), Snackbar.LENGTH_SHORT);
                Utils.setSnackbarTextColour(b, Color.YELLOW);
                b.show();
            } else {
                Snackbar.make(container, res.getString(R.string.fail_token_request), Snackbar.LENGTH_LONG).show();
                Log.w("tokenCallback", "Code: " + response.code() + "Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<OAuthToken> call, Throwable t) {
            t.printStackTrace();
        }
    };

    /**
     * Retrieves user's pic and binds it to the user object
     *
     * @param url       location of image
     * @param userObj   user object that will hold the retrieved bitmap
     */
    public void fetchUserImage(String url, DataUIModel userObj)
    {
        Call<ResponseBody> call = twitterObj.fetchImage(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // render the image in UI
                        Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                        userObj.setUserPic(bm);
                        adapter.notifyDataSetChanged(); // force image to display immediately
                    } else {
                        Snackbar.make(container, res.getString(R.string.fail_no_image_data), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(container, res.getString(R.string.fail_image_webservicecall), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // @ToDo
                Log.w("fetchUserPic", "Error Message: " + t.getMessage());
            }
        });

    }

}
