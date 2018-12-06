package ml.uncoded.margsahayak.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class OfflineComplainModel extends RealmObject {


    @PrimaryKey
    private int id;
    private String grievanceDescription;
    private String Imgurl;
    private RealmList<String> location;
    private String time;
    private String grievanceName;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getGrievanceDescription() {
        return grievanceDescription;
    }
    public void setGrievanceDescription(String grievanceDescription) {
        this.grievanceDescription = grievanceDescription;
    }

    public String getImgurl() {
        return Imgurl;
    }
    public void setImgurl(String imgurl) {
        Imgurl = imgurl;
    }


    public RealmList<String> getLocation() {
        return location;
    }
    public void setLocation(RealmList<String> location) {
        this.location = location;
    }

    public String getGrievanceName() {
        return grievanceName;
    }
    public void setGrievanceName(String grievanceName) {
        this.grievanceName = grievanceName;
    }


}