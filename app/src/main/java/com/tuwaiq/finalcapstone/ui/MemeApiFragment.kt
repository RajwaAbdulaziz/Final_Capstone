package com.tuwaiq.finalcapstone.ui

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tuwaiq.finalcapstone.R
import com.tuwaiq.finalcapstone.memeApi.models.Meme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val TAG = "MemeApiFragment"
class MemeApiFragment : Fragment() {

    private val memeApiViewModel by lazy { ViewModelProvider(this).get(MemeApiViewModel::class.java)}

    private lateinit var memeRv: RecyclerView
    //private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.meme_api_fragment, container, false)
        memeRv = view.findViewById(R.id.memes_rv)
        memeRv.layoutManager = GridLayoutManager(context, 2)


        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // sharedPref = activity?.getSharedPreferences("switch", Context.MODE_PRIVATE)!!
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

    inner class MemeViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var meme: Meme
        private val memeIv: ImageView = itemView.findViewById(R.id.meme_iv)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(meme: Meme) {
            this.meme = meme
            memeIv.load(meme.url)
        }

        override fun onClick(p0: View?) {
            when(p0) {
                itemView -> {
                    val action = MemeApiFragmentDirections.actionMemeApiFragmentToMoodDetailsFragment(meme.url)
                    findNavController().navigate(action)
                }
            }
        }
    }

    inner class MemeAdapter(private val memes: List<Meme>): RecyclerView.Adapter<MemeViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
            val view = layoutInflater.inflate(R.layout.meme_api_list_item, parent, false)
            return MemeViewHolder(view)
        }

        override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
            val meme = memes[position]
            holder.bind(meme)
        }

        override fun getItemCount(): Int = memes.size

    }


}