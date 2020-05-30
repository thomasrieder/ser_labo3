# SER : Labo 3, GEOJSON - KML

#### Da Silva Michaël, Rieder Thomas



## Classe java

Pour ce laboratoire, nous avons pensé à créer les deux Classes Java suivantes.

### GeoJsonReader

Cette classe nous permet de lire un fichier en format `geojson` et ainsi nous retourner un tableau de type `JSONArray` du niveau le plus haut nous intéressant.

Elle possède les fonctions suivantes :

- `parseGeoJson()` : prend en paramètre un fichier `geojson` et retourne la listes des pays avec leur coordonnées dans un `JSONArray`

### KMLWriter

Cette classe nous permet de lire plus en profondeur les données contenu dans un `JSONArray ` venant d'un fichier en format `geojson` puis dans les traiter pour finalement les écrire dans un fichier au format `kml`. 

Elle possède les fonctions suivantes :

- `generateKML()` : point d'entrée permettant la création d'un fichier KML, elle prend en paramètre un `JSONArray` contenant la liste des pays et un `String` correspondant au chemin vers le fichier à écrire. Cette fonction appelles les autres fonction permettant de traiter ces données puis fini par écrire le fichier KML.
- `parseCountryObject()` : Est appelée pour chaque pays se trouvant dans le `JSONArray` de la fonction `generateKML()` pour traiter les données correspondants au pays.
- `transformPolygon()` : Est appelée pour chaque Polygon de chacun des pays pour ainsi transformer les données lues en données au format `kml`.
- `writeKMLFile()` : Cette fonction va assembler toutes les données ayant été traitées par les autres fonction afin de créer un fichier valide au format `kml`.



## Difficultés rencontrées

Dans ce laboratoire, la plus grande difficulté était de comprendre le format `kml`afin de créer un fichier valide et compréhensible par Google Earth. Nous y sommes parvenu en lisant la documentation **KML Reference** fournit dans la donnée du laboratoire.
https://developers.google.com/kml/documentation/kmlreference

## Problèmes connus

Nous somme parvenu à rendre un programme sans aucuns problème connus restant !

## Parsing GeoJson

Voici une partie des logs que nous pouvons voir lors du parsing du fichier `geojson` et la création du fichier `kml` par notre programme Java :

![](images\proof_parsing_compilateur.png)


## Test dans Google Earth

Voici une démonstration après l'import de notre fichier dans Google Earth Pro avec la sélection demandée.

![](images\proof_google_earth.png)

## Apprentissage

Dans ce laboratoire, nous avons dû prendre connaissance de deux nouveaux format :

- GeoJson
- KML

## Conclusion

Le fait de réaliser un projet dont nous pouvons voir un résultat concret, nous a fait prendre conscience à quel point ces types de fichiers peuvent être utiles lors de la manipulation de données en grandes quantités.

Nous avons réussi à nous partager le travail équitablement ce qui à fortement contribuer à la réalisation de ce laboratoire sans rencontrer de problèmes en restant très efficaces.

