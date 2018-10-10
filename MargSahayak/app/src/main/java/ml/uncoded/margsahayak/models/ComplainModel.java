package ml.uncoded.margsahayak.models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ComplainModel extends RealmObject{


    @SerializedName("_id")
    private String id;
    @SerializedName("road_code")
    private String roadCode;
    @SerializedName("road_type")
    private String roadType;
    @SerializedName("url")
    private String url;
    @SerializedName("name")
    private String roadName;
    @SerializedName("description")
    private String description;
    @SerializedName("location")
    private RealmList<String> location;
    @SerializedName("complaint_status")
    private String complaintStatus;
    @SerializedName("estimated_time")
    private String estimatedTime;
    @SerializedName("griev_type")
    private String grivType;
    @SerializedName("comment")
    private RealmList<String> comment;
    @SerializedName("time")
    private String time;
    @SerializedName("officer_id")
    private String officerId;
    @SerializedName("officer_email")
    private String officerEmail;
    @SerializedName("officer_name")
    private String officerName;
    @SerializedName("success")
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @SerializedName("data")
    private String msg;


    public ComplainModel() { }

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoadCode() {
        return roadCode;
    }

    public RealmList<String> getLocation() {
        return location;
    }

    public void setLocation(RealmList<String> location) {
        this.location = location;
    }

    public RealmList<String> getComment() {
        return comment;
    }

    public void setComment(RealmList<String> comment) {
        this.comment = comment;
    }

    public void setRoadCode(String roadCode) {
        this.roadCode = roadCode;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public String getGrivType() {
        return grivType;
    }

    public void setGrivType(String grivType) {
        this.grivType = grivType;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOfficerId() {
        return officerId;
    }

    public void setOfficerId(String officerId) {
        this.officerId = officerId;
    }

    public String getOfficerEmail() {
        return officerEmail;
    }

    public void setOfficerEmail(String officerEmail) {
        this.officerEmail = officerEmail;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }
}