package com.brunodles.nossoponto

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login.setOnClickListener(this::onLoginClick)
        username.editText?.setText(Preferences(this).getPreviousUsername())
        password.editText?.setOnEditorActionListener { v, actionId, event ->
            onLoginClick(v)
            true
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onLoginClick(view: View) {
        val username = username.editText?.text?.toString()
        if (username.isNullOrBlank()) {
            Snackbar.make(root, "Invalid username", Snackbar.LENGTH_LONG).show()
        } else {
            Preferences(this).setPreviousUsername(username!!)
            (application as Application).currentUsername = username
            startActivity(HomeActivity.newIntent(this))
        }
    }
}
