package com.djevannn.nftmarketplace.adapters

import com.djevannn.nftmarketplace.data.NFT

interface OnItemClickCallback {
    fun onItemClicked(data: NFT)
}