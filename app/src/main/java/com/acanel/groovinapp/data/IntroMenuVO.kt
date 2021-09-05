package com.acanel.groovinapp.data

data class IntroMenuVO(
    val title: String? = "",
    val url: String? = null,
    val action: (() -> Unit)? = null,
    val spanned: Int = 1
)
