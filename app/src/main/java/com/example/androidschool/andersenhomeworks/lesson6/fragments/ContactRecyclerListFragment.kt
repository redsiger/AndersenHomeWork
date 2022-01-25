package com.example.androidschool.andersenhomeworks.lesson6.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactRecyclerListBinding
import com.example.androidschool.andersenhomeworks.lesson6.Contact
import com.example.androidschool.andersenhomeworks.lesson6.ContactsListener
import com.example.androidschool.andersenhomeworks.lesson6.RepositoryListener
import com.example.androidschool.andersenhomeworks.lesson6.fragments.recycler.ContactItemDecorator

class ContactRecyclerListFragment: Fragment(R.layout.fragment_contact_recycler_list), RepositoryListener {

    companion object {
        const val TAG = "CONTACT_RECYCLER_LIST_FRAGMENT_TAG"
    }

    private var _viewBinding: FragmentContactRecyclerListBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val listener by lazy { requireActivity() as ContactsListener }
    private val contactList get() = listener.getContacts()

    private val contactListAdapter by lazy {
        ContactAdapter(listener)
    }

    override fun repositoryUpdated() {
        contactListAdapter.setList(contactList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentContactRecyclerListBinding.bind(view)

        listener.addRepositoryListener(this)
        initRecycler()
        render(contactList)
    }

    private fun initRecycler() {
        viewBinding.contactRecyclerListContactList.apply {
            adapter = contactListAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            val margin = requireContext().resources.getDimension(R.dimen.contact_main_padding).toInt()

            addItemDecoration(
                ContactItemDecorator(marginBottom =  margin)
            )
        }
    }

    private fun render(contactList: List<Contact>) {
        contactListAdapter.setList(contactList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener.removeRepositoryListener(this)
    }
}