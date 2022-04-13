package com.djevannn.nftmarketplace.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.djevannn.nftmarketplace.R

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(
    itemView
) {
    var ivNft: ImageView = itemView.findViewById(R.id.iv_nft)
    var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
    var tvPrice: TextView = itemView.findViewById(R.id.tv_price)
}