package com.bora.gustavo.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bora.gustavo.NewGymDialogFragment;
import com.bora.gustavo.R;
import com.bora.gustavo.helper.LocationHolderSingleton;
import com.bora.gustavo.helper.Utils;
import com.bora.gustavo.models.Gym;
import com.bora.gustavo.models.GymParcelable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener, MainCallback {
    private final static String TAG = "MainActivity";
    private static final float MAP_DEFAULT_ZOOM = 15f;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private boolean mMapAnimated = false;
    private List<Gym> mGymsList = new ArrayList<>();
    private Utils mUtils;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_map);
        if (mapFragment == null) {
            Log.wtf(TAG, "mapFragment is null :(");
        } else {
            Log.d(TAG, "Call me when map is ready");
            mapFragment.getMapAsync(this);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("gyms");
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(firebaseAuth -> {
            boolean loggedOut = firebaseAuth.getCurrentUser() == null;
            Log.d(TAG, "User has logged " + (!loggedOut ? "in" : "out"));
            setLoginVisibility(loggedOut);
        });
        setLoginVisibility(mAuth.getCurrentUser() == null);

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_open, R.string.navigation_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        setUtilsInstance(null);
    }

    private void setLoginVisibility(boolean visible) {
        Menu navigationViewMenu = mNavigationView.getMenu();
        Log.d(TAG, "Showing login view? " + visible);
        navigationViewMenu.findItem(R.id.nav_login).setVisible(visible);
        navigationViewMenu.findItem(R.id.nav_logout).setVisible(!visible);
    }

    private void addGymToMap(Gym gym) {
        LatLng latLng = new LatLng(gym.getLatitude(), gym.getLongitude());
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions().position(latLng).title(gym.getAddress()));
        }
    }

    public void setUtilsInstance(Utils utils) {
        if (utils == null) {
            mUtils = new Utils();
        } else {
            mUtils = utils;
        }
    }

    @OnClick(R.id.main_fab)
    public void onMainFabClicked(View view) {
        if (mUtils.getUserUid() == null) {
            Utils.showSnackbar(findViewById(android.R.id.content), R.string.snackbar_close, R.string.new_gym_not_logged);
            return;
        }
        NewGymDialogFragment newGymFragment = new NewGymDialogFragment();
        newGymFragment.setCallback(this);
        newGymFragment.show(getSupportFragmentManager(), TAG);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Map is ready!");
        mMap = googleMap;
        if (mLastLocation != null && !mMapAnimated) {
            mMapAnimated = true;
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_DEFAULT_ZOOM));
        }
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View markerContentView = getLayoutInflater().inflate(R.layout.map_marker, null);
                TextView tvAddress = markerContentView.findViewById(R.id.map_balloon_address);
                TextView tvEquipments = markerContentView.findViewById(R.id.map_balloon_equipments);

                LatLng latLng = marker.getPosition();
                Log.d(TAG, "Clicked on map item: (" + latLng.latitude + ", " + latLng.longitude + ")");
                Gym gym = lookForGym(latLng);
                if (gym == null) {
                    tvAddress.setText(R.string.map_marker_gym_not_found);
                } else {
                    tvAddress.setText(gym.getAddress());
                    tvEquipments.setText(getFormattedStringForEquipments(gym.getEquipmentList().size()));
                }
                marker.setTag(gym);
                Log.d(TAG, "All set to show the marker");
                return markerContentView;
            }

            private CharSequence getFormattedStringForEquipments(int numberOfEquipments) {
                String text = getString(R.string.map_marker_equipments);
                String numberText = Integer.toString(numberOfEquipments);
                SpannableStringBuilder equipmentsString = new SpannableStringBuilder();
                int startBold = 0;
                equipmentsString.append(numberText);
                equipmentsString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                        startBold, equipmentsString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                equipmentsString.append(' ');
                equipmentsString.append(text);
                return equipmentsString;
            }

            private Gym lookForGym(LatLng latLng) {
                for (Gym gym : mGymsList) {
                    if (gym.getLatitude() == latLng.latitude && gym.getLongitude() == latLng.longitude) {
                        return gym;
                    }
                }
                Log.e(TAG, "Could not find gym with (" + latLng.latitude + ", " +
                        latLng.longitude + ") between all gyms: " + mGymsList);
                return null;
            }
        });
        Log.d(TAG, "I am done here. Now I am ready to fetch data and set location");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.d(TAG, "All permission are granted");
                            mMap.setMyLocationEnabled(true);
                            mFusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(MainActivity.this, location -> {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location == null) {
                                            Utils.showSnackbar(findViewById(android.R.id.content), R.string.snackbar_close, R.string.main_location_not_found);
                                        } else {
                                            LocationHolderSingleton locationHolder = LocationHolderSingleton.getInstance();
                                            locationHolder.setLocation(location);
                                            mLastLocation = location;
                                            if (!mMapAnimated) {
                                                if (mMap != null) {
                                                    mMapAnimated = true;
                                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), MAP_DEFAULT_ZOOM));
                                                }
                                            }
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Not all permissions were granted :|");
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Log.d(TAG, "User permanently denied a permission :(");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Log.d(TAG, "Showing rationale...");
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Gyms retrieved from database: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Gym gym = postSnapshot.getValue(Gym.class);
                    Log.d(TAG, "Got gym: " + gym);
                    if (gym == null) {
                        Log.e(TAG, "Got a gym == null in my database");
                    } else {
                        addGymToMap(gym);
                        mGymsList.add(gym);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onNewGymAdded(Gym newGym) {
        LatLng latLng = new LatLng(newGym.getLatitude(), newGym.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(newGym.getAddress()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_DEFAULT_ZOOM));
        Toast.makeText(this, "Thanks for adding a new gym", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Log.d(TAG, "Login clicked");
            if (mAuth.getCurrentUser() == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else {
                Log.d(TAG, "I know you!");
            }
        } else if (id == R.id.nav_logout) {
            Log.d(TAG, "Logout clicked");
            mAuth.signOut();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Gym gym = (Gym) marker.getTag();
        if (gym == null) {
            Log.e(TAG, "Got gym = null. Weird");
        } else {
            GymParcelable gymParcelable = new GymParcelable(gym);
            Log.d(TAG, "Marker's balloon clicked: " + gym);
            Intent intent = new Intent(getApplicationContext(), GymActivity.class);
            intent.putExtra(Utils.PARAM_GYM, gymParcelable);
            startActivity(intent);
        }
    }
}
