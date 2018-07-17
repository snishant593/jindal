package com.aequmindia.mdm;

import android.*;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import Interface.Appinstall;
import Interface.GettingApi;
import Interface.LoginInterface;
import Pojo.Login;
import Utility.ApiClient;
import Utility.Config;
import Utility.ConfigItems;
import Utility.Validation;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.admin.DevicePolicyManager.EXTRA_DEVICE_ADMIN;

public class loginactivity extends AppCompatActivity implements View.OnClickListener, Callback<ConfigItems> {

    private static final int ADMIN_INTENT = 1;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;


    String JSON_STRING, INSERT, constraint, id;
    SQLiteStatement insertStmt;
    SQLiteDatabase myDataBase;

    private static final String TAG = loginactivity.class.getSimpleName();
    private EditText user,password;
    Button login;
     String username,user_pass;
    private boolean loggedIn = false;



    private ArrayList<Config> data_Employee_Registration;
    private ArrayList<String> field_Employee_Registration;
    private ArrayList list_Employee_Registration;
    private ArrayList<String> constraint_Employee_Registration;
    private ArrayList<Config> data_Employee_Mobile;
    private ArrayList<String> field_Employee_Mobile;
    private ArrayList<String> constraint_Employee_Mobile;
    private ArrayList list_Employee_Mobile;

    String TABLE_Employee_Registration = "Employee_Team1";
    String TABLE_Employee_Mobile = "Internet_Service_Off";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        checkPermissions();




        user = (EditText)findViewById(R.id.editText);

        login = (Button)findViewById(R.id.login);
        password = (EditText)findViewById(R.id.txtPassword);
        login.setOnClickListener(this);


        field_Employee_Registration = new ArrayList<>();
        constraint_Employee_Registration = new ArrayList<>();
        data_Employee_Registration = new ArrayList<>();


        list_Employee_Registration = new ArrayList<>();

        field_Employee_Mobile = new ArrayList<>();
        constraint_Employee_Mobile = new ArrayList<>();
        data_Employee_Mobile= new ArrayList<>();


        list_Employee_Mobile = new ArrayList<>();


            mDevicePolicyManager = (DevicePolicyManager) getSystemService(
                    Context.DEVICE_POLICY_SERVICE);
            mComponentName = new ComponentName(this, Admin.class);
             getJSON();
    }

