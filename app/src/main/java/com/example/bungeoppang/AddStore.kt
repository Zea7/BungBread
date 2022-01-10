package com.example.bungeoppang

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView

class AddStore : AppCompatActivity() , MapView.MapViewEventListener, MapView.POIItemEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener{
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0
    private var save_button: Button? = null
    private var map:MapView? = null
    private var text: TextView? = null
    private var marker:MapPOIItem = MapPOIItem()
    private val TAG = "AppStore"
    private var address: String? = null

    override fun onBackPressed() {
        goBackToMain()
    }

    // App bar 버튼 동작
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            goBackToMain()
        }
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_store)

        // 뒤로가기 버튼
        getSupportActionBar()?.setTitle("Gallery")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        marker.isDraggable = true
        marker.itemName = "가게 위치"
        marker.markerType = MapPOIItem.MarkerType.CustomImage
        marker.customImageResourceId = R.drawable.blue_marker

        save_button = findViewById(R.id.save_store_button)
        text = findViewById(R.id.address_show)
        text?.setTextColor(R.color.black)
        map = findViewById<MapView>(R.id.add_store_map)
        map?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true)
        marker.mapPoint = map?.mapCenterPoint
        map?.addPOIItem(marker)
        map?.setPOIItemEventListener(this)

        // 저장 버튼을 누르면 세부 정보를 수정할 수 있는 Activity call
        save_button?.setOnClickListener {
            intent = Intent(baseContext, AdditionalInfo::class.java)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("address", address)
            startActivity(intent)
            (map?.parent as ViewGroup).removeView(map)
            MainActivity.main?.finish()
        }

        getInfo()
        addressUpdate()
        showLocationAtCenter()
    }

    // MainActivity로부터 현재 지도 위치 받아옴
    private fun getInfo(){
        val intent: Intent = this.intent
        latitude = intent.getDoubleExtra("latitude", 33.452613)
        longitude = intent.getDoubleExtra("longitude", 126.570888)
    }

    private fun showLocationAtCenter(){
        map?.setMapViewEventListener(this)

    }

    private fun addressUpdate(){
        val geoCoder = MapReverseGeoCoder(Variables.NATIVE_APP_KEY, MapPoint.mapPointWithGeoCoord(latitude,longitude), this, this)
        geoCoder.startFindingAddress()
    }

    private fun addressUpdate(lat:Double, long:Double){
        latitude = lat
        longitude = long
        val geoCoder = MapReverseGeoCoder(Variables.NATIVE_APP_KEY, MapPoint.mapPointWithGeoCoord(lat,long), this, this)
        geoCoder.startFindingAddress()

    }

    override fun onMapViewInitialized(p0: MapView?) {
        map!!.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 2, true)
        addressUpdate()
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {


    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {

    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
        val mapPoint:MapPoint.GeoCoordinate = p1!!.mapPointGeoCoord
        val lat = mapPoint.latitude
        val long = mapPoint.longitude
        marker.mapPoint= MapPoint.mapPointWithGeoCoord(lat, long)
        addressUpdate(lat, long)
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {

    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {

    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        val mapPoint:MapPoint.GeoCoordinate = p2!!.mapPointGeoCoord
        val lat = mapPoint.latitude
        val long = mapPoint.longitude
        addressUpdate(lat, long)
    }

    override fun onReverseGeoCoderFoundAddress(p0: MapReverseGeoCoder?, p1: String?) {
        text?.text = p1!!
        address = p1
    }

    override fun onReverseGeoCoderFailedToFindAddress(p0: MapReverseGeoCoder?) {
        Log.e(TAG, "주소 불러오기 실패", null)
        text?.text = String.format("(%f, %f)", latitude, longitude)
    }

    private fun goBackToMain(){
        (map?.parent as ViewGroup).removeView(map)
        MainActivity.main?.finish()
        latitude = intent.getDoubleExtra("latitude", 33.452613)
        longitude = intent.getDoubleExtra("longitude", 126.570888)
        MainActivity.zoomLevel = intent.getIntExtra("zoomlevel", 1)
        MainActivity.latitude = latitude
        MainActivity.longitude = longitude
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}