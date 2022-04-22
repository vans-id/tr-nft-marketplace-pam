package com.djevannn.nftmarketplace.ui.user.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.djevannn.nftmarketplace.ViewModelFactory
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.databinding.FragmentProfileBinding
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.setting.SettingActivity
import com.djevannn.nftmarketplace.ui.user.collection.CollectionActivity
import com.djevannn.nftmarketplace.ui.user.edit_profile.EditProfileActivity
import com.djevannn.nftmarketplace.ui.user.favorite.FavoriteActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private lateinit var notificationsViewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(
                    UserPreference.getInstance(
                        requireContext().dataStore
                    )
                )
            )[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )

        notificationsViewModel.getUser().observe(viewLifecycleOwner) {
            this.user = it

            val front = it.wallet.take(6)
            val back = it.wallet.takeLast(4)
            val truncate = "$front...$back"

            binding.apply {
                tvWallet.text = it.wallet
                tvName.text = it.name
                tvWallet.text = truncate
                tvEth.text = it.balance.toString() + "ETH"
                Glide.with(root)
                    .load(it.photo_url)
                    .apply(RequestOptions().override(300, 300))
                    .into(ivAvatar)
            }
        }

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        notificationsViewModel.getLocale.observe(viewLifecycleOwner){
            binding.tvLanguage.text = it
        }

        binding.btnCollection.setOnClickListener {
            val intent = Intent(
                context,
                CollectionActivity::class.java
            )
            startActivity(intent)
        }

        binding.btnProfile.setOnClickListener {
            val intent = Intent(
                context,
                EditProfileActivity::class.java
            )
            startActivity(intent)
        }

        binding.btnFavorite.setOnClickListener {
            val intent = Intent(
                context,
                FavoriteActivity::class.java
            )
            startActivity(intent)
        }

        binding.btnSetting.setOnClickListener {
            val intent = Intent(
                context,
                SettingActivity::class.java
            )
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            notificationsViewModel.logout()
            activity?.finish()
        }

        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}