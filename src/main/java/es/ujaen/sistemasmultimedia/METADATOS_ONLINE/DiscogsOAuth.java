package es.ujaen.sistemasmultimedia.METADATOS_ONLINE;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscogsOAuth {

    private static final String CONSUMER_KEY = "NchAuptUSOVUhvjIpEOb"; // Tu Clave del cliente
    private static final String CONSUMER_SECRET = "vvkCHANZjJXOqNXNhjeBQTCVWLMxaCEt"; // Tu Información secreta del cliente
    private static final String REQUEST_TOKEN_URL = "https://api.discogs.com/oauth/request_token";
    private static final String AUTHORIZE_URL = "https://www.discogs.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://api.discogs.com/oauth/access_token";
    private static final String CALLBACK_URL = "oob"; // Para obtener el PIN

    private OAuthConsumer consumer;
    private OAuthProvider provider;

    public DiscogsOAuth() {
        consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        provider = new CommonsHttpOAuthProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL);
    }

    public Map<String, String> initiateFlow() throws Exception {
        // 1. Get the request token
        String authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);

        // 2. Open the authorization URL in the browser
        System.out.println("Abre esta URL en tu navegador para autorizar la aplicación:\n" + authUrl);
        try {
            Desktop.getDesktop().browse(new URI(authUrl));
        } catch (IOException e) {
            System.err.println("No se pudo abrir el navegador. Por favor, abre la URL manualmente.");
        }

        // 3. Ask the user for the verifier PIN
        System.out.print("Introduce el PIN que Discogs te proporciona: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String oauthVerifier = br.readLine();

        // 4. Get the access token
        provider.retrieveAccessToken(consumer, oauthVerifier);

        // Store and return the access token and secret
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", consumer.getToken());
        tokens.put("accessTokenSecret", consumer.getTokenSecret());

        return tokens;
    }

    public static void main(String[] args) {
        DiscogsOAuth oauth = new DiscogsOAuth();
        try {
            Map<String, String> tokens = oauth.initiateFlow();
            String accessToken = tokens.get("accessToken");
            String accessTokenSecret = tokens.get("accessTokenSecret");

            System.out.println("accessToken: " + accessToken);
            System.out.println("accessTokenSecret: " + accessTokenSecret);

            // Now you can use these tokens in your DiscogsSearcher class
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
