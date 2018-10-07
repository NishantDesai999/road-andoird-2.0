package ml.uncoded.margsahayak.models;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
public class NotificationComplaintModel extends RealmObject {
    @SerializedName("_id")
    private String id;

    @SerializedName("complaint_status")
    private String complaintStatus;

    @SerializedName("comments")
    private RealmList<String> comments;

    @SerializedName("estimated_date")
    private String estimatedDate;

    @SerializedName("officer_id")
    private String officerId;

    @SerializedName("officer_email")
    private String officerEmail;

    public void setComments(RealmList<String> comments) {
        this.comments = comments;
    }

    @SerializedName("officer_name")
    private String officerName;
    String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public RealmList<String> getComments() {
        return comments;
    }

    public String getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(String estimatedDate) {
        this.estimatedDate = estimatedDate;
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
