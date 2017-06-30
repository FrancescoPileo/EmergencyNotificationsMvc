package com.univpm.cpp.emergencynotificationsmvc.controllers;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


import com.univpm.cpp.emergencynotificationsmvc.EmergencyNotificationsMvc;
import com.univpm.cpp.emergencynotificationsmvc.MainActivity;
import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.MyBluetoothManager;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.Beacon;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValues;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteDbHelper;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteUpdateTask;
import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;
import com.univpm.cpp.emergencynotificationsmvc.models.position.Position;
import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.utils.Directories;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;
import com.univpm.cpp.emergencynotificationsmvc.views.dialog.DialogView;
import com.univpm.cpp.emergencynotificationsmvc.views.dialog.DialogViewImpl;
import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeView;
import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeViewImpl;

import java.util.ArrayList;
import java.util.Date;

import static com.univpm.cpp.emergencynotificationsmvc.utils.ImageCoordinates.*;

public class HomeFragment extends Fragment implements
        HomeView.MapSpnItemSelectedViewListener,
        HomeView.LogoutBtnViewListener,
        HomeView.BeaconTouchListener,
        DialogView.OkButtonListener,
        HomeView.InfoBtnListener{

    public static final String TAG = "HOME_FRAGMENT";

    private EmergencyNotificationsMvc application;
    private HomeView mHomeView;
    private DialogView mDialogView;
    private Dialog dialog;
    private InitTask mInitTask;
    private SpinnerTask mSpinnerTask;
    private FirstMapTask mFirstMapTask;
    private MapTask mMapTask;
    private EnvValuesTask mEnvValuesTask;
    private MyBluetoothManager mMyBluetoothManager;
    private Map map;
    private User user;
    private Session session;
    private Node positionNode;  //è il nodo relativo alla posizione dell'utente
    private Position mLastposition;

    //todo commentare
    //todo visualizzare errori
    //todo (opt) gestione utente
    //todo (opt) dialogo dettagli caricamento
    //todo cosa succede se premo le notifiche
    //todo server non raggiungibile


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mHomeView = new HomeViewImpl(inflater, container, getContext());
        mDialogView = new DialogViewImpl(inflater, container, getContext());
        application = ((EmergencyNotificationsMvc) getActivity().getApplication());
        MainActivity activity = (MainActivity) getActivity();
        user = new User();
        map = new Map();
        mLastposition = new Position();
        positionNode = new Node();
        session = new Session();
        dialog = new Dialog(getContext());

        mHomeView.setMapSelectedListener(this);
        mHomeView.setLogoutListener(this);
        mHomeView.setBeaconTouchListener(this);
        mHomeView.setToolbar(this);
        mDialogView.setOkButtonListener(this);
        mHomeView.setInfoBtnListener(this);

        mMyBluetoothManager = new MyBluetoothManager(getContext(), getActivity());

        mHomeView.showProgress(true);

        mInitTask = new InitTask();
        mInitTask.execute((Void) null);

        /*LocalSQLiteUpdateTask task = new LocalSQLiteUpdateTask(application);
        task.execute((Void) null);*/

        mSpinnerTask = new SpinnerTask();
        mSpinnerTask.execute((Void) null);




        return mHomeView.getRootView();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        mHomeView.setToolbarItems(menu, inflater);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mHomeView.executeToolbarItemAction(id);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLogoutClick() {

        //termina la sessione poi fa il logout
        EndSessionTask mEndSessionTask = new EndSessionTask();
        mEndSessionTask.execute((Void) null);
    }

    @Override
    public void onBeaconClick(Beacon beacon) {
        dialog.getWindow();
        mEnvValuesTask = new EnvValuesTask(beacon);
        mEnvValuesTask.execute((Void) null);
        dialog.setContentView(mDialogView.getRootView());
        dialog.show();
    }

    @Override
    public void onOkButtonClick() {
        dialog.cancel();
    }

    @Override
    public void onInfoClick() {
        Log.w("info", "");
        Dialog infoDialog = new Dialog(getContext());
        infoDialog.getWindow();
        infoDialog.setContentView(R.layout.dialog_info);
        infoDialog.show();
    }

    private boolean started = false;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if(started) {
                start();

            }
        }
    };

    public void stop() {
        started = false;
        handler.removeCallbacks(runnable);
    }

    public void start() {
        if (application.isConnectionEnabled()) {
            mMyBluetoothManager.scanning();
        }
        mFirstMapTask = new FirstMapTask();
        mFirstMapTask.execute((Void) null);
        started = true;
        handler.postDelayed(runnable, 60000);
    }


    /* Al caricamento della home non è stato selezionato niente dallo spinner, parte FirstMapTask
     * Se si seleziona una mappa dallo spinner parte MapTask */
    @Override
    public void onMapSpnItemSelected(String nameMap) {
        mMapTask = new MapTask(nameMap);
        mMapTask.execute((Void) null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        //stop();
        super.onPause();
    }

    @Override
    public void onStop() {
        stop();
        super.onStop();
    }

    @Override
    public void onResume() {
        //start();
        super.onResume();
    }

    public class InitTask extends AsyncTask<Void, Void, Boolean> {

        InitTask() {
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            user = application.getUserModel().getUser(application.getLocalPreferences().getUser().getUsername());
            if (application.isConnectionEnabled()) {
                session = application.getSessionModel().getLastSession(user);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }
    }


    public class SpinnerTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<String> stringArrayList;

        SpinnerTask() {

            this.stringArrayList = new ArrayList<>();
            this.stringArrayList = null;
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            this.stringArrayList = application.getMapModel().getAllNames();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSpinnerTask = null;
            mHomeView.showProgress(false);
            if (success) {
                mHomeView.populateSpinner(stringArrayList);
                CheckMapsTask task = new CheckMapsTask(stringArrayList);
                task.execute((Void) null);

            }
            else {
                Log.w("Spinner", "error");
            }
        }
    }

    public class CheckMapsTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<String> stringArrayList;

        CheckMapsTask(ArrayList<String> stringArrayList) {

            this.stringArrayList = stringArrayList;
        }


        @Override
        protected Boolean doInBackground(Void... params) {


           for (String mapname:stringArrayList){
               try {
                   Bitmap bitmap = HttpUtils.getMapBitmap(mapname);
                   if (bitmap != null)
                   {
                       saveBitmap(bitmap, mapname);
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }

           return true;
        }

        private void saveBitmap(Bitmap bitmap, String name){

            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                File dir = new File(Directories.MAPS);
                if (!dir.exists())
                    dir.mkdirs();
                File file = new File(dir, name + ".png");
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            //mSpinnerTask = null;
        }
    }


    public class FirstMapTask extends AsyncTask<Void, Void, Boolean> {


        private ArrayList<Beacon> beacons;

        /* Se l'utente ha una o più posizioni, prende la più recente e la relativa mappa (in map)
         * Altrimenti in map si trova la prima mappa dello spinner */
        @Override
        protected Boolean doInBackground(Void... params) {

            beacons = application.getBeaconModel().getAllBeacons();

            Position lastPosition = application.getPositionModel().getLastPositionByUser(user);
            //Log.w("Position", lastPosition.toString());

            Boolean flag = false;

            if (lastPosition == null){
                map = application.getMapModel().getMapByName(application.getMapModel().getAllNames().get(0));
                flag = true;
            } else if (lastPosition != mLastposition) {                 //todo controllare
                positionNode = application.getNodeModel().getNodeById(lastPosition.getNode().getIdNode());
                map = application.getMapModel().getMapById(positionNode.getMap().getIdMap());
                mLastposition = lastPosition;
                flag = true;
            }

            return flag;

        }

        /* Se la posizione è stata trovata, stampa la mappa con il marker sulle coordinate
         * Altrimenti stampa la prima mappa dello spinner */
        @Override
        protected void onPostExecute(final Boolean success) {
            mSpinnerTask = null;

            Log.w("Marco", String.valueOf(success));

            if (success) {

                mHomeView.setMapOnView(map);
                mHomeView.setBeaconsOnMap(beacons, map);

                if (positionNode.getIdNode() != -1) {
                    mHomeView.setPosition(getPixelsXFromMetres(positionNode.getX(), map), getPixelsYFromMetres(positionNode.getY(), map)); //qua gli si deve passare la x e la y del nodo posizione
                    mHomeView.setPositionText("La tua posizione è in "+ map.getName() + ".");
                    mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");
                }

                else mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");
            }

            else {
                Log.w("Map", "nothing to do");
            }

        }

    }

    public class MapTask extends AsyncTask<Void, Void, Boolean> {

        private String nameMap;
        private String path;
        private Map positionMap;
        private ArrayList<Beacon> beacons;


        MapTask(String nameMap) {

            this.nameMap = nameMap;
            this.path = null;
        }


        /* Prende la mappa selezionata dallo spinner e, se esiste la posizione, salva la relativa mappa in positionMap */
        @Override
        protected Boolean doInBackground(Void... params) {

            Log.w("MapModel", application.getMapModel().toString());
            beacons = application.getBeaconModel().getAllBeacons();
            map = application.getMapModel().getMapByName(nameMap);

            if (positionNode.getIdNode() != -1) positionMap = application.getMapModel().getMapById(positionNode.getMap().getIdMap());

            return true;

        }

        /* Se la posizione esiste e la mappa scelta dallo spinner è quella relativa alla propria posizione, stampa mappa con marker
         * Se la posizione esiste e la mappa scelta dallo spinner è un'altra, stampa quella scelta senza marker
         * Se la posizione non esiste viene ovviamente stampata la mappa scelta dallo spinner */
        @Override
        protected void onPostExecute(final Boolean success) {
            mSpinnerTask = null;

            if (success) {

                if (map != null) {
                    mHomeView.setMapOnView(map);
                    mHomeView.setBeaconsOnMap(beacons, map);


                    if (positionNode.getIdNode() != -1) {

                        if (nameMap.equals(positionMap.getName())) {
                            mHomeView.setPositionText("La tua posizione è in " + positionMap.getName() + ".");
                            mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");
                            mHomeView.setPosition(getPixelsXFromMetres(positionNode.getX(), positionMap), getPixelsYFromMetres(positionNode.getY(), positionMap)); //qua gli si deve passare la x e la y del nodo posizione
                        } else {
                            mHomeView.setPositionText("La tua posizione è in " + positionMap.getName() + ".");
                            mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");
                        }

                    } else mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");
                }

            }

            else {
                Log.w("Map", "error");
            }

        }

    }

    public class EndSessionTask extends AsyncTask<Void, Void, Boolean> {


        EndSessionTask(){}

        @Override
        protected Boolean doInBackground(Void... voids) {

            if (application.isConnectionEnabled()) {
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+1"));

                session.setTimeOut(dateFormat.format(date));
                return new SessionModelImpl().updateSession(session);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success){

                application.getLocalPreferences().deleteLogin();
                application.getLocalPreferences().deleteSession();

                //carica il fragment di login
                Fragment newFragment = new LoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment, LoginFragment.TAG);
                transaction.addToBackStack(null);

                transaction.commit();
            }

            else Log.w("ErroreLogout", "non esce");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public class EnvValuesTask extends AsyncTask<Void, Void, Boolean> {

        private Beacon beacon;
        private EnviromentalValues envValues;
        boolean flag = false;

        EnvValuesTask(Beacon beacon) {
            this.beacon = beacon;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            ArrayList<EnviromentalValues> enviromentalValuesArrayList =
                    application.getEnviromentalValuesModel().getLastValuesForEachBeacon();

            if (enviromentalValuesArrayList != null) {
                for (int i = 0; i < enviromentalValuesArrayList.size(); i++) {

                    if (enviromentalValuesArrayList.get(i).getBeacon().getIdBeacon().equals(beacon.getIdBeacon())) {
                        envValues = enviromentalValuesArrayList.get(i);
                        flag = true;
                    }
                }
            }

            return flag;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mDialogView.setNodeNameText("Nodo: " + beacon.getNode().getNodename());
            mDialogView.setSuccess(success);
            if (success) {
                mDialogView.setTempValueText(String.valueOf(envValues.getTemperature()));
                mDialogView.setHumValueText(String.valueOf(envValues.getHumidity()));
                mDialogView.setAccXValueText(String.valueOf(envValues.getAccX()));
                mDialogView.setAccYValueText(String.valueOf(envValues.getAccY()));
                mDialogView.setAccZValueText(String.valueOf(envValues.getAccZ()));

                //mDialogView.setGyrValueText("X:" + String.valueOf(envValues.getGyrX()) + ", Y:" + String.valueOf(envValues.getGyrY()) + ", Z:" + String.valueOf(envValues.getGyrZ()));
            }
        }
    }


}