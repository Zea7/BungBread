package com.example.bungeoppang

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.example.bungeoppang.AddStore.AddStore
import com.example.bungeoppang.retrofit.DistanceStoreItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainFragment : Fragment(){
    private lateinit var locationManager: LocationManager
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0
    private var zoom = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView:ViewGroup = inflater.inflate(R.layout.fragment_main, container, false) as ViewGroup
        val map: MapView = rootView.findViewById(R.id.map_view)
        val context = context
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location: Location? = if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else null

        val button:Button = rootView.findViewById(R.id.add_store_button) as Button
        val now_location:ImageView = rootView.findViewById(R.id.my_location)
        var jsons = ArrayList<DistanceStoreItem>()
        var stores = ArrayList<JSONObject>()
        CoroutineScope(Dispatchers.Main).launch{
                getLocation()
                map.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), zoom,true)

                map.addPOIItem(getUserMarker())
                if(MainActivity.latitude != 0.0 || MainActivity.longitude != 0.0){
                    latitude = MainActivity.latitude
                    longitude = MainActivity.longitude
                    zoom = MainActivity.zoomLevel
                } else
                    getLocation()
                CoroutineScope(Dispatchers.Main).launch{
                        jsons = ServerConnect.getStoreByDistance(1000, latitude, longitude, context)
                }.join()
                stores = getStoresMarker(jsons, map)
        }






        button.setOnClickListener {
            val intent: Intent = Intent(context, AddStore::class.java)
            latitude = map.mapCenterPoint.mapPointGeoCoord.latitude
            longitude = map.mapCenterPoint.mapPointGeoCoord.longitude
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("zoomlevel", map.zoomLevel)
            rootView.removeView(rootView.findViewById(R.id.map_view))
            startActivity(intent)
        }

        now_location.setOnClickListener{
            map.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 3,true)
        }

        return rootView
    }

    fun getStoresMarker(jsons:ArrayList<DistanceStoreItem>, map:MapView):ArrayList<JSONObject>{
        val array = ArrayList<JSONObject>()
        for(i in 0 until jsons.size){
            val json:DistanceStoreItem = jsons[i]
            Log.d("CheckInMarker", json.toString())
            val temp = JSONObject()
            temp.put("name", json.registrant.nickName)
            temp.put("latitude", json.y)
            temp.put("longitude", json.x)
            temp.put("storeName", json.storeName)
            temp.put("address", json.address)
            temp.put("desc", json.desc)
            temp.put("comments", json.comments)
            temp.put("menus", json.menus)
            array.add(temp)
            val marker = MapPOIItem()
            marker.selectedMarkerType = MapPOIItem.MarkerType.BluePin
            marker.mapPoint = MapPoint.mapPointWithGeoCoord(temp.getDouble("latitude"), temp.getDouble("longitude"))
            marker.itemName = temp.getString("storeName")
            map.addPOIItem(marker)
        }

        return array
    }

    companion object {
        @JvmStatic
        fun newInstance() :MainFragment = MainFragment()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){

        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var userLocation: Location? = getLatLng()

        if(userLocation != null){
            latitude = userLocation.latitude
            longitude = userLocation.longitude
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLatLng(): Location? {
        var currentLatLng: Location? = null

        try{
            val gpsLocationListener = object:LocationListener{
                override fun onLocationChanged(p0: Location) {
                }
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000,
                1F,
                gpsLocationListener)
            val locatioNProvider = LocationManager.NETWORK_PROVIDER
            currentLatLng = locationManager.getLastKnownLocation(locatioNProvider)
        }catch (e:Exception){
            return null
        }
        return currentLatLng
    }

    private fun getUserMarker():MapPOIItem{
        val customMarker = MapPOIItem()
        customMarker.itemName = "User"
        customMarker.tag = 1
        customMarker.mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)
        customMarker.markerType = MapPOIItem.MarkerType.CustomImage
        customMarker.customImageResourceId = R.drawable.user_marker
        customMarker.isCustomImageAutoscale = false
        return customMarker
    }
}