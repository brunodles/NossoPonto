package com.brunodles.nossoponto

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onStart() {
        super.onStart()
        username.text = (application as Application).currentUsername ?: DEFAULT_USERNAME
        wellcome.setOnClickListener { Preferences(this).setFinisher(true) }
        finish.setOnClickListener { finish() }
        finish.visibility = if (Preferences(this).isFinisher()) View.VISIBLE else View.GONE
    }

    companion object {
        private const val DEFAULT_USERNAME = "Fulano"

        fun newIntent(context: Context): Intent? =
                Intent(context, HomeActivity::class.java)

    }
}
