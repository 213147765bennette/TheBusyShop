package com.ikhokha.techcheck.data.model

import lombok.*

/**
 * Created by Bennette Molepo on 05/06/2022.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
data class ShopItem(
    var description: String? =null,
    var image: String? =null,
    var price: Double = 0.0,
)