//    public void dialog() {
//        AlertDialog alertDialog = new AlertDialog.Builder(loginactivity.this).create();
//        alertDialog.setTitle("Congratulations");
//        alertDialog.setMessage("Thanks for Activating MDM");
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(intent);
//                    }
//                });
//        alertDialog.show();
//    }


    public void jsonparsed(String input) {
        try {
            JSONObject jsonObject_emp_reg = new JSONObject(input);
            JSONArray jsonArray_emp_reg = jsonObject_emp_reg.getJSONArray("Employee_Team1");
            for (int i = 0; i < jsonArray_emp_reg.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray_emp_reg.get(i);
                id = obj.getString("Field");
                field_Employee_Registration.add(id);
            }

            JSONArray constraint_emp_reg = jsonObject_emp_reg.getJSONArray("Employee_Team1_pk");
            for (int i = 0; i < constraint_emp_reg.length(); i++) {
                JSONObject obj86 = (JSONObject) constraint_emp_reg.get(i);
                constraint = obj86.getString("Constraint");
                constraint_Employee_Registration.add(constraint);

            }

            createDynamicDatabase(loginactivity.this, TABLE_Employee_Registration, field_Employee_Registration, constraint_Employee_Registration);

        } catch (Exception e) {
            Log.e("exception", e.toString());
        }


//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!INTERNET_OFF TABLE!!!!!!!!!!!!!!!!!!!!!!!!!!!
        try {
            JSONObject jsonObject_emp_mobile = new JSONObject(input);
            JSONArray jsonArray_emp_mobile = jsonObject_emp_mobile.getJSONArray("Internet_Service_Off");
            for (int i = 0; i < jsonArray_emp_mobile.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray_emp_mobile.get(i);
                id = obj.getString("Field");
                field_Employee_Mobile.add(id);
            }

            JSONArray constraint_emp_mobile = jsonObject_emp_mobile.getJSONArray("Internet_Service_Off_pk");
            for (int i = 0; i < constraint_emp_mobile.length(); i++) {
                JSONObject obj86 = (JSONObject) constraint_emp_mobile.get(i);
                constraint = obj86.getString("Constraint");
                constraint_Employee_Mobile.add(constraint);

            }

            createDynamicDatabase(loginactivity.this, TABLE_Employee_Mobile, field_Employee_Mobile, constraint_Employee_Mobile);

        } catch (Exception e) {
            Log.e("exception", e.toString());
        }





    }



    public void createDynamicDatabase(Context context, String tableName, ArrayList<String> title, ArrayList<String> constraint) {
        Log.i("INSIDE LoginDatabase", "****creatLoginDatabase*****");
        try {
            int i;
            String querryString;
            File db_path = new File("/data/data/" + loginactivity.class.getPackage().getName() + "/databases/Db");
            db_path.getParentFile().mkdirs();
            myDataBase = SQLiteDatabase.openOrCreateDatabase(db_path, null);          //Opens database in writable mode.


            querryString = title.get(0) + " NVARCHAR(30),";
            Log.d("**createDynamicDatabase", "in oncreate");
            for (i = 1; i < title.size() - 1; i++) {
                querryString += title.get(i);
                querryString += " NVARCHAR(30) ";
                querryString += ",";
            }
            String constraintString = constraint.toString().replace("[", "(").replace("]", ")");
            querryString += title.get(i) + " NVARCHAR(30) ";

            if (constraint.size() == 0) {


                querryString = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + querryString + ");";
                System.out.println("Create Table Stmt : " + querryString);
                myDataBase.execSQL(querryString);


            } else {

                querryString = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + querryString + ", CONSTRAINT store_pk PRIMARY KEY " + constraintString + " );";
                System.out.println("Create Table Stmt : " + querryString);
                myDataBase.execSQL(querryString);


            }


            myDataBase.close();
        } catch (SQLException ex) {
            Log.i("xyz CreateDB Exception ", ex.getMessage());
        }
    }


    public void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                Log.e(TAG, "TableFunctioncalled");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // loading.dismiss();
                JSON_STRING = s;
                jsonparsed(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                JSONParserSync rh = new JSONParserSync();
                String s = rh.sendGetRequest("http://35.194.196.229:8080/JindalApi/Employee_Struct.jsp");
                // String s = rh.sendGetRequest("http://35.194.196.229:8080/Download/Employee_Struct.jsp");
                Log.e("@@@###", "TableFunctionApiCalled");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    int insertion_index=0;
    public void insert(Context context, ArrayList<String> array_vals, String TABLE_NAME, int row_index) {
        Log.d("Inside Insert", "Insertion starts for table name: " + TABLE_NAME);
        String querryString;
        File db_path = new File("/data/data/" + loginactivity.class.getPackage().getName() + "/databases/Db");
        db_path.getParentFile().mkdirs();
        myDataBase = SQLiteDatabase.openOrCreateDatabase(db_path, null);          //Opens database in writable mode.

        //Opens database in writable mode.
        String markString = array_vals.toString().replace("[","").replace("]","");
        Log.d("**createDynamicDatabase", "in oncreate");

        INSERT = "INSERT OR REPLACE INTO " + TABLE_NAME  + " values " + markString ;
        Log.d("&&&&&&&",INSERT);

        insertion_index++;

        this.insertStmt = this.myDataBase.compileStatement(INSERT);
        insertStmt.executeInsert();

        myDataBase.close();

        int flagger = 1;


    }

    @Override
    public void onResume () {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, this.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if (loggedIn) {
            //We will start the Profile Activity
           Intent intent = new Intent(getApplicationContext(), MainActivity.class);
           startActivity(intent);

            finish();
//            Toast.makeText(getActivity(),"Press again to exit",Toast.LENGTH_SHORT);
        }
    }



    @Override
    public void onClick(View v) {



        switch (v.getId()) {

            case R.id.login:


                password.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        Validation.hasText(password);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                if (user.getText().toString().equalsIgnoreCase("") || password.getText().toString().equalsIgnoreCase("")) {



                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Fill all the field", Toast.LENGTH_LONG);
                    toast.show();
                } else  {

                    username = user.getText().toString();
                    user_pass = password.getText().toString();

                    registrationProcessWithRetrofit(username,user_pass);


                }
                break;
        }

    }

    @Override
    public void onResponse(Call<ConfigItems> call, Response<ConfigItems> response) {

        try {
            data_Employee_Registration.addAll(response.body().Employee_Team1);
            list_Employee_Registration = new ArrayList<>();
            list_Employee_Registration.size();
            String d;
            int rowindex = data_Employee_Registration.size()/field_Employee_Registration.size();
            for (int k = 0; k < data_Employee_Registration.size() - 1; k+=(field_Employee_Registration.size()*rowindex)+1) {
                for (int i = k; i < field_Employee_Registration.size() * rowindex; i++)
                {
                    d = (data_Employee_Registration.get(i).toString());
                    list_Employee_Registration.add(d);
                }
                insert(loginactivity.this,list_Employee_Registration, TABLE_Employee_Registration, 5);
            }
        } catch (Exception e) {
            Log.e("Exception in Emp_Team", e.toString());
        }

    }

    @Override
    public void onFailure(Call<ConfigItems> call, Throwable t) {

    }


    public void downloadata(String email){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).readTimeout(120,TimeUnit.SECONDS).build();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl("http://35.194.196.229:8080/")

                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // prepare call in Retrofit 2.0
        GettingApi stackOverflowAPI = retrofit.create(GettingApi.class);

        Call<ConfigItems> call1 = stackOverflowAPI.load(email);
        call1.enqueue(loginactivity.this);


//        loading = new ProgressDialog(loginactivity.this);
//        loading.setMessage("Thanks for Being Patient...");
//        loading.setIndeterminate(false);
//        loading.setCancelable(false);
//        loading.show();

    }



    public void registrationProcessWithRetrofit( final String user,String password){
       LoginInterface apiService =
                ApiClient.getClient().create(LoginInterface.class);
        Call<Login> mService = apiService.registration(username, user_pass);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                response.body();

                String username = response.body().getLogin_by();
                String success = response.body().getSuccess();
                String mobileno = response.body().getMobile();
            try
            {

                if (success.equalsIgnoreCase("1"))
                {



                    SharedPreferences sharedPreferences = getApplication().getSharedPreferences(Config.SHARED_PREF_NAME, getApplicationContext().MODE_PRIVATE);

                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Adding values to editor
                    editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                    editor.putString(Config.SHARED_PREF_USERNAME,username);
                    editor.putString(Config.SHARED_PREF_MOBILE,mobileno);



                    editor.commit();







                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(EXTRA_DEVICE_ADMIN, mComponentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Administrator description");
                    startActivityForResult(intent, ADMIN_INTENT);
                    downloadata(username);





                } else {
                    //If the server response is not success
                    //Displaying an error message on toast
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                    mDevicePolicyManager.removeActiveAdmin(mComponentName);
                }

            }
            catch(Exception ex)
            {ex.printStackTrace();
            }
        }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                Toast.makeText(loginactivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }



    private void checkPermissions(){
        PermissionsUtil.askPermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsUtil.PERMISSION_ALL: {

                if (grantResults.length > 0) {

                    List<Integer> indexesOfPermissionsNeededToShow = new ArrayList<>();

                    for(int i = 0; i < permissions.length; ++i) {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            indexesOfPermissionsNeededToShow.add(i);
                        }
                    }

                    int size = indexesOfPermissionsNeededToShow.size();
                    if(size != 0) {
                        int i = 0;
                        boolean isPermissionGranted = true;

                        while(i < size && isPermissionGranted) {
                            isPermissionGranted = grantResults[indexesOfPermissionsNeededToShow.get(i)]
                                    == PackageManager.PERMISSION_GRANTED;
                            i++;
                        }

                        if(!isPermissionGranted) {

                            showDialogNotCancelable("Permissions mandatory",
                                    "All the permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            checkPermissions();
                                        }
                                    });
                        }
                    }
                }
            }
        }
    }

    private void showDialogNotCancelable(String title, String message,
                                         DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create()
                .show();
    }




    public void InstallProcessWithRetrofit()
    {



        Appinstall apiService =
                ApiClient.getClient().create(Appinstall.class);
        Call<Login> mService = apiService.InstallUpload(username,currenttime(),"Y");
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                response.body();

//              Log.e("App uninstall",nishant);
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                //   Toast.makeText(loginactivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }




    public String currenttime()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        String localTime = date.format(currentLocalTime);
        Log.e("Current time",localTime);
        return localTime;


    }




}



