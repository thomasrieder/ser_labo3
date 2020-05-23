import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.jdom2.*;
import org.jdom2.output.*;


public class Main {

    private static final String finalKMLPAth = "src/coutriesKML.kml";

    static final Element root = new Element("kml");
    static final Element docElem = new Element("Document");
    static final  Document document = new Document(root);

    public static void main(String[] args) {

        //JSON parser object pour lire le fichier
        JSONParser jsonParser = new JSONParser();

//        root.setAttribute(new Attribute("xmlns", "http://www.opengis.net/kml/2.2"));
        root.addContent(docElem);

        Element styleElem = new Element("Style");
        styleElem.setAttribute(new Attribute("id", "lineStyle"));

        Element styleLine = new Element("LineStyle");
        styleLine.addContent(new Element("color").setText("ff0000ff"));
        styleLine.addContent(new Element("width").setText("2"));

        styleElem.addContent(styleLine);
        docElem.addContent(styleElem);

        try (FileReader reader = new FileReader("src/countries.geojson")) {

            // lecture du fichier
            JSONObject obj = (JSONObject)jsonParser.parse(reader);

            JSONArray countriesCollection = (JSONArray) obj.get("features");
//            System.out.println(countriesCollection);

            // parcours du tableau de pays
            countriesCollection.forEach(country->parseCountryObject((JSONObject)country));


            XMLOutputter xmlOutputer = new XMLOutputter();
            xmlOutputer.setFormat(Format.getPrettyFormat());
            xmlOutputer.output(document, new FileWriter(finalKMLPAth));

            System.out.println("XML File was created successfully!");

        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseCountryObject(JSONObject country) {

        //récupère les propriétés du pays
        JSONObject countryProperties = (JSONObject) country.get("properties");

        //récupère la géometrie du pays
        JSONObject countryGeometry = (JSONObject) country.get("geometry");

        JSONArray countryPolygons = (JSONArray) countryGeometry.get("coordinates");


        //System.out.println("coordinates : " + countryCoordinates.size());

        // obtenir les détails ...
        String countryType    = (String) country.get("type");
        String countryName    = (String) countryProperties.get("ADMIN");
        String countryISO    = (String) countryProperties.get("ISO_A3");
        String countryGeoType    = (String) countryGeometry.get("type");

        writeKMLFile(countryName, countryGeoType, countryPolygons);

        // afficher le contenu
        System.out.println(countryType);
        System.out.println(countryName);
    }

    private static void writeKMLFile(String countryName, String countryGeoType, JSONArray countryPolygons) {



        Element countryElem = new Element("Placemark");
        countryElem.addContent(new Element("name").setText(countryName));
        countryElem.addContent(new Element("styleUrl").setText("#lineStyle"));
//        countryElem.addContent(new Element("visibility").setText("1"));
//        countryElem.setAttribute(new Attribute("name", countryName));

        Element geometryElem = new Element("MultiGeometry");
        //dans country
        Element polygonElem = new Element("Polygon");
        Element boundElem = new Element("outerBoundaryIs");
        Element lineElem = new Element("LinearRing");
        Element cooElem = new Element("coordinates");

        //Pour les pays ayant qu'un polygon
        if(countryGeoType.equals("Polygon")) {

            //pour tout polygon
            countryPolygons.forEach(itemsPolygon -> {

                JSONArray polygon = (JSONArray)itemsPolygon;

                //pour toute coordonnée dans le polygon
                polygon.forEach(coordinates -> {

                    double lon = (double)((JSONArray)coordinates).get(0);
                    double lat = (double) ((JSONArray)coordinates).get(1);

                    cooElem.addContent(lon + "," + lat + ",0 ");

                    System.out.println("Longitude: " + lon);
                    System.out.println("Latitude: " + lat);
                });

            });
        }

        lineElem.addContent(cooElem);
        boundElem.addContent(lineElem);
        polygonElem.addContent(new Element("tessellate").setText("1"));
        polygonElem.addContent(boundElem);
//        geometryElem.addContent(polygonElem);
        countryElem.addContent(polygonElem);

        docElem.addContent(countryElem);

    }
}
