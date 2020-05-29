/**
 * Auteur:
 * Da Silva Michaël, Rieder Thoms
 *
 * Description:
 * Lit un fichier de type geojson permet de
 * résupérer un JSONArray des pays se trouvant ddans ce fichier
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class GeoJsonReader {


    public JSONArray parseGeoJson(String jsonSource){

        JSONParser jsonParser = new JSONParser();
        JSONArray countriesCollection = new JSONArray();

        try (FileReader reader = new FileReader(jsonSource)) {

            // lecture du fichier
            JSONObject obj = (JSONObject)jsonParser.parse(reader);

            //récupère les pays
            countriesCollection = (JSONArray) obj.get("features");

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ParseException e) {

            e.printStackTrace();
        }

        return  countriesCollection;
    }
}
