package com.ikhokha.techcheck.data.dto


import com.ikhokha.techcheck.data.model.ShopItem
import lombok.*
import java.io.Serializable

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
class OrderCreateDTO:Serializable {
     var orders: List<ShopItem>? =null

}