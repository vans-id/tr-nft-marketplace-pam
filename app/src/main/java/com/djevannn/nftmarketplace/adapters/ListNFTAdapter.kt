package com.djevannn.nftmarketplace.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djevannn.nftmarketplace.R
import com.djevannn.nftmarketplace.data.NFT

class ListNFTAdapter :
    RecyclerView.Adapter<ListNFTAdapter.ListViewHolder>() {

    private var listNft = ArrayList<NFT>()

    fun setNFTs(data: List<NFT>?) {
        if (data == null) return
        this.listNft.clear()
        this.listNft.addAll(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_nft, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        val (
            title,
            imageUrl,
            currentPrice,
        ) = listNft[position]

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.ivNft)
        holder.tvTitle.text = title
        holder.tvPrice.text = "$currentPrice ETH"
    }

    override fun getItemCount(): Int = listNft.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
        var ivNft: ImageView = itemView.findViewById(R.id.iv_nft)
        var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        var tvPrice: TextView = itemView.findViewById(R.id.tv_price)
    }
}