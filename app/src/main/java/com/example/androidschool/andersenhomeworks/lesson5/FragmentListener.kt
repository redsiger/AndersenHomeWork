package com.example.androidschool.andersenhomeworks.lesson5

interface FragmentListener {

    fun onItemClick(id: Int)

    fun onItemEdit(id: Int)

    fun onItemSave(contact: Contact)

    fun onItemCancel()

    fun getCurrentId(): Int

    fun getContact(id: Int): Contact

    fun getContacts(): List<Contact>

    fun addRepositoryListener(listener: RepositoryListener)

    fun removeRepositoryListener(listener: RepositoryListener)
}