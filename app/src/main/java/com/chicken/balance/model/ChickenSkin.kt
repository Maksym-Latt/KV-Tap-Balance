package com.chicken.balance.model

import androidx.annotation.DrawableRes
import com.chicken.balance.R

enum class ChickenSkin(
    val id: String,
    val title: String,
    @DrawableRes val calmRes: Int,
    @DrawableRes val fallRes: Int,
    val price: Int
) {
    WhiteHen(
        id = "WHITE",
        title = "White Hen",
        calmRes = R.drawable.chicken_1_calm,
        fallRes = R.drawable.chicken_1_fall,
        price = 0
    ),
    BrownHen(
        id = "BROWN",
        title = "Brown Hen",
        calmRes = R.drawable.chicken_2_calm,
        fallRes = R.drawable.chicken_2_fall,
        price = 450
    ),
    PixelHen(
        id = "PIXEL",
        title = "Pixel Hen",
        calmRes = R.drawable.chicken_3_calm,
        fallRes = R.drawable.chicken_3_fall,
        price = 1500
    ),
    PartyHatHen(
        id = "PARTY",
        title = "Ghost Chick",
        calmRes = R.drawable.chicken_4_calm,
        fallRes = R.drawable.chicken_4_fall,
        price = 2200
    );

    companion object {
        val purchasable = listOf(BrownHen, PixelHen, PartyHatHen)
    }
}
