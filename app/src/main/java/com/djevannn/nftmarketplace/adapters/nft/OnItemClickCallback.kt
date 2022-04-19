package com.djevannn.nftmarketplace.adapters.nft

import com.djevannn.nftmarketplace.data.NFT

interface OnItemClickCallback {
    fun onItemClicked(data: NFT)
}