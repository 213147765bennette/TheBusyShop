package com.ikhokha.techcheck.util

import android.util.Log

/**
 * Created by Bennette Molepo on 17/06/2022.
 */
object MakeShopImagePath {

    fun getImagePath(itemCode:String): String {
        var path = ""
        when(itemCode){
            "APL883" ->{
                path = "gs://the-busy-shop.appspot.com/apple.jpg"
            }

            "BAN258" ->{
                path = "gs://the-busy-shop.appspot.com/banana.jpg"
            }

            "COC378" ->{
                path = "gs://the-busy-shop.appspot.com/coconut.jpg"
            }

            "GPF208" ->{
                path = "gs://the-busy-shop.appspot.com/grapefruit.jpg"
            }

            "ORN750" ->{
                path = "gs://the-busy-shop.appspot.com/orange.jpg"
            }

            "PER478" ->{
                path = "gs://the-busy-shop.appspot.com/pear.jpg"
            }

            "SBR101" ->{
                path = "gs://the-busy-shop.appspot.com/strawberry.jpg"
            }

            "WML999" ->{
                path = "gs://the-busy-shop.appspot.com/watermelon.jpg"
            }

            else ->{
                path = "gs://the-busy-shop.appspot.com/apple.jpg"
            }

        }
        return path
    }

}