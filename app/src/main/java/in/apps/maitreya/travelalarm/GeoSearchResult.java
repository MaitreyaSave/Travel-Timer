package in.apps.maitreya.travelalarm;

import android.location.Address;

/**
 * Created by Maitreya on 26-Feb-17.
 */

public class GeoSearchResult {

    private Address address;

    public GeoSearchResult(Address address)
    {
        this.address = address;
    }

    public String getAddressAsString(){

        String display_address = "";
        display_address += address.getAddressLine(0) + "\n";

        for(int i = 1; i < address.getMaxAddressLineIndex(); i++)
        {
            display_address += address.getAddressLine(i) + ", ";
        }

        display_address = display_address.substring(0, display_address.length() - 2);

        return display_address;
    }
    public Address getAddress(){
        return address;
    }
    public String toString(){
        String display_address = "";

        if(address.getFeatureName() != null)
        {
            display_address += address + ", ";
        }

        for(int i = 0; i < address.getMaxAddressLineIndex(); i++)
        {
            display_address += address.getAddressLine(i);
        }

        return display_address;
    }
}
