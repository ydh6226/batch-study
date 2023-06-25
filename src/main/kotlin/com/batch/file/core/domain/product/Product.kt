package com.batch.file.core.domain.product

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Product(
    val name: String,
    val price: Int,
    val type: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    override fun toString(): String {
        return "id: ${id}, name: ${name}, price: ${price} type: ${type}"
    }
}