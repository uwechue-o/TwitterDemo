# TwitterDemo

A custom Twitter client demo


This is a native/Java app and makes use of the following technologies: RxJava2, Retrofit2, SQLite&SQLBrite, JNI, and Stetho(for examining the db in real-time).

**First Steps: replace the placeholder strings in _/src/cpp/mydata.cpp_ with valid Twitter Secret and Twitter Consumer Key strings, then recompile.**

Notes:

The Hashtags/tweets fetch is based on fetching the most "popular" tweets for a particular hashtag, then sorting them in descending order of #ReTweets, then applying a DISTINCT filter on the Twitter account names in the resultset. This way, only the most popular tweets (one per user) will be displayed. There is a switch to enable/disable the uniqueness filter.

Data fetches are executed on a fixed interval - there is a switch provided to allow the user to set the interval. The interval is defined directly on the RxJava2 observable object.

JUnit tests and Instrumented tests included are rudimentary and need to be fleshed out more. They can be executed by right-clicking on the Java testfile names and selecting 'Run' on the filename.

The historical hashtags/tweets fetch is incomplete: although most of the data storage and data selection logic and RxJava bindings are in place, the SQLite/SQLBrite db code/logic needs to be fleshed out some more and a new activity created to display the results of the historical hashtags/tweets fetch.

sample screenshots (from emulator) are located under the /ScreenShots folder.

further @ToDo list: enhance the UI to better conform to Material Design principles (add more layout containers with appropriate elevation shadows)
