package com.example.e_kanadmin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kanadmin.databinding.ActivityMainBinding
import com.example.e_kanadmin.listProduct.ListProductActivity
import com.example.e_kanadmin.listSeller.ListSellerActivity
import com.example.e_kanadmin.listUser.ListUserActivity
import com.example.e_kanadmin.retrofit.AuthService
import com.example.e_kanadmin.retrofit.DataService
import com.example.e_kanadmin.retrofit.RetrofitClient
import com.example.e_kanadmin.retrofit.response.DefaultResponse
import com.example.e_kanadmin.retrofit.response.LoginResponse
import com.example.e_kanadmin.retrofit.response.TransactionResponse
import com.example.e_kanadmin.settings.SettingActivity
import com.example.e_kanadmin.settings.profile.ProfileActivity
import com.example.e_kanadmin.transaction.TransactionEntity
import com.example.e_kanadmin.transaction.TransactionListAdapter
import com.example.e_kanadmin.utils.Constants
import com.example.e_kanadmin.utils.MySharedPreferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var transactionAdapter: TransactionListAdapter
    private var listTransaction: ArrayList<TransactionEntity> = arrayListOf()
    private var filterStatus: ArrayList<TransactionEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        myPreferences = MySharedPreferences(this@MainActivity)
        transactionAdapter = TransactionListAdapter()

        val iadmin = myPreferences.getValue(Constants.USER_ID).toString()
        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        mainBinding.btnProfile.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }

        mainBinding.llSetting.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            finish()
        }

        mainBinding.llListUser.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListUserActivity::class.java))
            finish()
        }

        mainBinding.llListSeller.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListSellerActivity::class.java))
            finish()
        }

        mainBinding.llListProduct.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListProductActivity::class.java))
            finish()
        }


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val deviceToken = task.result
            insertToken(iadmin, deviceToken.toString())
        })
        refreshAuthToken(iadmin)

        // Create an ArrayAdapter
        val array = resources.getStringArray(R.array.transaction_status_filter)
        val adapter = ArrayAdapter.createFromResource(this, R.array.transaction_status_filter, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        mainBinding.statusFilter.adapter = adapter
        mainBinding.statusFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (array[p2]) {
                    getString(R.string.show_all) -> {
                        filterStatus.clear()
                        transactionAdapter.setListTransactionItem(listTransaction)
                    }
                    getString(R.string.pending) -> {
                        filterStatus.clear()
                        listTransaction.forEach { listTransaction ->
                            if (listTransaction.transaksi == getString(R.string.pending)) {
                                filterStatus.add(listTransaction)
                            }
                        }
                        transactionAdapter.setListTransactionItem(filterStatus)
                    }
                    getString(R.string.approved) -> {
                        filterStatus.clear()
                        listTransaction.forEach { listTransaction ->
                            if (listTransaction.transaksi == getString(R.string.approved)) {
                                filterStatus.add(listTransaction)
                            }
                        }
                        transactionAdapter.setListTransactionItem(filterStatus)
                    }
                    getString(R.string.rejected) -> {
                        filterStatus.clear()
                        listTransaction.forEach { listTransaction ->
                            if (listTransaction.transaksi == getString(R.string.rejected)) {
                                filterStatus.add(listTransaction)
                            }
                        }
                        transactionAdapter.setListTransactionItem(filterStatus)
                    }
                    getString(R.string.canceled) -> {
                        filterStatus.clear()
                        listTransaction.forEach { listTransaction ->
                            if (listTransaction.transaksi == getString(R.string.canceled)) {
                                filterStatus.add(listTransaction)
                            }
                        }
                        transactionAdapter.setListTransactionItem(filterStatus)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        listTransaction(tokenAuth)
    }

    private fun insertToken(iadmin: String, device_token: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.addToken(iadmin, device_token).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }

    private fun refreshAuthToken(iadmin: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.refreshAuthToken(iadmin).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        myPreferences.setValue(Constants.TokenAuth, response.body()!!.tokenAuth)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }

    private fun listTransaction(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getTransaction(tokenAuth).enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        mainBinding.loadingAnim.visibility = View.GONE
                        val listData = response.body()!!.data
                        listTransaction = listData
                        transactionAdapter.setListTransactionItem(listTransaction)
                        transactionAdapter.notifyDataSetChanged()

                        with(mainBinding.rvTransaction) {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = transactionAdapter
                        }
                    } else {
                        Toasty.error(this@MainActivity, response.message(), Toasty.LENGTH_LONG).show()
                    }
                } else {
                    Toasty.error(this@MainActivity, "Mohon tutup aplikasi dan buka kembali", Toasty.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }
}