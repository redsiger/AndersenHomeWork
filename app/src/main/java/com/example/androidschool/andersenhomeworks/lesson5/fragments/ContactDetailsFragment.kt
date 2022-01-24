package com.example.androidschool.andersenhomeworks.lesson5.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactDetailsBinding
import com.example.androidschool.andersenhomeworks.lesson5.Contact
import com.example.androidschool.andersenhomeworks.lesson5.FragmentListener
import com.example.androidschool.andersenhomeworks.lesson5.RepositoryListener

class ContactDetailsFragment: Fragment(R.layout.fragment_contact_details), RepositoryListener {

    companion object {
        const val TAG = "CONTACT_DETAILS_FRAGMENT_TAG"
        private const val CONTACT_ID = "CONTACT_ID"

        fun newInstance(): ContactDetailsFragment = ContactDetailsFragment()
    }

    private var _viewBinding: FragmentContactDetailsBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val listener by lazy { requireActivity() as FragmentListener }
    private val contactId: Int by lazy { listener.getCurrentId() }
    private val contact: Contact get() =  listener.getContact(contactId)

    override fun repositoryUpdated() = render(contact)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentContactDetailsBinding.bind(view)

        listener.addRepositoryListener(this)
        render(contact)
        initEditBtn()
    }

    private fun render(contact: Contact) {
        with(viewBinding) {
            contactDetailsFirstName.text = contact.firstName
            contactDetailsSecondName.text = contact.secondName
            contactDetailsPhoneNumber.text = contact.phoneNumber
        }
    }

    private fun initEditBtn() {
        viewBinding.contactDetailsEditBtn.setOnClickListener {
            listener.onItemEdit(contactId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener.removeRepositoryListener(this)
    }
}
