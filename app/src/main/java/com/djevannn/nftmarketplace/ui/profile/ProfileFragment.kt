package com.djevannn.nftmarketplace.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.djevannn.nftmarketplace.databinding.FragmentProfileBinding
import com.djevannn.nftmarketplace.ui.collection.CollectionActivity
import com.djevannn.nftmarketplace.ui.favorite.FavoriteActivity


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        binding.btnToCollection.setOnClickListener {
            val intent = Intent(
                context,
                CollectionActivity::class.java
            )
            startActivity(intent)
        }
        binding.btnToFavorite.setOnClickListener {
            val intent = Intent(
                context,
                FavoriteActivity::class.java
            )
            startActivity(intent)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}