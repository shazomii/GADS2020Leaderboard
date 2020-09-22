package com.davenet.gadsleaderboard.ui.submission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.davenet.gadsleaderboard.R
import com.davenet.gadsleaderboard.util.InputsValidator
import com.davenet.gadsleaderboard.util.toast
import com.davenet.gadsleaderboard.viewmodels.SubmitViewModel
import kotlinx.android.synthetic.main.fragment_submit.*


class SubmitFragment : Fragment(), ConfirmDialogFragment.OnSubmitButtonClicked {
    private val inputValidator: InputsValidator by lazy { InputsValidator() }
    private val submitViewModel: SubmitViewModel by lazy {
        ViewModelProvider(this, SubmitViewModel.Factory(requireActivity().application))
            .get(SubmitViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_submit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
        initListeners()
    }

    private fun initListeners() {
        confirmButton.setOnClickListener {
            if (validateSubmission()){
                val dialog = ConfirmDialogFragment()
                dialog.setSubmitButtonClickListener(this)
                dialog.show(childFragmentManager, dialog.tag)
            }
        }
    }
    private fun subscribeToLiveData(){
        submitViewModel.submissionSuccess.observe(viewLifecycleOwner, {
            val successDialog = SuccessDialogFragment()
            val errorDialog = ErrorDialogFragment()
            if (it == true){
                successDialog.show(childFragmentManager, successDialog.tag)
            }
            else{
                errorDialog.show(childFragmentManager, errorDialog.tag)
            }
        })
        submitViewModel.loadingState.observe(viewLifecycleOwner, {loading ->
            if (loading == true){
                submitProgressBar.visibility = View.VISIBLE
            }
            else
            {
                submitProgressBar.visibility = View.GONE
            }
        })
        submitViewModel.toast.observe(viewLifecycleOwner, {
            this.requireActivity().toast(it)
        })
    }
    //submit project details
    private fun submitProject(){
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val projectLink = githubEditText.text.toString()
        submitViewModel.submitProject(
            firstName, lastName, email,projectLink
        )
    }

    override fun onSubmitButtonClicked(button: View) {
        button.setOnClickListener { submitProject() }
    }

    private fun validateSubmission(): Boolean{
        var areCredentialsValid = true
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val projectLink = githubEditText.text.toString()
        inputValidator.setCredentials(
            firstName, lastName, email, projectLink
        )
        if (!inputValidator.isFirstNameValid()){
            areCredentialsValid = false
            firstNameEditText.error = getString(R.string.first_name_error)
            firstNameEditText.requestFocus()
        }
        if (!inputValidator.isLastNameValid()){
            areCredentialsValid = false
            lastNameEditText.error = getString(R.string.last_name_error)
            lastNameEditText.requestFocus()
        }
        if (!inputValidator.isEmailValid()){
            areCredentialsValid = false
            emailEditText.error = getString(R.string.email_error)
            emailEditText.requestFocus()
        }
        if (!inputValidator.isProjectLinkValid()){
            areCredentialsValid = false
            githubEditText.error = getString(R.string.link_error)
            githubEditText.requestFocus()
        }
        return areCredentialsValid
    }
}