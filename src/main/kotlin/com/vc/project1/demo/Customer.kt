package com.vc.project1.demo;

import javax.persistence.Entity
import javax.persistence.Id


@Entity
data class Customer(@Id val id: String = "", val name: String = "", val lastName: String = "", val age: Int = 0){
    override fun toString(): String{
        return "| The customer data is: id=$id, firstName=$name, lastName=$lastName. "
    }
}
