package com.example.androidschool.andersenhomeworks.lesson6.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactDetailsLesson6Binding
import com.example.androidschool.andersenhomeworks.lesson6.Contact
import com.example.androidschool.andersenhomeworks.lesson6.FragmentListener
import com.example.androidschool.andersenhomeworks.lesson6.RepositoryListener

class ContactDetailsFragment: Fragment(R.layout.fragment_contact_details_lesson6), RepositoryListener {

    companion object {
        const val TAG = "CONTACT_DETAILS_FRAGMENT_TAG"

        fun newInstance(): ContactDetailsFragment = ContactDetailsFragment()
    }

    private var _viewBinding: FragmentContactDetailsLesson6Binding? = null
    private val viewBinding get() = _viewBinding!!

    private val listener by lazy { requireActivity() as FragmentListener }
    private val contact: Contact get() =  listener.getContact(listener.getCurrentId())

    override fun repositoryUpdated() {
        render(contact)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentContactDetailsLesson6Binding.bind(view)

        listener.addRepositoryListener(this)

        when (contact.id) {
            -1 -> hideView(view as ViewGroup)
            else -> {
                showView(view as ViewGroup)
                render(contact)
                initEditBtn()
            }
        }
    }

    private fun render(contact: Contact) {
        with(viewBinding) {
            contactDetailsFirstName.text = contact.firstName
            contactDetailsSecondName.text = contact.lastName
            contactDetailsPhoneNumber.text = contact.phoneNumber
            Glide
                .with(requireActivity())
                .load(contact.imgUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .signature(ObjectKey(contact.imgUrl + contact.id))
                .into(contactDetailsImg)
        }
    }

    private fun initEditBtn() {
        viewBinding.contactDetailsEditBtn.setOnClickListener {
            listener.onItemEdit(contact.id)
        }
    }

    private fun showView(viewGroup: ViewGroup) {
        viewGroup.children.forEach {
            it.visibility = View.VISIBLE
        }
    }

    private fun hideView(viewGroup: ViewGroup) {
        viewGroup.children.forEach {
            it.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener.removeRepositoryListener(this)
    }
}
