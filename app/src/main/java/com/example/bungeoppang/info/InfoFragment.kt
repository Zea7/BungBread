package com.example.bungeoppang.info

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bungeoppang.R
import com.example.bungeoppang.api.ApiClient
import com.example.bungeoppang.api.ApiInterface
import com.example.bungeoppang.data.Coord
import com.example.bungeoppang.data.Stores
import com.example.bungeoppang.data.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoFragment : Fragment() {
    private lateinit var pickedRV: RecyclerView
    private lateinit var registeredRV: RecyclerView
    private lateinit var writtenRV: RecyclerView
    private lateinit var commentedRV: RecyclerView

    private lateinit var adapter: StoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvName: TextView = view.findViewById(R.id.tv_name)
        pickedRV = view.findViewById(R.id.rv_picked)
        pickedRV.layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.HORIZONTAL }

        registeredRV = view.findViewById(R.id.rv_registered)
        registeredRV.layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.HORIZONTAL }

        this.adapter = StoreAdapter(requireContext(), Stores(), layoutInflater)
        pickedRV.adapter = this.adapter

        val apiClient = ApiClient?.instance?.create(ApiInterface::class.java)

        apiClient?.getUserByUserId(2067452916)?.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()?.user
                tvName.text = user?.nickName

                val stores = arrayListOf<Stores.StoreItem>()
                user?.picked?.forEach {
                    Log.d("sss", it)
                    apiClient.getStoreByStoreId(it).enqueue(object  : Callback<Stores.StoreItem>{
                        override fun onResponse(
                            call: Call<Stores.StoreItem>,
                            response: Response<Stores.StoreItem>
                        ) {
                            stores.add(response.body()!!)
                            this@InfoFragment.adapter.itemList = stores
                            this@InfoFragment.adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<Stores.StoreItem>, t: Throwable) {
                            Log.e("err", t.message.toString())
                        }

                    })
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

            }

        })
        apiClient?.getStoresByUserId(2067452916)?.enqueue(object : Callback<Stores>{
            override fun onResponse(call: Call<Stores>, response: Response<Stores>) {
                val stores = response.body()
                registeredRV.adapter = StoreAdapter(context!!, stores!!, layoutInflater)
                Log.d("store_user_id", response.body().toString())
            }

            override fun onFailure(call: Call<Stores>, t: Throwable) {
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InfoFragment()
    }
}