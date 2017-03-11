package in.apps.maitreya.travelalarm;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Maitreya on 11-Mar-17.
 */

public class Route {
    private String title;
    private LatLng source;
    private LatLng destination;
    private String sourceString;
    private String destinationString;
    private boolean deleteYN;
    public Route(){

    }
    public Route(String title){
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public LatLng getSource() {
        return source;
    }

    public void setSource(LatLng source) {
        this.source = source;
    }

    public String getSourceString() {
        return sourceString;
    }

    public void setSourceString(String sourceString) {
        this.sourceString = sourceString;
    }

    public String getDestinationString() {
        return destinationString;
    }

    public void setDestinationString(String destinationString) {
        this.destinationString = destinationString;
    }

    public boolean isDeleteYN() {
        return deleteYN;
    }

    public void setDeleteYN(boolean deleteYN) {
        this.deleteYN = deleteYN;
    }
}
