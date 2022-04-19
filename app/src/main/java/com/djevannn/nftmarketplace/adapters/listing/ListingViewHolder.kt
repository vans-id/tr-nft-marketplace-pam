package com.djevannn.nftmarketplace.adapters.listing

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.djevannn.nftmarketplace.R

class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(
    itemView
) {
    var tvListingEth: TextView =
        itemView.findViewById(R.id.tv_listing_eth)
    var tvListingFrom: TextView =
        itemView.findViewById(R.id.tv_listing_from)
    var tvListingTo: TextView =
        itemView.findViewById(R.id.tv_listing_to)
    var tvListingDate: TextView =
        itemView.findViewById(R.id.tv_listing_date)
}