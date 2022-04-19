package com.djevannn.nftmarketplace.adapters.listing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.djevannn.nftmarketplace.R
import com.djevannn.nftmarketplace.data.Listing

class ListingAdapter : RecyclerView.Adapter<ListingViewHolder>() {

    private var listings = ArrayList<Listing>()

    fun setNFTs(data: List<Listing>?) {
        if (data == null) return
        this.listings.clear()
        this.listings.addAll(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListingViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_listing, parent, false)
        return ListingViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListingViewHolder,
        position: Int
    ) {
        val (
            new_owner,
            _,
            prev_owner,
            purchase_price,
            time
        ) = listings[position]

        holder.tvListingEth.text = purchase_price.toString()
        holder.tvListingFrom.text = prev_owner
        holder.tvListingTo.text = new_owner
        holder.tvListingDate.text = time
    }

    override fun getItemCount(): Int = listings.size
}