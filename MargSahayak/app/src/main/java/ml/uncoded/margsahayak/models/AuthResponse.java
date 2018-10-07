package ml.uncoded.margsahayak.models;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("data")
    public String data;

    @SerializedName("success")
    public String success;

    @SerializedName("isNewUser")
    public String status;
}
