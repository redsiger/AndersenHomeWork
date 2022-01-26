package com.example.androidschool.andersenhomeworks.lesson6.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactRecyclerListBinding
import com.example.androidschool.andersenhomeworks.lesson6.Contact
import com.example.androidschool.andersenhomeworks.lesson6.ContactsListener
import com.example.androidschool.andersenhomeworks.lesson6.RepositoryListener
import com.example.androidschool.andersenhomeworks.lesson6.fragments.recycler.ContactItemDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ContactRecyclerListFragment : Fragment(R.layout.fragment_contact_recycler_list), RepositoryListener, CoroutineScope {

    companion object {
        const val TAG = "CONTACT_RECYCLER_LIST_FRAGMENT_TAG"
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main

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
        initToolbar(
            viewBinding.searchInput,
            viewBinding.searchClear,
            ::search
        )
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

    private fun initToolbar(searchEditText: EditText, clearBtn: View, actionSearch: (String) -> Unit) {
        initSearch(searchEditText, clearBtn, actionSearch)
    }

    /**
     * Function to make input clearable and launches search for Contacts
     */
    private fun initSearch(searchEditText: EditText, clearBtn: View, actionSearch: (String) -> Unit) {
        searchEditText.addTextChangedListener(object: TextWatcher {
            private var _query = ""
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {

                text?.trim()?.apply {
                    val query = this.toString()
                    if (_query == query) return

                    _query = query

                    launch {
                        delay(1000)
                        if (_query != query) return@launch
                        actionSearch(_query)
                    }
                }
            }
            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(text: Editable?) {
                clearBtn.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })

        clearBtn.setOnClickListener {
            searchEditText.text.clear()
        }
    }


    private fun search(query: String) {
        contactListAdapter.setList(listener.searchContacts(query))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener.removeRepositoryListener(this)
    }
}