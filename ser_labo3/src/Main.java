import org.json.simple.JSONArray;

public class Main {

    public static void main(String[] args) {


        GeoJsonReader geoJsonReader = new GeoJsonReader();
        KMLWriter kmlWriter = new KMLWriter();

        JSONArray countriesArray = geoJsonReader.parseGeoJson("src/countries.geojson");

        kmlWriter.generateKML(countriesArray, "src/countries.geojson");

    }

}
