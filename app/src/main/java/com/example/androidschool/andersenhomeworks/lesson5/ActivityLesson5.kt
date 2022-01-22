package com.example.androidschool.andersenhomeworks.lesson5

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.ActivityLesson5Binding
import com.example.androidschool.andersenhomeworks.databinding.ActivityLesson5LandBinding
import com.example.androidschool.andersenhomeworks.lesson5.ContactDetailsFragment.Companion.CONTACT_DETAILS_FRAGMENT_TAG
import com.example.androidschool.andersenhomeworks.lesson5.ContactsListFragment.Companion.CONTACTS_LIST_FRAGMENT_TAG
import com.github.javafaker.Faker

class ActivityLesson5: AppCompatActivity(), FragmentListener {

    companion object {

        const val CONTACTS_LIST = "CONTACTS_LIST"
    }

    private val isTablet: Boolean by lazy { this.resources.getBoolean(R.bool.isTablet) }
    private var _tabletBinding: ActivityLesson5LandBinding? = null
    private val tabletBinding get() = _tabletBinding!!

    private var _phoneBinding: ActivityLesson5Binding? = null
    private val phoneBinding get() = _phoneBinding!!

    private val manager: FragmentManager by lazy { supportFragmentManager }

    private val faker = Faker()
    private var contactList = ArrayList<Contact>()
    private var currentContactId = 0

    override fun itemClicked(id: Int) {
        if (id != currentContactId) {
            currentContactId = id
            replaceDetailFragment(contactList[currentContactId])
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactList = populateContacts(3)

        when {
            isTablet -> {
                initTablet()
            }
            else -> {
                initPhone()
            }
        }
    }

    private fun populateContacts(contactsCount: Int): ArrayList<Contact> {
        val contactList = ArrayList<Contact>()
        for (i in 0 until contactsCount) {
            val contact = Contact(
                id = i,
                firstName = faker.name().firstName(),
                secondName = faker.name().lastName(),
                phoneNumber = faker.phoneNumber().phoneNumber()
            )
            contactList.add(contact)
        }
        return contactList
    }

    private fun initTablet() {
        Log.e("initTablet", "invoked")
        _tabletBinding = ActivityLesson5LandBinding.inflate(layoutInflater)
        setContentView(tabletBinding.root)
        setTabletFragments()
    }

    private fun setTabletFragments() {
        addListFragment(contactList)
        addDetailFragment(contactList[currentContactId])
    }

    private fun replaceListFragment(contactList: ArrayList<Contact>) {
        removeListFragment()
        addListFragment(contactList)
    }

    private fun removeListFragment() {
        manager.findFragmentByTag(CONTACTS_LIST_FRAGMENT_TAG)?.let { existFragment ->
            manager.beginTransaction()
                .remove(existFragment)
                .commit()
        }
    }

    private fun addListFragment(contactList: ArrayList<Contact>) {
        ContactsListFragment.newInstance(contactList).apply {
            this.addFragmentListener(this@ActivityLesson5)
            manager.beginTransaction()
                .add(
                    R.id.list_fragment,
                    this,
                    CONTACTS_LIST_FRAGMENT_TAG)
                .commit()
        }
    }

    private fun replaceDetailFragment(contact: Contact) {
        removeDetailFragment()
        addDetailFragment(contact)
    }

    private fun removeDetailFragment() {
        manager.findFragmentByTag(CONTACT_DETAILS_FRAGMENT_TAG)?.let { existFragment ->
            manager.beginTransaction()
                .remove(existFragment)
                .commit()
        }
    }

    private fun addDetailFragment(contact: Contact) {
        ContactDetailsFragment.newInstance(contact).apply {
            manager.beginTransaction()
                .add(
                    R.id.details_fragment,
                    this,
                    CONTACT_DETAILS_FRAGMENT_TAG)
                .commit()
        }
    }

    private fun initPhone() {
        Log.e("initPhone", "invoked")
        _phoneBinding = ActivityLesson5Binding.inflate(layoutInflater)
        setContentView(phoneBinding.root)
        setPhoneFragments()
    }

    private fun setPhoneFragments() {

    }
}