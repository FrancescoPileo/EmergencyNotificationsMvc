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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.controllers.bluetooth.MyBluetoothManager;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.Beacon;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModel;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValues;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModel;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValuesModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferences;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferencesImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteDbHelper;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalSQLiteUpdateTask;
import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModel;
import com.univpm.cpp.emergencynotificationsmvc.models.map.MapModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModel;
import com.univpm.cpp.emergencynotificationsmvc.models.node.NodeModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.position.Position;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.position.PositionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModel;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.utils.Directories;
import com.univpm.cpp.emergencynotificationsmvc.utils.HttpUtils;
import com.univpm.cpp.emergencynotificationsmvc.utils.ImageCoordinates;
import com.univpm.cpp.emergencynotificationsmvc.utils.TouchImageView;
import com.univpm.cpp.emergencynotificationsmvc.views.dialog.DialogView;
import com.univpm.cpp.emergencynotificationsmvc.views.dialog.DialogViewImpl;
import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeView;
import com.univpm.cpp.emergencynotificationsmvc.views.home.HomeViewImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class HomeFragment extends Fragment implements
        HomeView.MapSpnItemSelectedViewListener,
        HomeView.LogoutBtnViewListener,
        HomeView.BeaconTouchListener,
        DialogView.OkButtonListener{

    private HomeView mHomeView;
    private DialogView mDialogView;
    private Dialog dialog;
    private MapModel mMapModel;
    private BeaconModel mBeaconModel;
    private UserModel mUserModel;
    private NodeModel mNodeModel;
    private PositionModel mPositionModel;
    private SessionModel mSessionModel;
    private EnviromentalValuesModel mEnviromentalValuesModel;
    private SpinnerTask mSpinnerTask;
    private FirstMapTask mFirstMapTask;
    private InitTask mInitTask;
    private MapTask mMapTask;
    private EnvValuesTask mEnvValuesTask;
    private MyBluetoothManager mMyBluetoothManager;
    private LocalPreferences mLocalPreferences;
    private Map map;
    private User user;
    private Session session;
    private Node positionNode;  //è il nodo relativo alla posizione dell'utente
    private Position mLastposition;


    //todo dbinterno
    //todo appoffline
    //todo commentare
    //todo visualizzare errori
    //todo notifiche --> firebase
    //todo gestione utente
    //todo spesso crasha quando non ho il bluetooth acceso e mi chiede di accenderlo oppure quando entro la 1 volta come ospite
    //todo controllare criterio x accettare password in registrazione


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mHomeView = new HomeViewImpl(inflater, container, getContext());
        mDialogView = new DialogViewImpl(inflater, container, getContext());
        mMapModel = new MapModelImpl();
        mUserModel = new UserModelImpl();
        mNodeModel = new NodeModelImpl();
        mLocalPreferences = new LocalPreferencesImpl(getContext());
        mBeaconModel = new BeaconModelImpl();
        mPositionModel = new PositionModelImpl();
        mSessionModel = new SessionModelImpl();
        mEnviromentalValuesModel = new EnviromentalValuesModelImpl();
        mSpinnerTask = new SpinnerTask();
        mInitTask = new InitTask();
        user = new User();
        map = new Map();
        mLastposition = new Position();
        positionNode = new Node();
        session = new Session();
        dialog = new Dialog(getContext());


        mInitTask.execute((Void) null);
        mSpinnerTask.execute((Void) null);
        LocalSQLiteUpdateTask task = new LocalSQLiteUpdateTask(getContext());
        task.execute((Void) null);


        //todo cambiare on click
        mHomeView.setMapSelectedListener(this);
        mHomeView.setLogoutListener(this);
        mHomeView.setBeaconTouchListener(this);
        mHomeView.setToolbar(this);

        mDialogView.setOkButtonListener(this);

        mMyBluetoothManager = new MyBluetoothManager(getContext(), getActivity());
        //mMyBluetoothManager.scanning();
        start();

        return mHomeView.getRootView();
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
        mMyBluetoothManager.scanning();
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


        InitTask(){}

        @Override
        protected Boolean doInBackground(Void... voids) {

            user = mUserModel.getUser(mLocalPreferences.getUsername());
            session = mSessionModel.getLastSession(user);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success){
            }

            else Log.w("Errore", "ErroreInitTask");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
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

            this.stringArrayList = mMapModel.getAllNames();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSpinnerTask = null;

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
            mSpinnerTask = null;


        }
    }


    public class FirstMapTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<Beacon> beacons;

        /* Se l'utente ha una o più posizioni, prende la più recente e la relativa mappa (in map)
         * Altrimenti in map si trova la prima mappa dello spinner */
        @Override
        protected Boolean doInBackground(Void... params) {

            beacons = mBeaconModel.getAllBeacons();

            Position lastPosition = mPositionModel.getLastPositionByUser(user);

            Boolean flag = false;

            if (lastPosition == null){
                map = mMapModel.getMapByName(mHomeView.getMap());
                flag = true;
            } else if (lastPosition != mLastposition) {                 //todo controllare
                positionNode = mNodeModel.getNodeById(lastPosition.getNode().getIdNode());
                map = mMapModel.getMapById(positionNode.getMap().getIdMap());
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
                    mHomeView.setPosition(ImageCoordinates.getPixelsXFromMetres(positionNode.getX(), map), ImageCoordinates.getPixelsYFromMetres(positionNode.getY(), map));
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

            map = mMapModel.getMapByName(nameMap);
            beacons = mBeaconModel.getAllBeacons();

            if (positionNode.getIdNode() != -1) positionMap = mMapModel.getMapById(positionNode.getMap().getIdMap());

            return true;

        }

        /* Se la posizione esiste e la mappa scelta dallo spinner è quella relativa alla propria posizione, stampa mappa con marker
         * Se la posizione esiste e la mappa scelta dallo spinner è un'altra, stampa quella scelta senza marker
         * Se la posizione non esiste viene ovviamente stampata la mappa scelta dallo spinner */
        @Override
        protected void onPostExecute(final Boolean success) {
            mSpinnerTask = null;

            if (success) {

                mHomeView.setMapOnView(map);
                mHomeView.setBeaconsOnMap(beacons, map);

                if (positionNode.getIdNode() != -1) {

                    if (nameMap.equals(positionMap.getName())) {
                        mHomeView.setPositionText("La tua posizione è in "+ positionMap.getName() + ".");
                        mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");
                        mHomeView.setPosition(ImageCoordinates.getPixelsXFromMetres(positionNode.getX(), positionMap), ImageCoordinates.getPixelsYFromMetres(positionNode.getY(), positionMap)); //qua gli si deve passare la x e la y del nodo posizione
                    }

                    else {
                        mHomeView.setPositionText("La tua posizione è in "+ positionMap.getName() + ".");
                        mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");
                    }

                }

                else mHomeView.setMapName("La mappa visualizzata è " + map.getName() + ".");

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

            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+1"));
            session.setTimeOut(dateFormat.format(date));
            mSessionModel = new SessionModelImpl();
            return mSessionModel.updateSession(session);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success){

                mLocalPreferences.deleteLogin();
                mLocalPreferences.deleteSession();

                //carica il fragment di login
                Fragment newFragment = new LoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
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

            ArrayList<EnviromentalValues> enviromentalValuesArrayList = mEnviromentalValuesModel.getLastValuesForEachBeacon();

            for (int i = 0; i < enviromentalValuesArrayList.size(); i++) {

                if (enviromentalValuesArrayList.get(i).getBeacon().getIdBeacon().equals(beacon.getIdBeacon())) {
                    envValues = enviromentalValuesArrayList.get(i);
                    flag = true;
                }
            }

            return flag;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                mDialogView.setNodeNameText("Nodo: " + beacon.getNode().getNodename());
                mDialogView.setTempVisible();
                mDialogView.setHumVisible();
                mDialogView.setAccVisible();
                mDialogView.setGyrVisible();
                mDialogView.setTempValueText(String.valueOf(envValues.getTemperature()));
                mDialogView.setHumValueText(String.valueOf(envValues.getHumidity()));
                mDialogView.setAccValueText(String.valueOf(envValues.getAccX()) + ", " + String.valueOf(envValues.getAccY()) + ", " + String.valueOf(envValues.getAccZ()));
                mDialogView.setGyrValueText(String.valueOf(envValues.getGyrX()) + ", " + String.valueOf(envValues.getGyrY()) + ", " + String.valueOf(envValues.getGyrZ()));

            }

            else {
                mDialogView.setNodeNameText("Dati ambientali non disponibili.");
                mDialogView.setTempInvisible();
                mDialogView.setHumInvisible();
                mDialogView.setAccInvisible();
                mDialogView.setGyrInvisible();

            }

        }

    }


}