package com.example.androidschool.andersenhomeworks.lesson6.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactRecyclerListBinding
import com.example.androidschool.andersenhomeworks.lesson5.Contact
import com.example.androidschool.andersenhomeworks.lesson5.FragmentListener
import com.example.androidschool.andersenhomeworks.lesson5.RepositoryListener

class ContactRecyclerListFragment: Fragment(R.layout.fragment_contact_recycler_list), RepositoryListener {

    companion object {
        const val TAG = "CONTACT_RECYCLER_LIST_FRAGMENT_TAG"
    }

    private var _viewBinding: FragmentContactRecyclerListBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val listener by lazy { requireActivity() as FragmentListener }
    private val contactList get() = listener.getContacts()

    private val contactListAdapter by lazy {
        ContactAdapter(listener::onItemClick, listener::onItemDelete)
    }

    override fun repositoryUpdated() {
        Log.e("RECYCLER", "invoked")
        contactListAdapter.setList(listener.getContacts())
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