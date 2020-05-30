import org.json.simple.JSONArray;

public class Main {

    public static void main(String[] args) {

        GeoJsonReader geoJsonReader = new GeoJsonReader();
        KMLWriter kmlWriter = new KMLWriter();
        // lecture et parsing du fichier en GEOJSON
        JSONArray countriesArray = geoJsonReader.parseGeoJson("src/countries.geojson");
        // création du fichier en KML
        kmlWriter.generateKML(countriesArray, "src/countries.kml");

    }

}
