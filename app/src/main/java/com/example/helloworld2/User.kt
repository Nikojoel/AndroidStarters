package com.example.helloworld2

import androidx.room.*

@Entity
data class User(@PrimaryKey(autoGenerate = true) val uid: Long, val firstname: String, val lastname: String) {

    override fun toString(): String {
        return "($uid) $firstname $lastname"
    }
}

@Entity(foreignKeys = [ForeignKey(
    entity = User::class,
    parentColumns = ["uid"],
    childColumns = ["user"])])
data class ContactInfo(val user: Long, val type: String, @PrimaryKey val value: String) {
    override fun toString() = "$type - $value"
}

class UserContact {
    @Embedded
    var user: User? = null
    @Relation(parentColumn = "uid", entityColumn = "user")
    var contacts: List<ContactInfo>? = null
}