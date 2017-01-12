package com.xinning.mapdemo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public AMap aMap;//高德地图属性
    @Bind(R.id.map)
    MapView map;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //设置监听器
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                    final int locationType = aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    final double latitude = aMapLocation.getLatitude();//获取纬度
                    final double longitude = aMapLocation.getLongitude();//获取经度
                    final float accuracy = aMapLocation.getAccuracy();//获取精度信息
                    final  String address = aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    final String country = aMapLocation.getCountry();//国家信息
                    final String province = aMapLocation.getProvince();//省信息
                   final  String city = aMapLocation.getCity();//城市信息
                   final  String district = aMapLocation.getDistrict();//城区信息
                   final  String street = aMapLocation.getStreet();//街道信息
                   final  String streetNum = aMapLocation.getStreetNum();//街道门牌号信息
                    final String cityCode = aMapLocation.getCityCode();//城市编码
                   final  String adCode = aMapLocation.getAdCode();//地区编码
                    final String aoiName = aMapLocation.getAoiName();//获取当前定位点的AOI信息
                    final String buildingId = aMapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                    final String floor = aMapLocation.getFloor();//获取当前室内定位的楼层
                    int gpsStatus = aMapLocation.getGpsAccuracyStatus();

                    Log.e("1","定位类型:"+locationType);
                    Log.e("1","维度:"+latitude);
                    Log.e("1","经度:"+longitude);
                    Log.e("1","精度:"+accuracy);
                    Log.e("1","地址:"+address);
                    Log.e("1","国家:"+country);
                    Log.e("1","省:"+province);
                    Log.e("1","城市:"+city);
                    Log.e("1","城区信息:"+district);
                    Log.e("1","街道:"+street);
                    Log.e("1","门牌信息:"+streetNum);
                    Log.e("1","城市编码:"+cityCode);
                    Log.e("1","地区编码:"+adCode);
                    Log.e("1","AOI信息:"+aoiName);
                    Log.e("1","第几层："+floor);
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }

        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            Log.e("1",result.substring(0, result.length()-1));
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sHA1(getApplicationContext());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //创建地图
        map.onCreate(savedInstanceState);
        //初始化地图变量
        if (aMap == null) {
            aMap = map.getMap();
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。--设置定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setInterval(1000);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否强制刷新WIFI，默认为true，强制刷新。
            mLocationOption.setWifiActiveScan(false);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(20000);
            //关闭缓存机制
            mLocationOption.setLocationCacheEnable(false);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }
}
