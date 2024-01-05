package com.features.growharvest.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
//import com.features.growharvest.DataProduct.DataProductFrag
import com.features.growharvest.R

class AccountFrag : Fragment(R.layout.fragment_akun) {
    private lateinit var btnLogOut: ImageButton
    private lateinit var txtUsername: TextView
    private lateinit var imageUser: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogOut = view.findViewById(R.id.imgbtnLogout)
        txtUsername = view.findViewById(R.id.txtUsername)
        imageUser = view.findViewById(R.id.userImage)

        btnLogOut.setOnClickListener{
            val intent = Intent(requireActivity(), AccountFrag::class.java)
            startActivity(intent)
        }
    }
}