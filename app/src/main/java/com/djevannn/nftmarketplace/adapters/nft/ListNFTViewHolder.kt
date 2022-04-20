package com.djevannn.nftmarketplace.adapters.nft

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.djevannn.nftmarketplace.R

class ListNFTViewHolder(itemView: View) : RecyclerView.ViewHolder(
    itemView
) {
    var ivNft: ImageView = itemView.findViewById(R.id.iv_nft)
    var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
    var tvPrice: TextView = itemView.findViewById(R.id.tv_price)
    var tvCreator: TextView = itemView.findViewById(R.id.tv_creator)
    var tvPriceUSD: TextView = itemView.findViewById(R.id.tv_price_usd)
}