package com.example.proyectoinventario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityAccederInventarioBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationBarView;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;





//Implementación para mapa google
//OnMapReadyCallback


//Código del manifest para mapa google
/*
<meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="TU_CLAVE_DE_API_DE_GOOGLE_MAPS" />

 */

public class AccederInventarioActivity extends AppCompatActivity implements LocationListener {


    ActivityAccederInventarioBinding binding;

    ArrayAdapter<String> adapter;

    ArrayList<String> lvSuper;
    ArrayList<String> lvSuperPrin;

    ArrayList<Super> supers;
    ArrayList<Super> supersPrin;
    int idSelect;


    static int touch;




    MapView mMapView = null;
    CompassOverlay mCompassOverlay = null;
    RotationGestureOverlay rotationGestureOverlay = null;
    ScaleBarOverlay mScaleBarOverlay = null;
    DisplayMetrics dm = null;
    MinimapOverlay mMinimapOverlay = null;
    MapController mapController;


    final static int rC_SOLICITAR = 1;
    boolean perdirPermisos = true;
    Location pos;
    LocationManager locationManager;



    ArrayList<OverlayItem> puntos = new ArrayList<>();
    ArrayList<GeoPoint> points = new ArrayList<>();
    ArrayList<Super> superPoints = new ArrayList<>();
    boolean ubicando = true;





    //private GoogleMap mMap;


    public void cambiarFecha(String s){
        binding.editTextFecha.setText(s);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccederInventarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent it = new Intent(this, InventarioActivity.class);
        Intent itMain = new Intent(this, MainActivity.class);



        //Mapa google
        /*
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

         */



        binding.editTextFecha.setKeyListener(null);
        touch = 0;

        mMapView = binding.mapView;
        mMapView.setTileSource(TileSourceFactory.MAPNIK);

        // Se añaden los botones de zoom
        mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        //Se añade la posibilidad de hacer zoom con dos dedos: multi-touch
        mMapView.setMultiTouchControls(true);
        // Centramos el mapa en un punto y establecemos el zoom por defecto
        mapController =(MapController)mMapView.getController();
        // Se centra el mapa en el punto creado torreEiffel

        // Se estable el zoom por defecto del mapa
        mapController.setZoom(16.5);


        // Permite dibujar los tiles del mapa. Se necesitan los permisos necesarios
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mMapView);
        mLocationOverlay.enableMyLocation();
        mMapView.getOverlays().add(mLocationOverlay);





        Cursor cMap = db.rawQuery("SELECT * FROM "+Contract.SuperEntry.TABLE_NAME, null);



        while (cMap.moveToNext()){
            @SuppressLint("Range") double coorX = cMap.getDouble(cMap.getColumnIndex(Contract.SuperEntry.COORDX));
            @SuppressLint("Range") double coorY = cMap.getDouble(cMap.getColumnIndex(Contract.SuperEntry.COORSY));
            @SuppressLint("Range") String name = cMap.getString(cMap.getColumnIndex(Contract.SuperEntry.NOMBRE));
            @SuppressLint("Range") String dir = cMap.getString(cMap.getColumnIndex(Contract.SuperEntry.DIRECCION));

            if (coorX != 0.0 && coorY != 0.0){
                GeoPoint geoPoint = new GeoPoint(coorX, coorY);
                points.add(geoPoint);

                Super s = new Super();
                s.setNombre(name);
                s.setDireccion(dir);
                superPoints.add(s);
            }
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String mensaje = "Gps: " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) + "\n" + "Network: " + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();


        actualizaPosicion();




        lvSuper = new ArrayList<>();
        lvSuperPrin = new ArrayList<>();

        supersPrin = new ArrayList<>();
        supers = new ArrayList<>();

        Cursor cSuper = db.rawQuery("SELECT * FROM "+Contract.SuperEntry.TABLE_NAME, null);

