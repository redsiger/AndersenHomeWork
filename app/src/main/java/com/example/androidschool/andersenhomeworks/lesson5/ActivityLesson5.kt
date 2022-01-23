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
        private const val ACTIVITY_LESSON5_TAG = "ACTIVITY_LESSON5_TAG"
        private const val CONTACT_LIST = "CONTACT_LIST"
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
        Log.e("LISTENER", "click on $id")
        when {
            isTablet -> {
                if (id != currentContactId) {
                    currentContactId = id
                    replaceDetailFragment(contactList[currentContactId])
                }
            }
            else -> {
                addDetailFragment(contactList[id], R.id.fragment_container)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when {
            isTablet -> initTabletView()
            else -> initPhoneView()
        }

        savedInstanceState?.let {
            contactList = it.getParcelableArrayList(CONTACT_LIST) ?: ArrayList()
        } ?: run {
            contactList = populateContacts(3)

            when {
                isTablet -> setTabletFragments()
                else -> setPhoneFragments()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(CONTACT_LIST, contactList)
        super.onSaveInstanceState(outState)
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

    private fun initTabletView() {
        _tabletBinding = ActivityLesson5LandBinding.inflate(layoutInflater)
        setContentView(tabletBinding.root)
    }

    private fun setTabletFragments() {
        replaceListFragment(contactList, R.id.list_fragment)
        addDetailFragment(contactList[currentContactId], R.id.details_fragment)
    }

    private fun initPhoneView() {
        _phoneBinding = ActivityLesson5Binding.inflate(layoutInflater)
        setContentView(phoneBinding.root)
    }

    private fun setPhoneFragments() {
        replaceListFragment(contactList, R.id.fragment_container)
    }

    private fun replaceListFragment(contactList: ArrayList<Contact>, containerViewId: Int) {
        removeListFragment()
        addListFragment(contactList, containerViewId)
    }

    private fun removeListFragment() {
        manager.findFragmentByTag(CONTACTS_LIST_FRAGMENT_TAG)?.let { existFragment ->
            manager.beginTransaction()
                .remove(existFragment)
                .commit()
        }
    }

    private fun addListFragment(contactList: ArrayList<Contact>, containerViewId: Int) {
        ContactsListFragment.newInstance(contactList).apply {
//            this.addFragmentListener(this@ActivityLesson5)
            manager.beginTransaction()
                .replace(
                    containerViewId,
                    this,
                    CONTACTS_LIST_FRAGMENT_TAG)
                .commit()
        }
    }

    private fun replaceDetailFragment(contact: Contact) {
        removeDetailFragment()
        addDetailFragment(contact, R.id.details_fragment)
    }

    private fun removeDetailFragment() {
        manager.findFragmentByTag(CONTACT_DETAILS_FRAGMENT_TAG)?.let { existFragment ->
            manager.beginTransaction()
                .remove(existFragment)
                .commit()
        }
    }

    private fun addDetailFragment(contact: Contact, containerViewId: Int) {
        ContactDetailsFragment.newInstance(contact).apply {
            manager.beginTransaction().apply {
                if (!isTablet) this.addToBackStack(null)
            }
                .replace(
                    containerViewId,
                    this,
                    CONTACT_DETAILS_FRAGMENT_TAG)
                .commit()
        }
    }
}