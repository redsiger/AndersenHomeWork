package com.example.androidschool.andersenhomeworks.lesson6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.ActivityLesson6Binding
import com.example.androidschool.andersenhomeworks.databinding.ActivityLesson6LandBinding
import com.example.androidschool.andersenhomeworks.lesson6.fragments.*

class ActivityLesson6: AppCompatActivity(), ContactsListener, DialogListener {

    private val isTablet: Boolean by lazy { this.resources.getBoolean(R.bool.isTablet) }
    private var _tabletBinding: ActivityLesson6LandBinding? = null
    private val tabletBinding get() = _tabletBinding!!

    private var _phoneBinding: ActivityLesson6Binding? = null
    private val phoneBinding get() = _phoneBinding!!

    private val manager: FragmentManager by lazy { supportFragmentManager }

    private val service = ContactsDataSource.Local()

    private var currentContactId = service.getDefaultId()

    private val repositoryListeners = mutableListOf<RepositoryListener>()

    override fun getContact(id: Int): Contact = service.getContact(id)
    override fun getContacts(): List<Contact> = service.getContacts()

    override fun addRepositoryListener(listener: RepositoryListener) {
        repositoryListeners.add(listener)
    }

    override fun removeRepositoryListener(listener: RepositoryListener) {
        repositoryListeners.remove(listener)
    }

    private fun updateListeners() = repositoryListeners.forEach { it.repositoryUpdated() }

    override fun onItemClick(contactId: Int) {
        when {
            isTablet -> if (currentContactId != contactId) {
                currentContactId = contactId
                replaceDetailFragment(contactId)
            }
            else -> replaceDetailFragment(contactId)
        }
    }

    override fun onItemEdit(contactId: Int) {
        ContactEditFragment.newInstance(contactId).show(manager, ContactEditFragment.TAG)
    }

    override fun onItemSave(contact: Contact) {
        service.editContact(contact)
        removeFragment(ContactEditFragment.TAG)
        updateListeners()
    }

    override fun onItemDelete(id: Int) {
        showDeleteDialog(id)
    }

    override fun getDefaultId(): Int = service.getDefaultId()

    override fun accept(contactId: Int) {
        deleteContact(contactId)
    }

    private fun deleteContact(id: Int) {
        service.deleteContact(id)
        if (currentContactId == id) currentContactId = service.getDefaultId()
        updateListeners()
    }

    override fun onItemCancel() {
        removeFragment(ContactEditFragment.TAG)
    }

    /**
     * Entry point
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        when {
            isTablet -> setTabletFragments()
            else -> setPhoneFragments()
        }
    }


    /**
     * Initializes layout depend on device
     */
    private fun initView() {
        when {
            isTablet -> initTabletView()
            else -> initPhoneView()
        }
    }

    /**
     * Initializes tablet layout
     */
    private fun initTabletView() {
        _tabletBinding = ActivityLesson6LandBinding.inflate(layoutInflater)
        setContentView(tabletBinding.root)
    }

    /**
     * Initializes phone layout
     */
    private fun initPhoneView() {
        _phoneBinding = ActivityLesson6Binding.inflate(layoutInflater)
        setContentView(phoneBinding.root)
    }

    /**
     * Sets two fragment per one screen
     */
    private fun setTabletFragments() {
        replaceListFragment()
        replaceDetailFragment(0)
    }

    /**
     * Sets one fragment per screen
     */
    private fun setPhoneFragments() {
        replaceListFragment()
    }

    private fun replaceListFragment() {
        val tag = ContactRecyclerListFragment.TAG
        removeFragment(tag)
        replaceFragment(ContactRecyclerListFragment(), R.id.fragment_main, tag, false)
    }

    private fun replaceDetailFragment(contactId: Int) {
        val tag = ContactDetailsFragment.TAG
        removeFragment(tag)
        when {
            isTablet -> { replaceFragment(
                ContactDetailsFragment.newInstance(contactId),
                R.id.fragment_details,
                tag,
                false
            )}
            else -> { replaceFragment(
                ContactDetailsFragment.newInstance(contactId),
                R.id.fragment_main,
                tag,
                true
            ) }
        }

    }

    private fun showDeleteDialog(contactId: Int) {
        DeleteContactDialogFragment
            .newInstance(contactId)
            .show(manager, DeleteContactDialogFragment.TAG)
    }

    private fun replaceFragment(
        fragment: Fragment,
        containerViewId: Int,
        tag: String,
        addToBackStack: Boolean
    ) {
        manager
            .beginTransaction()
            .apply { if (addToBackStack) this.addToBackStack(tag) }
            .replace(
                containerViewId,
                fragment,
                tag
            )
            .commit()
    }

    private fun removeFragment(tag: String) {
        manager.findFragmentByTag(tag)?.let { existFragment ->
            manager.beginTransaction()
                .remove(existFragment)
                .commit()
        }
    }
}