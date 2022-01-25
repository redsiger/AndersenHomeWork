package com.example.androidschool.andersenhomeworks.lesson6.fragments

import android.util.Log
import com.example.androidschool.andersenhomeworks.lesson6.Contact
import com.github.javafaker.Faker

interface ContactsDataSource {

    fun getDefaultId(): Int
    fun getContacts(): List<Contact>
    fun getContact(id: Int): Contact
    fun deleteContact(id: Int)
    fun editContact(contact: Contact)

    class Local(): ContactsDataSource {

        companion object {
            private const val DEFAULT_LIST_SIZE = 100
        }

        private val faker = Faker()

        private var _contactsList = populateContacts(DEFAULT_LIST_SIZE)
        private val contactList: List<Contact> get() = _contactsList.map { it }

        override fun getDefaultId(): Int = contactList.first().id

        override fun getContacts() = contactList

        override fun getContact(id: Int): Contact = contactList.first { it.id == id }

        override fun deleteContact(id: Int) {
            _contactsList = _contactsList.filterNot { it.id == id }
            Log.e("DATASOURCE", "deleted contact with id: $id")
        }

        override fun editContact(contact: Contact) {
            _contactsList = _contactsList.map { if (it.id == contact.id) contact else it }
        }

        private fun populateContacts(contactsCount: Int): List<Contact> {
            return buildList {
                for (i in 0 until contactsCount)
                    add(Contact(
                            id = i,
                            firstName = faker.name().firstName(),
                            lastName = faker.name().lastName(),
                            phoneNumber = faker.phoneNumber().phoneNumber(),
                            imgUrl = "https://picsum.photos/200?temp=" + faker.name()
                    ))
            }
        }
    }
}