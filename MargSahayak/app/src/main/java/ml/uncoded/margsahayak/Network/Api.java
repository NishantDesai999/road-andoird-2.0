package ml.uncoded.margsahayak.Network;


import java.util.List;

import ml.uncoded.margsahayak.models.AuthResponse;
import ml.uncoded.margsahayak.models.BisagResponse;
import ml.uncoded.margsahayak.models.ComplainModel;
import ml.uncoded.margsahayak.models.NotificationComplaintModel;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "http://192.168.2.10:3003/api/android/";

    @GET("testLate")
    Call<ComplainModel> getLateReply();

    @GET("test3")
    Call<ComplainModel> getComplain();

    @POST("postNewComplaint")
    Call<ComplainModel> postComplaint(@Header("auth") String auth, @Body ComplainModel mComplainModel);

    @FormUrlEncoded
    @POST("otp")
    Call<AuthResponse> login(@Field("phoneNo") String phone);

    @FormUrlEncoded
    @POST("otp-verify")
    Call<AuthResponse> otp(@Field("otpNo") String otp, @Field("phoneNo") String phone);

    @FormUrlEncoded
    @POST("signup")
    Call<AuthResponse> register(@Header("auth") String auth, @Field("name") String name, @Field("email") String email);

    @GET("notifications")
    Call<List<NotificationComplaintModel>> getComplaintNotification(@Header("auth") String auth);


    @Multipart
    @POST("imageUpload")
    Call<AuthResponse> uploadImage(@Header("auth") String auth, @Part MultipartBody.Part file1);

    @GET("https://ncog.gov.in/RNB_mob_data/get_road?code=4862")
        //PAss Code 4862
    Call<List<BisagResponse>> callBisagApi(@Query("lat") String lat, @Query("lon") String lon);


//    @DELETE("delete")
//    Call<mixQuestion> delete();
//
//
////    @GET("/2.2/questions/{id}/answers?order=desc&sort=votes&site=stackoverflow")
////    Call<ListWrapper<Answer>> getAnswersForQuestion(@Path("id") String questionId);
//
//    @FormUrlEncoded
//    @POST("/2.2/answers/{id}/upvote")
//    Call<ResponseBody> postUpvoteOnAnswer(@Path("id") int answerId, @Field("access_token") String accessToken, @Field("key") String key, @Field("site") String site, @Field("preview") boolean preview, @Field("filter") String filter);
}