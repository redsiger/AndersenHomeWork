package com.example.androidschool.andersenhomeworks.lesson5

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactsListBinding

class ContactsListFragment: Fragment(R.layout.fragment_contacts_list) {

    companion object {
        const val CONTACTS_LIST_FRAGMENT_TAG = "CONTACTS_LIST_FRAGMENT_TAG"
        private const val CONTACTS_LIST = "CONTACTS_LIST"

        fun newInstance(contactsList: ArrayList<Contact>): ContactsListFragment {
            val args = Bundle()
            args.putParcelableArrayList(CONTACTS_LIST, contactsList)

            val fragment = ContactsListFragment().apply {
                arguments = args
            }
            return fragment
        }


    }

    private var _viewBinding: FragmentContactsListBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val contactList: ArrayList<Contact> by lazy {
        arguments?.getParcelableArrayList(CONTACTS_LIST) ?: ArrayList()
    }

    private val listeners = mutableListOf<FragmentListener>()

    fun addFragmentListener(listener: FragmentListener) = listeners.add(listener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentContactsListBinding.bind(view)

        initList(contactList)
    }

    /**
     * Populates ViewGroup by children in form of contact
     */
    private fun initList(contactList: ArrayList<Contact>) {
        viewBinding.contactsList
        contactList.forEach { contact ->
            val linearLayout = viewBinding.contactsList
            val view = layoutInflater.inflate(
                R.layout.item_contact,
                linearLayout,
                false
            ) as ViewGroup
            fillViewByContact(view, contact)
            linearLayout.addView(view)
            view.setOnClickListener { listeners.forEach { it.itemClicked(contact.id) } }
        }
    }

    /**
     * Fills ViewGroup by Contact's data
     */
    private fun fillViewByContact(view: ViewGroup, contact: Contact) {
        view.children.forEach { view ->
            when {
                view.id == R.id.item_contact_firstName -> (view as TextView).text = contact.firstName
                view.id == R.id.item_contact_secondName -> (view as TextView).text = contact.secondName
                view.id == R.id.item_contact_phoneNumber -> (view as TextView).text = contact.phoneNumber
            }
        }
    }
}