package com.sumit.myidea;

import android.net.Uri;

public class plants {
   String plant_name;
   String uri;

    public plants(String plant_name, String uri) {
        this.plant_name = plant_name;
        this.uri = uri;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