        while (cSuper.moveToNext()){
            @SuppressLint("Range") int id = cSuper.getInt(cSuper.getColumnIndex(Contract.SuperEntry.ID));
            @SuppressLint("Range") String name = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.NOMBRE));
            @SuppressLint("Range") double coorX = cSuper.getDouble(cSuper.getColumnIndex(Contract.SuperEntry.COORDX));
            @SuppressLint("Range") double coorY = cSuper.getDouble(cSuper.getColumnIndex(Contract.SuperEntry.COORSY));
            @SuppressLint("Range") String dir = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.DIRECCION));
            @SuppressLint("Range") String prov = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.PROVINCIA));
            @SuppressLint("Range") String ciudad = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.CIUDAD));
            @SuppressLint("Range") String frec = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.FRECUENCIA));
            @SuppressLint("Range") int codClient = cSuper.getInt(cSuper.getColumnIndex(Contract.SuperEntry.COD_CLIENTE));
            @SuppressLint("Range") String pers = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.PERSONA_CONTACTO));
            @SuppressLint("Range") String email = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.EMAIL));
            @SuppressLint("Range") int num = cSuper.getInt(cSuper.getColumnIndex(Contract.SuperEntry.TELEFONO));
            @SuppressLint("Range") int visAno = cSuper.getInt(cSuper.getColumnIndex(Contract.SuperEntry.VISITAS_ANO));
            @SuppressLint("Range") String obs = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.OBSERVACIONES));


            Super s = new Super();
            s.setId(id);
            s.setNombre(name);
            s.setCoorX(coorX);
            s.setCoorY(coorY);
            s.setLocalizacion(coorX+","+coorY);
            s.setDireccion(dir);
            s.setProvincia(prov);
            s.setCiudad(ciudad);
            s.setFrecuencia(frec);
            s.setCodCliente(codClient);
            s.setPersonaContacto(pers);
            s.setEmail(email);
            s.setTelefono(num);
            s.setVisitasAno(visAno);
            s.setObservaciones(obs);

            supersPrin.add(s);
            lvSuperPrin.add(name);
        }

        supers.addAll(supersPrin);
        lvSuper.addAll(lvSuperPrin);


        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, lvSuper);
        binding.spinnerSuper.setAdapter(adapter);


        Calendar cal = Calendar.getInstance();
        String dateS = cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR);
        binding.editTextFecha.setHint(dateS);



        binding.editTextFecha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                touch++;

                if (touch==1){
                    DialogoFecha dialogoFecha= new DialogoFecha();
                    dialogoFecha.show(getSupportFragmentManager (), "Fecha");
                }
                return false;
            }
        });




        binding.spinnerSuper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idSelect = supers.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<String> lvSuperNew = new ArrayList<>();
                ArrayList<Super> supersNew = new ArrayList<>();

                for (int i = 0; i < lvSuperPrin.size(); i++) {

                    if (lvSuperPrin.get(i).toUpperCase().startsWith(newText.toUpperCase())){

                        lvSuperNew.add(lvSuperPrin.get(i));
                        supersNew.add(supersPrin.get(i));
                    }
                }

                supers.clear();;
                supers.addAll(supersNew);
                idSelect = supers.get(0).getId();

                lvSuper.clear();
                lvSuper.addAll(lvSuperNew);
                adapter.notifyDataSetChanged();
                return false;
            }
        });







        Cursor cUser = db.rawQuery("SELECT * FROM "+Contract.Usuario.TABLE_NAME+" WHERE "+Contract.Usuario.LOGEADO+" = "+1,null);
        cUser.moveToFirst();
        @SuppressLint("Range") int idUser = cUser.getInt(cUser.getColumnIndex(Contract.Usuario.ID));



        Menu menu = binding.include30.bottomNavView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.id_perfil);
        menuItem.setIcon(R.drawable.ic_start);
        menuItem.setTitle("Iniciar");
        invalidateOptionsMenu();

        binding.include30.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.id_home:
                        startActivity(itMain);
                        return true;
                    case R.id.id_perfil:

                        LocalDateTime locaDate = LocalDateTime.now();
                        int hours  = locaDate.getHour();
                        int minutes = locaDate.getMinute();
                        int seconds = locaDate.getSecond();

                        String horaInicio = hours+":"+minutes+":"+seconds;
                        String fecha = binding.editTextFecha.getText().toString();


                        Inventario inventario = new Inventario();
                        inventario.setId_super(idSelect);
                        inventario.setFecha(fecha);
                        inventario.setIncio(horaInicio);
                        inventario.setPendiente(1);
                        inventario.setIdUser(idUser);

                        it.putExtra("MOD", false);
                        it.putExtra("IDSUPER", idSelect);
                        it.putExtra("CLASEINVENTARIO", inventario);
                        startActivity(it);
                        return true;
                }

                return false;
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (perdirPermisos
                && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
        ) {
            // En caso de no tener los permisos se solicita al usuario su concesión
            String[] permisos = new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            };
            ActivityCompat.requestPermissions(AccederInventarioActivity.this, permisos, rC_SOLICITAR);
        } else {
            // Si ya se tienen los permisos se ejecuta de forma normal
            Criteria criteria = new Criteria(); // Se establece el criterio de precisión
            criteria.setAccuracy(Criteria.ACCURACY_COARSE); // gps + network
            //criteria.setAccuracy(Criteria.ACCURACY_FINE); // solo gps
            List<String> proveedores = locationManager.getProviders(criteria, true);

            // Si no se encuentran proveedores de localización
            if (proveedores.isEmpty()) {


            }else {
                // Se realiza una actualización de estado para cada proveedor.
                // El parámetro looper (tercer parámetro de requestSingleUpdate)
                // a null indica los métodos callback están en el hilo que ejecuta la llamada

                for (String proveedorActivo : proveedores) {
                    locationManager.requestLocationUpdates(proveedorActivo, 0, 0, this, null);
                    // Se solicita una nueva ubicación
                    // locationManager.requestSingleUpdate(proveedorActivo, this, null);
                    // Parámetros -> 1: String proveedor 2: tiempo Mínimo Actu
                    // 3: Distancia Mínima Actu. 4: listener 5: looper

                }
            }
        }

        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Se comprueba que se viene del dialogo que solicita permisos de localización
        if (requestCode == rC_SOLICITAR) {

            // permiso concedido
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(getApplicationContext(), "Permiso concedido",
                        Toast.LENGTH_LONG).show();
        } else {
            // Se ha rechazado el permiso
            perdirPermisos=false; // No solicito más los permisos
        }
    }


    public void actualizarDatos(Location location){

        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date(location.getTime()));
    }


    // Actualiza la posición en el mapa y dibuja elementos auxiliares del mismo
    public void actualizaPosicion(){
        if (pos!=null) {
            // Nuevo punto usando la posición actual
            GeoPoint aux = new GeoPoint(pos.getLatitude(), pos.getLongitude());

            // Centro el mapa en esta posición
            if (ubicando){
                mapController.setCenter(aux);
                ubicando = false;
            }

            // Se eliminan los marcadores previos para que no se visualicen
            mMapView.getOverlays().clear();
            // Se añaden elementos auxiliares al mapa: compas, escala, minimap, ....
            addElementosMapa();


            puntos.clear();

            for (int i = 0; i < points.size(); i++) {

                OverlayItem marcador = new OverlayItem(superPoints.get(i).getNombre(),superPoints.get(i).getDireccion(),points.get(i));
               // marcador.setMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.center, null));
                puntos.add(marcador);

            }

            // Añado la posición actual
            puntos.add(new OverlayItem("Mi posición", pos.getLatitude()+","+
                    pos.getLongitude(), aux));
            ItemizedOverlayWithFocus<OverlayItem> capas = new
                    ItemizedOverlayWithFocus<OverlayItem>( puntos,
                    new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                        @Override
                        public boolean onItemSingleTapUp(final int index, final OverlayItem item){

                            return true;
                        }
                        @Override
                        public boolean onItemLongPress(final int index, final OverlayItem item) {

                            return false;
                        }
                    }, this);

            capas.setFocusItemsOnTap(true);
            // añado la capa con los puntos
            mMapView.getOverlays().add(capas);
        }
    }

    public void addElementosMapa(){

            // Visualiza una brújula en la zona superor izquierda

        if (mCompassOverlay == null){
            mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), mMapView);
            mCompassOverlay.enableCompass();
            mMapView.getOverlays().add(mCompassOverlay);
        }else{
            if (!mCompassOverlay.isEnabled()){
                mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), mMapView);
                mCompassOverlay.enableCompass();
                mMapView.getOverlays().add(mCompassOverlay);
            }
        }




            // Habilita gesto de rotación con los dedos
            rotationGestureOverlay = new RotationGestureOverlay(this, mMapView);
            rotationGestureOverlay.setEnabled(true);
            mMapView.setMultiTouchControls(true);
            mMapView.getOverlays().add(rotationGestureOverlay);




            // Muestra en la parte superior un marcador que indica la escala de mapa
            mScaleBarOverlay = new ScaleBarOverlay(mMapView);
            mScaleBarOverlay.setCentred(true);



            dm = getResources().getDisplayMetrics();



            // Play around with these values to get the location on screen in the right place
            // for your application
            mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
            mMapView.getOverlays().add(mScaleBarOverlay);




            if (mMinimapOverlay==null){
                //How to add the built-in Minimap
                mMinimapOverlay = new MinimapOverlay(this, mMapView.getTileRequestCompleteHandler());
                mMinimapOverlay.setWidth(dm.widthPixels / 5);
                mMinimapOverlay.setHeight(dm. heightPixels / 5);
                //Optionally, you can set the minimap to a different tile source
                //mMinimapOverlay.setTileSource(....);
                mMapView.getOverlays().add(mMinimapOverlay);
            }else{
                if (!mMinimapOverlay.isEnabled()){
                    //How to add the built-in Minimap
                    mMinimapOverlay = new MinimapOverlay(this, mMapView.getTileRequestCompleteHandler());
                    mMinimapOverlay.setWidth(dm.widthPixels / 5);
                    mMinimapOverlay.setHeight(dm. heightPixels / 5);
                    //Optionally, you can set the minimap to a different tile source
                    //mMinimapOverlay.setTileSource(....);
                    mMapView.getOverlays().add(mMinimapOverlay);
                }
            }




    }



    @Override
    public void onLocationChanged(@NonNull Location location) {

        // actualizo el objeto posición para usarse con el botón de lanzar el mapa
        this.pos=location;
        // Actualiza el contenido de los TextEdits con los datos de location
        actualizarDatos(location);
        // Actualiza la posición en el mapa y dibuja elementos auxiliares del mismo
        actualizaPosicion();


    }

    @Override
    public void onProviderEnabled(@NonNull String s) {

        Toast.makeText(this, "Proveedor habilitado " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(@NonNull String s) {

        Toast.makeText(this, "Proveedor deshabilitado " + s, Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onBackPressed() {
        Intent itMain = new Intent(this, MainActivity.class);
        startActivity(itMain);
    }



    //Mapa google

    /*
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

     */




    //Mapa google

    /*

     <fragment
    android:id="@+id/map"
    android:name="com.google.android.gms.maps.SupportMapFragment"
    android:layout_width="match_parent"
    android:layout_height="450dp"
    android:layout_marginStart="5dp"
    android:layout_marginTop="10dp"
    android:layout_marginEnd="5dp"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toBottomOf="@id/editTextFecha"
    app:layout_constraintBottom_toTopOf="@id/include30"
    map:cameraZoom="10"
    map:uiRotateGestures="true"
    map:uiZoomControls="true" />

     */
}