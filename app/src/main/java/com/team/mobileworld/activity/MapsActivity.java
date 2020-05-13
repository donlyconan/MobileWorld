package com.team.mobileworld.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.LocationInfo;
import com.team.mobileworld.core.object.Geocode;
import com.team.mobileworld.core.object.Place;
import com.team.mobileworld.core.service.LocationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final int SIZE_CURRENT_LOCATION = 16;
    private static final String DEBUG = "projectinfo";
    FusedLocationProviderClient locationProviderClient;
    public static String Address = "";
    private GoogleMap googlemap;
    Location location;
    Button btnselect;
    TextView txtaddress;
    Button btnlocation, btnclose;
    SearchView searchView;
    ListView listView;
    List<Place> places;
    Call<ResponseBody> serviceplaces, servicegeocode;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        init();

        searchView.setOnQueryTextListener(actionSearch());

        btnlocation.setOnClickListener(e -> showCurrentLocation());

        searchView.setOnLongClickListener(v -> {
            if (places != null) {
                listView.setVisibility(View.INVISIBLE);
                places.clear();
            }
            return true;
        });

        searchView.setOnCloseListener(() -> {
            if (places != null) {
                listView.setVisibility(View.INVISIBLE);
                places.clear();
            }
            return true;
        });

        listView.setOnItemClickListener(OnSelectItem());
        LocationInfo.register(this);
    }

    private void init() {
        btnselect = findViewById(R.id.btnselect);
        txtaddress = findViewById(R.id.txtaddress);
        btnlocation = findViewById(R.id.btn_currentlocation);
        searchView = findViewById(R.id.search_view);
        listView = findViewById(R.id.list_address);
        listView.setVisibility(View.INVISIBLE);
        btnclose = findViewById(R.id.btn_close);

        btnclose.setOnClickListener(e -> finish());
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnselect.setOnClickListener(e -> {
            String address = txtaddress.getText().toString();
            print("address="+address);
            final Intent intent = new Intent();
            intent.putExtra("address", address);
            Address = address;
            setResult(RESULT_OK, intent);
            finish();
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (Intent.CATEGORY_OPENABLE == getIntent().getAction()) {
            searchView.setVisibility(View.INVISIBLE);
            btnlocation.setVisibility(View.INVISIBLE);
            txtaddress.setVisibility(View.INVISIBLE);
            btnselect.setText("Close");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MainActivity.REQUEST_LOCATION && grantResults.length > 0) {
            showCurrentLocation();
        } else {
            showToast("Không thể truy cập GPS!");
        }
    }

    public AdapterView.OnItemClickListener OnSelectItem() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                print("start action");
                if (servicegeocode != null)
                    servicegeocode.cancel();
                servicegeocode = NetworkCommon.buildURL(LocationService.BASE_URL_DEVELOPER_MAPQUEST)
                        .create(LocationService.class)
                        .searchGeocodeLocation(places.get(position).getDescription());
                final ProgressDialog progressDialog = createProgressDialog("Đang tìm tọa độ...");
                servicegeocode.enqueue(new Callback<ResponseBody>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                Geocode geocode = Geocode.getListGeocode(response.body())
                                        .get(0);
                                LatLng latLng = new LatLng(geocode.getLatlng().getLat(), geocode.getLatlng().getLng());
                                googlemap.clear();
                                showAnimateLocationOnMapLatLng(latLng, places.get(position).getDescription());
                                print(String.format("geocode=" + geocode));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                        listView.setVisibility(View.INVISIBLE);
                        places.clear();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToast(t.getMessage());
                        progressDialog.dismiss();
                    }
                });
                progressDialog.show();
            }
        };

    }

    public SearchView.OnQueryTextListener actionSearch() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                print("action search");
                if (serviceplaces != null)
                    serviceplaces.cancel();
                serviceplaces = NetworkCommon.buildURL(LocationService.BASE_URL_GOOGLE)
                        .create(LocationService.class)
                        .searchPlaceLocation(query);
                final ProgressDialog progressDialog = createProgressDialog("Đang tìm kiếm...");
                serviceplaces.enqueue(new Callback<ResponseBody>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                places = Place.getListPlace(response.body());
                                List<String> address = new ArrayList<>(places.size());
                                places.forEach(e -> address.add(e.getDescription()));
                                ArrayAdapter adapter = new ArrayAdapter(MapsActivity.this, android.R.layout.simple_list_item_1, address);
                                listView.setAdapter(adapter);

                                if (places.size() > 0) {
                                    listView.setVisibility(View.VISIBLE);
                                } else {
                                    listView.setVisibility(View.INVISIBLE);
                                    showToast(String.format("Không tìm thấy kết quả trùng khớp %s", query));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        print("Result search=" + places.size());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToast(t.getMessage());
                        progressDialog.dismiss();
                    }
                });
                progressDialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }

    public ProgressDialog createProgressDialog(String message) {
        ProgressDialog progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        return progressDialog;
    }


    public void findAddress(List<Address> addressList) throws Exception {
        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        googlemap.clear();
        txtaddress.setText(address.getAddressLine(0));
        showAnimateLocationOnMapLatLng(latLng, address.getAddressLine(0));
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void getCurrentLocation() {
//        LatLng
////        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
////        {
////            ProgressDialog progressDialog = createProgressDialog("Đang truy cập...");
////            locationProviderClient.getLastLocation().addOnSuccessListener(x -> {
////                MapsActivity.this.location = x;
////                print("location=" + x);
////                if (x == null)
////                    showToast("Không cập nhật được vị trí hiện tại.");
////                else
////                    runnable.run();
////                progressDialog.dismiss();
////            });
////            progressDialog.show();
////        } else {
////            showToast("Không thể truy cập vào GPS!");
////        }
//
//    }

    public String getAddress(Address address) {
        String line = "";
        Log.d("test", address + "");
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            line += address.getAddressLine(i);
        }
        return line;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googlemap = googleMap;
        showCurrentLocation();

        if (Intent.CATEGORY_OPENABLE == getIntent().getAction()) {
            double lat = getIntent().getExtras().getFloat("lat");
            double lng = getIntent().getExtras().getFloat("lng");
            Geocoder geocoder = new Geocoder(this);
            try {
                googlemap.clear();
                List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
                findAddress(addressList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            googlemap.setOnMapClickListener(lng -> {

                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addressList = geocoder.getFromLocation(lng.latitude, lng.longitude, 1);
                    findAddress(addressList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    private void showAnimateLocationOnMapLatLng(LatLng latlng, String title) {
        googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, SIZE_CURRENT_LOCATION));
        googlemap.addMarker(new MarkerOptions().position(latlng).title(title));
        txtaddress.setText(title);
    }

    private void showLocationOnMapLatLng(LatLng latlng, String title) {
        googlemap.addMarker(new MarkerOptions().position(latlng).title(title));
        googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, SIZE_CURRENT_LOCATION));
        googlemap.animateCamera(CameraUpdateFactory.zoomTo(SIZE_CURRENT_LOCATION), 2000, null);
        txtaddress.setText(title);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showCurrentLocation() {
        LocationInfo.worker = () -> {
            try {
                googlemap.clear();
                LatLng latLng = LocationInfo.getLatLng();
                Address address = new Geocoder(this)
                        .getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                showAnimateLocationOnMapLatLng(latLng, address.getAddressLine(0));
                txtaddress.setText(address.getAddressLine(0));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };
        LocationInfo.getCurrentLocation();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static void print(String message) {
        Log.d(DEBUG, message);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
