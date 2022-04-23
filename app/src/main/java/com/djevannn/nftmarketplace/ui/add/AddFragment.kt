package com.djevannn.nftmarketplace.ui.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.djevannn.nftmarketplace.MainActivity
import com.djevannn.nftmarketplace.R
import com.djevannn.nftmarketplace.ViewModelFactory
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.databinding.FragmentAddBinding
import com.djevannn.nftmarketplace.helper.ResponseCallback
import com.djevannn.nftmarketplace.helper.UserPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private lateinit var dashboardViewModel: AddViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(
                    UserPreference.getInstance(
                        requireContext().dataStore
                    )
                )
            )[AddViewModel::class.java]

        _binding = FragmentAddBinding.inflate(
            inflater,
            container,
            false
        )

        dashboardViewModel.getUser().observe(viewLifecycleOwner) {
            this.user = it
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            createNft.setOnClickListener {
                postNFT()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun postNFT() {
        var isFounds : Boolean

        val db = FirebaseDatabase.getInstance().getReference("assets")
        db.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var rand : Int
                do {
                    rand = (111..9999).random()
                    isFounds = false
                    if(snapshot.hasChild(rand.toString())){
                        isFounds = true
                    }
                } while (isFounds)
                if (!isFounds) {
                    dashboardViewModel.saveNFT(
                        binding.etNftTitle.editText?.text.toString(),
                        binding.etUrlPhoto.editText?.text.toString(),
                        binding.etNftPrice.editText?.text.toString().toDouble(),
                        user,
                        rand,
                        object : ResponseCallback {
                            override fun getCallback(
                                msg: String,
                                status: Boolean
                            ) {
                                if (status) {
                                    Toast.makeText(
                                        activity,
                                        getString(R.string.nft_successful_created),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val i = Intent(activity, MainActivity::class.java)
                                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(i)

                                } else {
                                    Toast.makeText(
                                        activity,
                                        getString(R.string.nft_failed_created),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Get Data bawah", error.toString())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}