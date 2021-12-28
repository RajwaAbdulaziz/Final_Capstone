package com.tuwaiq.finalcapstone.memeApi.models

import com.google.gson.annotations.SerializedName

class MemeResponse {
    //@SerializedName("memes")
     var memes: List<Meme> = emptyList()
}