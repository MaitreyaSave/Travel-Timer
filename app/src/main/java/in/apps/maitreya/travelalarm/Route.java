package in.apps.maitreya.travelalarm;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Maitreya on 11-Mar-17.
 *
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

    /*public void setTitle(String title) {
        this.title = title;
    }
    */
    LatLng getDestination() {
        return destination;
    }

    void setDestination(LatLng destination) {
        this.destination = destination;
    }

    LatLng getSource() {
        return source;
    }

    void setSource(LatLng source) {
        this.source = source;
    }

    String getSourceString() {
        return sourceString;
    }

    void setSourceString(String sourceString) {
        this.sourceString = sourceString;
    }

    String getDestinationString() {
        return destinationString;
    }

    void setDestinationString(String destinationString) {
        this.destinationString = destinationString;
    }

    boolean isDeleteYN() {
        return deleteYN;
    }

    void setDeleteYN(boolean deleteYN) {
        this.deleteYN = deleteYN;
    }
}
