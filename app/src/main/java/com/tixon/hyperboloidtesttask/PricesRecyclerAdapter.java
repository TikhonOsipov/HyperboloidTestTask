package com.tixon.hyperboloidtesttask;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PricesRecyclerAdapter extends RecyclerView.Adapter<PricesRecyclerAdapter.ViewHolder> {

    ArrayList<String> prices, descriptions;

    public PricesRecyclerAdapter(ArrayList<String> prices, ArrayList<String> descriptions) {
        this.descriptions = descriptions;
        this.prices = prices;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_price, viewGroup, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tv_description.setText(descriptions.get(i));
        viewHolder.tv_price.setText(prices.get(i));
    }

    @Override
    public int getItemCount() {
        return prices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tv_description;
        TextView tv_price;

        public ViewHolder(CardView itemView) {
            super(itemView);
            cardView = itemView;
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}
