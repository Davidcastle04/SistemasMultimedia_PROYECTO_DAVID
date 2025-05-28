package es.ujaen.sistemasmultimedia.METADATOS_ONLINE;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException; // Importar para manejar excepciones de JSON

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException; // Importar para manejar excepciones de E/S
import java.net.HttpURLConnection; // No usado directamente con HttpClient, pero buena práctica si se cambia
import java.net.URL; // No usado directamente con HttpClient, pero buena práctica si se cambia
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets; // Para URLEncoder

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;

public class DiscogsSearcher {
    private static final String USER_AGENT = "MiAppJava/1.0";
    private static final String CONSUMER_KEY = "NchAuptUSOVUhvjIpEOb"; // Tu Clave del cliente
    private static final String CONSUMER_SECRET = "vvkCHANZjJXOqNXNhjeBQTCVWLMxaCEt"; // Tu Información secreta del cliente
    private static final String ACCESS_TOKEN = "lXBevKtcGrwIKYXgFlyVdfjBmooTkyJilnHEJCxK";
    private static final String ACCESS_TOKEN_SECRET = "muvGJrPSVIocltNfMkqcFHmUMsqXTcCTciUWpIIc";

    /**
     * Busca metadatos de una canción/lanzamiento en Discogs utilizando la API.
     *
     * @param titulo El título de la canción o lanzamiento a buscar.
     * @return Un JSONObject con los metadatos encontrados (titulo, artista, album, genero, pista, caratula),
     * o null si no se encuentran resultados o ocurre un error.
     * @throws Exception Si ocurre un error durante la conexión, la firma OAuth o el procesamiento de la respuesta.
     */
    public static JSONObject buscarEnDiscogs(String titulo) throws Exception {
        // Codificar el título para la URL
        String query = URLEncoder.encode(titulo, StandardCharsets.UTF_8.toString());
        String apiUrl = "https://api.discogs.com/database/search?q=" + query + "&type=release&per_page=1";

        // Configuración y firma OAuth
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        consumer.setTokenWithSecret(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);

        HttpGet request = new HttpGet(apiUrl);
        request.setHeader("User-Agent", USER_AGENT); // Es buena práctica incluir un User-Agent
        consumer.sign(request);

        // Ejecutar la petición HTTP
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        // Leer la respuesta
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close(); // Cerrar el lector

        // Procesar la respuesta JSON
        JSONObject jsonResponse = new JSONObject(result.toString());

        // Obtener el array de resultados. Usar optJSONArray para evitar NPE si "results" no existe.
        JSONArray results = jsonResponse.optJSONArray("results");

        if (results != null && results.length() > 0) {
            JSONObject release = results.optJSONObject(0); // Usar optJSONObject para evitar NPE si el primer elemento no es un objeto

            if (release != null) {
                JSONObject datos = new JSONObject();

                // Obtener título (usar optString para seguridad)
                datos.put("titulo", release.optString("title", "N/A"));

                // Obtener artista: Discogs suele devolver "artists" como un JSONArray de objetos.
                // Accedemos al primer objeto del array y luego a su campo "name".
                String artista = "N/A";
                JSONArray artistsArray = release.optJSONArray("artists");
                if (artistsArray != null && artistsArray.length() > 0) {
                    JSONObject firstArtist = artistsArray.optJSONObject(0);
                    if (firstArtist != null) {
                        artista = firstArtist.optString("name", "N/A");
                    }
                }
                datos.put("artista", artista);

                // Para el álbum, a menudo se usa el mismo título del lanzamiento si no hay un campo "album" claro en el resultado de búsqueda.
                // Si la API de Discogs tiene un campo específico para "album" en el resultado de búsqueda, úsalo.
                datos.put("album", release.optString("title", "N/A"));

                // Obtener género: Discogs suele devolver "genre" o "genres" como un JSONArray de strings.
                String genero = "N/A";
                JSONArray genreArray = release.optJSONArray("genre"); // O "genres" si es el caso
                if (genreArray != null && genreArray.length() > 0) {
                    genero = genreArray.optString(0, "N/A");
                } else {
                    // Si no se encuentra "genre", intentar con "styles" que también es un array de strings
                    JSONArray stylesArray = release.optJSONArray("styles");
                    if (stylesArray != null && stylesArray.length() > 0) {
                        genero = stylesArray.optString(0, "N/A");
                    }
                }
                datos.put("genero", genero);

                // Pista: Si la búsqueda es de un lanzamiento completo, la pista puede no ser relevante o ser "1".
                // Si la API proporciona un campo para el número de pista, úsalo.
                datos.put("pista", "1"); // Valor por defecto si no se puede obtener

                // Carátula: Discogs suele usar "cover_image" o "thumb" para la URL de la imagen.
                datos.put("caratula", release.optString("cover_image", "")); // Vacío si no hay carátula

                return datos;
            } else {
                System.out.println("El primer resultado de la búsqueda no es un objeto JSON válido.");
                return null;
            }
        } else {
            System.out.println("No se encontraron resultados para la búsqueda: " + titulo);
            return null;
        }
    }
}