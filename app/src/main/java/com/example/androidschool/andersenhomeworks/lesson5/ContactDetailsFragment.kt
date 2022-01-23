package com.example.androidschool.andersenhomeworks.lesson5

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactDetailsBinding

class ContactDetailsFragment: Fragment(R.layout.fragment_contact_details) {

    companion object {
        const val CONTACT_DETAILS_FRAGMENT_TAG = "CONTACT_DETAILS_FRAGMENT_TAG"
        private const val CONTACT = "CONTACT"

        fun newInstance(contact: Contact): ContactDetailsFragment {
            val args = Bundle()
            args.putParcelable(CONTACT, contact)

            val fragment = ContactDetailsFragment().apply {
                arguments = args
            }
            return fragment
        }
    }

    private var _viewBinding: FragmentContactDetailsBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val contact: Contact by lazy {
        arguments?.getParcelable(CONTACT) ?: Contact()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentContactDetailsBinding.bind(view)
        renderContact(contact)
        Log.e(CONTACT_DETAILS_FRAGMENT_TAG, "onViewCreated, $contact")
    }

    private fun renderContact(contact: Contact) {
        with(viewBinding) {
            contactDetailsFirstName.text = contact.firstName
            contactDetailsSecondName.text = contact.secondName
            contactDetailsPhoneNumber.text = contact.phoneNumber
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(CONTACT_DETAILS_FRAGMENT_TAG, "onDestroy, $contact")
    }
}