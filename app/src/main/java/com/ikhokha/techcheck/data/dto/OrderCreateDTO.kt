package com.example.arijenguserapp.data.dto

import com.example.arijenguserapp.data.model.Orders
import com.example.arijenguserapp.data.model.Person
import lombok.*
import java.io.Serializable

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
class OrderCreateDTO:Serializable {
     var person: Person? =null
     var customerOid: String? =null
     var orders: List<Orders>? =null

}