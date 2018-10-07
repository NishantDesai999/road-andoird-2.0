package ml.uncoded.margsahayak.models;

import com.google.gson.annotations.SerializedName;

public class BisagResponse {
    @SerializedName("distance")
    public String distance;

    @SerializedName("road_name")
    public String roadName;

    @SerializedName("uniqe_code")
    public String roadCode;

    @SerializedName("category_s_p")
    public String raodType;

}
