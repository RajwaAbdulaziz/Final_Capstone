package com.tuwaiq.finalcapstone.presentation.memeApiFragment

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.data.remote.memeApi.models.Meme
import com.tuwaiq.finalcapstone.databinding.MemeApiFragmentBinding
import com.tuwaiq.finalcapstone.databinding.MemeApiListItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val TAG = "MemeApiFragment"
var CHOSE_MEME = false
@AndroidEntryPoint
class MemeApiFragment : Fragment() {

    private val memeApiViewModel by viewModels<MemeApiViewModel>()

    private lateinit var binding: MemeApiFragmentBinding
    private lateinit var memeRv: RecyclerView
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.meme_api_fragment, container, false)
        binding = MemeApiFragmentBinding.inflate(inflater, container, false)
        memeRv = view.findViewById(R.id.memes_rv)
        binding.memesRv.layoutManager = GridLayoutManager(context, 2)


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = activity?.getSharedPreferences("switch", Context.MODE_PRIVATE)!!
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            updateUI()
        }
    }

    private suspend fun updateUI() {
        memeApiViewModel.getMemes().onEach {
            Log.e(TAG, "updateUI: $it")
            memeRv.adapter = MemeAdapter(it)
        }.launchIn(lifecycleScope)
    }

    inner class MemeViewHolder(private val binding: MemeApiListItemBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var meme: Meme

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(meme: Meme) {
            this.meme = meme
            binding.memeIv.load(meme.url)
        }

        override fun onClick(p0: View?) {
            when(p0) {
                itemView -> {
                    CHOSE_MEME = true
                    Log.d(TAG, "meme: ${meme.url}")
                    val action = MemeApiFragmentDirections.actionMemeApiFragmentToMoodDetailsFragment("", "", "", "", meme.url)
                    findNavController().navigate(action)
                }
            }
        }
    }

    inner class MemeAdapter(private val memes: List<Meme>): RecyclerView.Adapter<MemeViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
            val binding = MemeApiListItemBinding.inflate(layoutInflater, parent, false)
            return MemeViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
            val meme = memes[position]
            holder.bind(meme)
        }

        override fun getItemCount(): Int = memes.size

    }


}