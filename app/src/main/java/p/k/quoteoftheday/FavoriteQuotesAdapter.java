package p.k.quoteoftheday;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FavoriteQuotesAdapter extends RecyclerView.Adapter<FavoriteQuotesAdapter.ViewHolder> {

    private List<String> favoriteQuotesList;

    public FavoriteQuotesAdapter(List<String> favoriteQuotesList) {
        this.favoriteQuotesList = favoriteQuotesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.quoteTextView.setText(favoriteQuotesList.get(position));
    }

    @Override
    public int getItemCount() {
        return favoriteQuotesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView quoteTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quoteTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}

