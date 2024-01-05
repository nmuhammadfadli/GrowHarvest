package com.features.growharvest.Fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import android.content.Intent
import com.features.growharvest.LoginActivity.DataAkun
//import com.features.growharvest.DataProduct.DataProductFrag  // Replace with the correct package name
//import com.features.growharvest.Transaksi.KeranjangActivity  // Replace with the correct package name
import com.features.growharvest.R
import com.features.growharvest.Sementara.DataProductActivity
import com.features.growharvest.Transaksi.KeranjangActivity

class HomeFrag : Fragment(R.layout.fragment_home) {
    private lateinit var btnTransaksi: ImageButton
    private lateinit var btnAccount: ImageButton
    private lateinit var btnProduct: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAccount = view.findViewById(R.id.btnAccount)
        btnTransaksi = view.findViewById(R.id.btnTransaksi)
        btnProduct = view.findViewById(R.id.btnProduct)

        btnAccount.setOnClickListener{
            val intent = Intent(requireActivity(), DataAkun::class.java)
            startActivity(intent)
        }

        btnProduct.setOnClickListener{
            val intent = Intent(requireActivity(), DataProductActivity::class.java)
            startActivity(intent)
        }

        btnTransaksi.setOnClickListener {
            val intent = Intent(requireActivity(), KeranjangActivity::class.java)
            requireActivity().startActivity(intent)
        }
    }
}

