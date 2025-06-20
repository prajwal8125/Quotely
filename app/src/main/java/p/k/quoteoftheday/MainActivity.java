package p.k.quoteoftheday;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView quoteTextView;
    private Button refreshButton, shareButton, viewFavoritesButton, favoriteButton;
    private SharedPreferences sharedPreferences;
    private Set<String> favoriteQuotesSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        quoteTextView = findViewById(R.id.quoteTextView);
        refreshButton = findViewById(R.id.refreshButton);
        shareButton = findViewById(R.id.shareButton);
        viewFavoritesButton = findViewById(R.id.viewFavoritesButton);
        favoriteButton = findViewById(R.id.favoriteButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("FavoriteQuotes", Context.MODE_PRIVATE);
        favoriteQuotesSet = new HashSet<>(sharedPreferences.getStringSet("favorites", new HashSet<>()));

//        isInternetAvailable();
        // Fetch and display a random quote
        fetchRandomQuote();

        // Set click listeners
        refreshButton.setOnClickListener(v -> fetchRandomQuote());
        shareButton.setOnClickListener(v -> shareQuote());
        viewFavoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
        favoriteButton.setOnClickListener(v -> saveFavoriteQuote());
    }

    // Method to fetch a random quote from the API
    private void fetchRandomQuote() {
        String url = "https://zenquotes.io/api/random";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject quoteObject = response.getJSONObject(0); // ZenQuotes API returns an array
                        String quote = quoteObject.getString("q");
                        String author = quoteObject.getString("a");
                        quoteTextView.setText(quote + " – " + author);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "JSON Parsing Error!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed to fetch quote! Check Internet", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(jsonArrayRequest);
    }


    // Method to share the current quote
    private void shareQuote() {
        String quote = quoteTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, quote);
        startActivity(Intent.createChooser(intent, "Share Quote"));
    }

    // Method to save the current quote as a favorite
    private void saveFavoriteQuote() {
        String currentQuote = quoteTextView.getText().toString();
        favoriteQuotesSet.add(currentQuote);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("favorites", favoriteQuotesSet);
        editor.apply();
        Toast.makeText(MainActivity.this, "Quote added to favorites!", Toast.LENGTH_SHORT).show();
    }
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

}
