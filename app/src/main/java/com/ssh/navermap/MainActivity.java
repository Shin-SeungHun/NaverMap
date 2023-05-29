package com.ssh.navermap;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.ssh.navermap.Adapter.pointAdapter;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private MapView mapView;
    private static NaverMap naverMap;

    //마커 변수 선언 및 초기화
    private Marker marker1 = new Marker();
    private Marker marker2 = new Marker();

    //Infowindow 변수 선언 및 초기화
    private InfoWindow infoWindow1 = new InfoWindow();
    private InfoWindow infoWindow2 = new InfoWindow();
    private InfoWindow infoWindow3 = new InfoWindow();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnMark1 = (Button) findViewById(R.id.btnmark1);
        Button btnMark2 = (Button) findViewById(R.id.btnmark2);
        Button btnInfo1 = (Button) findViewById(R.id.btninfo1);
        Button btnInfo2 = (Button) findViewById(R.id.btninfo2);

        btnMark1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setMarker(marker1, 33.2712, 126.5354, R.drawable.ic_place_black_24dp, 0);

                marker1.setOnClickListener(new Overlay.OnClickListener()
                {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay)
                    {
                        Toast.makeText(getApplication(), "마커1 클릭", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                new LatLng(33.2712, 126.5354),15)
                        .animate(CameraAnimation.Fly, 3000);

                naverMap.moveCamera(cameraUpdate);

            }
        });

        btnMark2.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setMarker(marker2, 33.49957, 126.531076, R.drawable.ic_album_black_24dp, 10);

                marker2.setOnClickListener(new Overlay.OnClickListener()
                {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay)
                    {
                        ViewGroup rootView = (ViewGroup)findViewById(R.id.fragment_container);
                        pointAdapter adapter = new pointAdapter(MainActivity.this, rootView);

                        infoWindow3.setAdapter(adapter);

                        //인포창의 우선순위
                        infoWindow3.setZIndex(10);
                        //투명도 조정
                        infoWindow3.setAlpha(0.9f);
                        //인포창 표시
                        infoWindow3.open(marker2);
                        return false;
                    }
                });
                CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                new LatLng(33.49957, 126.531076),9)
                        .animate(CameraAnimation.Fly, 3000);

                naverMap.moveCamera(cameraUpdate);
            }
        });

        btnInfo1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                infoWindow1.setAdapter(new InfoWindow.DefaultTextAdapter(getApplication())
                {
                    @NonNull
                    @Override
                    public CharSequence getText(@NonNull InfoWindow infoWindow)
                    {
                        return "정보창1";
                    }
                });

                //인포창의 우선순위
                infoWindow1.setZIndex(10);
                //투명도 조정
                infoWindow1.setAlpha(0.9f);
                //위치 지정
                infoWindow1.setPosition(new LatLng(33.28, 126.4));
                //인포창 표시
                infoWindow1.open(naverMap);
            }
        });

        btnInfo2.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                infoWindow2.setAdapter(new InfoWindow.DefaultTextAdapter(getApplication())
                {
                    @NonNull
                    @Override
                    public CharSequence getText(@NonNull InfoWindow infoWindow)
                    {
                        return "마커1위에 표시";
                    }
                });

                //인포창의 우선순위
                infoWindow2.setZIndex(10);
                //투명도 조정
                infoWindow2.setAlpha(0.9f);
                //인포창 표시
                infoWindow2.open(marker1);

                infoWindow2.setOnClickListener(new Overlay.OnClickListener()
                {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay)
                    {
                        Toast.makeText(MainActivity.this, "정보창2 클릭됨", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
        });

        //네이버 지도
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void setMarker(Marker marker,  double lat, double lng, int resourceID, int zIndex)
    {
        //원근감 표시
        marker.setIconPerspectiveEnabled(true);
        //아이콘 지정
        marker.setIcon(OverlayImage.fromResource(resourceID));
        //마커의 투명도
        marker.setAlpha(0.8f);
        //마커 위치
        marker.setPosition(new LatLng(lat, lng));
        //마커 우선순위
        marker.setZIndex(zIndex);
        //마커 표시
        marker.setMap(naverMap);
    }

    private void setInfoWindow(InfoWindow infoWindow,  double lat, double lng, int resourceID, int zIndex)
    {
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap)
    {
        this.naverMap = naverMap;

        //배경 지도 선택
        naverMap.setMapType(NaverMap.MapType.Navi);

        //건물 표시
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        //위치 및 각도 조정
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(33.38, 126.55),   // 위치 지정
                9,                                     // 줌 레벨
                45,                                       // 기울임 각도
                0                                     // 방향
        );
        naverMap.setCameraPosition(cameraPosition);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}