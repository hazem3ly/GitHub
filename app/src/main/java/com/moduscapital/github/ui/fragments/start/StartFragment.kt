package com.moduscapital.github.ui.fragments.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.moduscapital.github.R
import com.moduscapital.github.extensions.isValidateEditText
import com.moduscapital.github.extensions.loadSavedUser
import com.moduscapital.github.extensions.value
import kotlinx.android.synthetic.main.start_fragment.*

class StartFragment : Fragment() {

    private lateinit var viewModel: StartViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StartViewModel::class.java)

        load_all_users?.setOnClickListener {
            openAllUsers()
        }

        search_btn?.setOnClickListener {
            if (user_name_et?.isValidateEditText() == true) {
                user_name_et?.clearFocus()
                openUserDetails(user_name_et?.value()!!)
            } else
                Toast.makeText(requireContext(), "Empty Text Error", Toast.LENGTH_SHORT).show()
        }

        load_saved?.setOnClickListener {

            val user = requireContext().loadSavedUser()
            if (user != "") {
                openUserDetails(user)
            } else
                Toast.makeText(requireContext(), "No Saved User", Toast.LENGTH_SHORT).show()
        }

    }


    private fun openAllUsers() {
        val action =
                StartFragmentDirections.actionStartFragmentToHomeFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun openUserDetails(userName: String) {
        val action =
                StartFragmentDirections.actionStartFragmentToUserDetailsFragment()
        action.userName = userName
        view?.findNavController()?.navigate(action)
    }


}
