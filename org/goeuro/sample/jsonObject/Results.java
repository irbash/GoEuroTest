package org.goeuro.sample.jsonObject;

import java.util.List;

//Results: Holds the result objects returned as JSON
public class Results
{
    private List<GeoObject> results;

    public List<GeoObject> getGeoObjects()
    {
        return results;
    }

    public void setGeoObjects(List<GeoObject> geoObjects)
    {
        this.results = geoObjects;
    }
}
