import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class KMLWriter {

    private String finalKMLPath;

    private static final Namespace ns = Namespace.getNamespace("http://www.opengis.net/kml/2.2");
    private static final Element root = new Element("kml", ns);
    private static final Element docElem = new Element("Document", ns);
    private static final Document document = new Document(root);

    public void generateKML(JSONArray countriesArray, String kmlDestination) {

        finalKMLPath = kmlDestination;

        root.addContent(docElem);

        // permet à l'ouverture du fichier KML de voir tous les pays disponibles sur Google Earth
        Element opeN = new Element("open", ns).setText("1");

        // permet de modifier le style des différents "composants" disponibles en KML (ici Polygon)
        Element styleElem = new Element("Style", ns);
        styleElem.setAttribute(new Attribute("id", "borders"));

        // permet de définir la couleur et la largeur de la ligne extérieur des Polygon
        Element styleLine = new Element("LineStyle", ns);
        styleLine.addContent(new Element("color", ns).setText("ffffffff"));
        styleLine.addContent(new Element("width", ns).setText("2"));

        // permet de modifier la couleur du Polygon et définir s'il détient une bordure extérieure
        // ici, les Polygon sont transparent pour ne laisser que les bordures extérieures visibles
        Element stylePoly = new Element("PolyStyle", ns);
        stylePoly.addContent(new Element("fill", ns).setText("0"));
        stylePoly.addContent(new Element("outline", ns).setText("1"));

        styleElem.addContent(styleLine);
        styleElem.addContent(stylePoly);
        docElem.addContent(opeN);
        docElem.addContent(styleElem);


        countriesArray.forEach(country -> parseCountryObject((JSONObject)country));

        XMLOutputter xmlOutputer = new XMLOutputter();
        xmlOutputer.setFormat(Format.getPrettyFormat());

        try {

            xmlOutputer.output(document, new FileWriter(finalKMLPath));
            System.out.println("XML File was created successfully!");

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    private void parseCountryObject(JSONObject country) {
        //récupère les propriétés du pays
        JSONObject countryProperties = (JSONObject) country.get("properties");

        //récupère la géometrie du pays (Polygon ou MultiPolygon, ainsi que les coordonnées)
        JSONObject countryGeometry = (JSONObject) country.get("geometry");

        JSONArray countryPolygons = (JSONArray) countryGeometry.get("coordinates");

        // obtenir les détails ...
        String countryName    = (String) countryProperties.get("ADMIN");
        String countryISO    = (String) countryProperties.get("ISO_A3");
        String countryGeoType    = (String) countryGeometry.get("type");

        writeKMLFile(countryISO, countryName, countryGeoType, countryPolygons);
    }

    private void writeKMLFile(String countryISO, String countryName, String countryGeoType, JSONArray countryPolygons) {
        Element placemark = new Element("Placemark", ns);

        // nom affiché dans Google Earth pour chaque pays
        placemark.addContent(new Element("name", ns).setText(countryName));

        // nom du style à appliquer pour chaque Polygon/MultiPolygon
        placemark.addContent(new Element("styleUrl", ns).setText("#borders"));

        // dans le cas où un pays détient plusieurs frontières (îles par exemple -> MultiPolygon)
        Element geometryElem = new Element("MultiGeometry", ns);

        System.out.println("(" + countryISO + ") " + countryName);

        if(countryGeoType.equals("Polygon"))
            placemark.addContent(transformPolygon(countryPolygons));
        else {
            //pour tout polygon
            countryPolygons.forEach(itemsPolygon -> {
                geometryElem.addContent(transformPolygon((JSONArray) itemsPolygon));
            });
            placemark.addContent(geometryElem);
        }

        docElem.addContent(placemark);
        System.out.println("\n");
    }

    private Element transformPolygon(JSONArray polygon){

        Element polygonElem = new Element("Polygon", ns);

        for(int i = 0; i < polygon.size(); ++i){
            Element cooElem = new Element("coordinates", ns);
            // éléments nécessaires pour la création d'un Polygon en KML
            Element lineElem = new Element("LinearRing", ns);
            Element outerboundElem = new Element("outerBoundaryIs", ns);
            Element innerboundElem = new Element("innerBoundaryIs", ns);

            JSONArray coordinates = (JSONArray)polygon.get(i);

            //pour toute coordonnée dans le polygon
            coordinates.forEach(coordinate -> {

                double lon = (double)((JSONArray)coordinate).get(0);
                double lat = (double) ((JSONArray)coordinate).get(1);

                cooElem.addContent(lon + "," + lat + " ");
            });
            System.out.println("\t - " + coordinates.size() + " coordinates");

            lineElem.addContent(cooElem);
            if(i > 0) {
                innerboundElem.addContent(lineElem);
                polygonElem.addContent(innerboundElem);
            } else {
                outerboundElem.addContent(lineElem);
                polygonElem.addContent(outerboundElem);
            }
        }

        return polygonElem;
    }
}
