package com.davenet.gadsleaderboard.ui.submission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.davenet.gadsleaderboard.R
import com.davenet.gadsleaderboard.ui.main.MainActivity
import com.davenet.gadsleaderboard.util.InputsValidator
import com.davenet.gadsleaderboard.util.toast
import com.davenet.gadsleaderboard.viewmodels.SubmitViewModel
import kotlinx.android.synthetic.main.activity_submit.*


class SubmitActivity : AppCompatActivity(), ConfirmDialogFragment.OnSubmitButtonClicked {
    private val inputValidator: InputsValidator by lazy { InputsValidator() }
    private val submitViewModel: SubmitViewModel by lazy {
        ViewModelProvider(this, SubmitViewModel.Factory(application))
            .get(SubmitViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)
        subscribeToLiveData()
        initListeners()
    }
    private fun initListeners() {
        confirmButton.setOnClickListener {
            if (validateSubmission()){
                val dialog = ConfirmDialogFragment()
                dialog.setSubmitButtonClickListener(this)
                dialog.show(supportFragmentManager, dialog.tag)
            }
        }
    }
    private fun subscribeToLiveData(){
        submitViewModel.submissionSuccess.observe(this, {
            if (it == true){
                val dialog = SuccessDialogFragment()
                dialog.show(supportFragmentManager, dialog.tag)
            }
            else{
                val dialog = ErrorDialogFragment()
                dialog.show(supportFragmentManager, dialog.tag)
            }
        })
        submitViewModel.loadingState.observe(this, {loading ->
            if (loading == true){
                submitProgressBar.visibility = View.VISIBLE
            }
            else
            {
                submitProgressBar.visibility = View.GONE
            }
        })
        submitViewModel.toast.observe(this, {
            this.toast(it)
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