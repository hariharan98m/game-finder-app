package io.hasura.orange.db_conn;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public interface ApiInterface {

    @POST(NetworkURL.INSERT)
    Call<ReqResponse> insert(@Body AuthReq body);

    @POST(NetworkURL.UPDATE_LAT_LONG)
    Call<ReqResponse> update_lat_long(@Body AuthReq body);

    @POST(NetworkURL.UPDATE_GAME_PLACE)
    Call<ReqResponse> update_game_place(@Body AuthReq body);

    @POST(NetworkURL.SELECT_FRIENDS)
    Call<List<SelectFriends>> select_friends(@Body AuthReq body);

    @POST(NetworkURL.SELECT_GAME_STARTER)
    Call<SelectFriends> select_game_starter(@Body AuthReq body);

    /*
    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> insert_into_request_or_confirm(@Body InsertIntoRequestORConfirm body);

    @POST(NetworkURL.QUERY)
    Call<List<HomePageFriendsReqORConfirm>> select(@Body SelectFriendsData body);

    @POST(NetworkURL.QUERY)
    Call<List<FriendSongsList>> get_songs_for_this_friend(@Body SelectFriendsSongsRequest body);

    @POST(NetworkURL.QUERY)
    Call<List<SongComments>> get_comments_for_this_song(@Body CommentTextListRequest body);

    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> insert_comments_for_this_song(@Body CommentTextListRequest body);

    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> insert_song(@Body SongInsert body);

    @GET(NetworkURL.LOGOUT)
    Call<ErrorResponse> logout();

    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> insert_like(@Body InsertLikeModelRequest body);

    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> delete_like(@Body InsertLikeModelRequest body);



    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> insert_dp(@Body DpInsert body);

    @POST(NetworkURL.QUERY)
    Call<List<BitmapResponse>> select_dp_link(@Body DPSelect body);


    //@POST(NetworkURL.ARTICLES)
    //Call<List<ArticlesResponse>> fetch_articles(@Body ArticleListRequest body);

    */
}
