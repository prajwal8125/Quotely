package p.k.quoteoftheday;

import android.os.Bundle;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;
    private FavoriteQuotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);

        // Apply insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve favorite quotes from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("FavoriteQuotes", MODE_PRIVATE);
        Set<String> favoriteQuotesSet = sharedPreferences.getStringSet("favorites", new HashSet<>());

        // Convert Set to List for the adapter
        ArrayList<String> favoriteQuotesList = new ArrayList<>(favoriteQuotesSet);

        // Set up the adapter
        adapter = new FavoriteQuotesAdapter(favoriteQuotesList);
        favoritesRecyclerView.setAdapter(adapter);
    }
}
