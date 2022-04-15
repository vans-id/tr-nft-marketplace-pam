package com.djevannn.nftmarketplace.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djevannn.nftmarketplace.R
import com.djevannn.nftmarketplace.data.NFT

class ListNFTAdapter :
    RecyclerView.Adapter<ListViewHolder>() {

    private var listNft = ArrayList<NFT>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


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
            _,
            image_url,
            current_price,
            _,
            token_id
        ) = listNft[position]

        Glide.with(holder.itemView.context)
            .load(image_url)
            .into(holder.ivNft)
        holder.tvTitle.text = "$title$token_id"
        holder.tvPrice.text = "$current_price ETH"

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(
                listNft[holder.adapterPosition]
            )
        }
    }

    override fun getItemCount(): Int = listNft.size


}