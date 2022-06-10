package com.ikhokha.techcheck.data.response

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

/**
 * Created by Bennette Molepo on 04/06/2022.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
class ConfirmOrderResponse {
    var id:Long = 0

    lateinit var itemQuantity:String
    lateinit var itemDescription:String
    lateinit var itemPrice:String

}