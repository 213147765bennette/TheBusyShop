package com.ikhokha.techcheck.util

/**
 * Created by Bennette Molepo on 11/06/2022.
 */
object CalculateTotal {

    fun getTotalPrice(itemPrice: Double, quantity:Int): String {
        val price = itemPrice * quantity
        return "R"+price.toString()+"0"
    }
}