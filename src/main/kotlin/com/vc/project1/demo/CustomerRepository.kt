package com.vc.project1.demo

import org.springframework.data.repository.CrudRepository



interface CustomerRepository : CrudRepository<Customer, Long> {
    fun findByLastName(lastName: String): List<Customer>
    override fun findAll():List<Customer>
}
