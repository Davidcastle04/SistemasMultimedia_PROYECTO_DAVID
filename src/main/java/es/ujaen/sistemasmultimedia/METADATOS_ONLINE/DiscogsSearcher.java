package es.ujaen.sistemasmultimedia.METADATOS_ONLINE;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiscogsSearcher {
    private static final String USER_AGENT = "MultiStudio/1.0";
    private static final String API_TOKEN = "JTInNiBLDLHIKQDyRQZDdlcRMkokKiAstjKyVvlI"; // <- tu token aquí

    public static JSONObject buscarEnDiscogs(String releaseTitle, String artistName) throws Exception {
        if (releaseTitle == null || releaseTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }

        String encodedTitle = URLEncoder.encode(releaseTitle, StandardCharsets.UTF_8.toString());
        String queryParams;

        if (artistName != null && !artistName.trim().isEmpty()) {
            String encodedArtist = URLEncoder.encode(artistName, StandardCharsets.UTF_8.toString());
            queryParams = "q=" + encodedTitle + "+" + encodedArtist;
        } else {
            queryParams = "q=" + encodedTitle;
        }

        String apiUrl = "https://api.discogs.com/database/search?" + queryParams + "&type=release&per_page=1&token=" + API_TOKEN;
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray results = jsonResponse.optJSONArray("results");

        if (results != null && results.length() > 0) {
            JSONObject release = results.getJSONObject(0);
            JSONObject datos = new JSONObject();

            // Obtener artista y título desde "title"
            String fullTitle = release.optString("title", releaseTitle);
            String artista = artistName;
            String titulo = releaseTitle;

            if (fullTitle.contains(" - ")) {
                String[] partes = fullTitle.split(" - ", 2);
                artista = partes[0].trim();
                titulo = partes[1].trim();
            }

            datos.put("titulo", titulo);
            datos.put("artista", artista);
            datos.put("album", fullTitle);

            // Concatenar géneros y estilos
            List<String> generos = new ArrayList<>();
            JSONArray genreArray = release.optJSONArray("genre");
            JSONArray styleArray = release.optJSONArray("style");

            if (genreArray != null) {
                for (int i = 0; i < genreArray.length(); i++) {
                    generos.add(genreArray.optString(i));
                }
            }
            if (styleArray != null) {
                for (int i = 0; i < styleArray.length(); i++) {
                    generos.add(styleArray.optString(i));
                }
            }

            datos.put("genero", generos.isEmpty() ? "N/A" : String.join(", ", generos));
            datos.put("pista", "1");
            datos.put("caratula", release.optString("cover_image", ""));

            return datos;
        } else {
            System.out.println("No se encontraron resultados para: " + releaseTitle + " by " + artistName);
            return null;
        }
    }
}
