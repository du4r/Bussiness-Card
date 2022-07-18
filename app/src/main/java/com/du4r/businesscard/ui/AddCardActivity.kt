package com.du4r.businesscard.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.du4r.businesscard.App
import com.du4r.businesscard.R
import com.du4r.businesscard.data.BusinessCard
import com.du4r.businesscard.databinding.ActivityAddCardBinding

class AddCardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddCardBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels{
        MainViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        insertListener()
    }

    private fun insertListener() {
        binding.btnClose
            .setOnClickListener {
                finish()
            }

        binding.btnCreate
            .setOnClickListener {
                val businessCard = BusinessCard(
                        name = binding.tilName.editText?.text.toString(),
                        company = binding.tilCompany.editText?.text.toString(),
                        phone = binding.tilPhone.editText?.text.toString(),
                        email = binding.tilEmail.editText?.text.toString(),
                        color = binding.tilColor.editText?.text.toString()
                )
                mainViewModel.insert(businessCard)
                Toast.makeText(this, R.string.label_show_success, Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}