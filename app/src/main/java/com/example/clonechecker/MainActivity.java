package com.example.clonechecker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String deviceName = Build.MODEL;
    String deviceMan = Build.MANUFACTURER;

    ProgressDialog progressDialog;
    EditText editText;
    String deviceID = Build.DEVICE;
    String imei;

    private LinearLayout lbrand, lmanufacturer, lime, loperator, lid, lserial_no, lmodel, num_layout;
    private  TextView tvManufacturer, tvNetworkOperator, tvIsroaming, tvBrand, phoneNumber;

    private TextView tVModel;
    private  TextView manufacturer;
    private  TextView ime, serial, id;
    private Button btnGetDetails;
    private String phoneUmber;
    private String countryNetworkIso;
    private String networkOperator;
    private String simOperatorName;
    private boolean isRoaming;
    private String stringIMEI;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CloneChecker);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait!");
        progressDialog.setMessage("Checking...");
        progressDialog.setCancelable(false);



        tVModel = findViewById(R.id.model);
        manufacturer = findViewById(R.id.manufacturer);
        ime = findViewById(R.id.ime);
        serial = findViewById(R.id.serial);
        tvManufacturer = findViewById(R.id.manufacturer);
        phoneNumber = findViewById(R.id.number);
        tvNetworkOperator = findViewById(R.id.network_operator);
        tvIsroaming = findViewById(R.id.isroaming);
        btnGetDetails = findViewById(R.id.get_details);
        editText = findViewById(R.id.edIMEI);
        lime = findViewById(R.id.ime_layout);
        lmanufacturer = findViewById(R.id.manufacturer_layout);
        loperator = findViewById(R.id.operator_layout);
        lserial_no = findViewById(R.id.serial_layout);
        lmodel = findViewById(R.id.model_layout);
        num_layout = findViewById(R.id.number_layout);





        btnGetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetails();
                btnGetDetails.setText("Verify Originality");
                editText.setVisibility(View.VISIBLE);
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_IMS_SERVICE);
//            String stringIME = telephonyManager.getImei();
//            String subscriberId = telephonyManager.getNetworkOperator();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDetails() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        stringIMEI = telephonyManager.getImei();
        phoneUmber = telephonyManager.getLine1Number();
        countryNetworkIso = telephonyManager.getNetworkCountryIso();
        networkOperator = telephonyManager.getNetworkOperator();
        simOperatorName = telephonyManager.getSimOperatorName();
        isRoaming = telephonyManager.isNetworkRoaming();


        editText.setText(stringIMEI);

        btnGetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letCheck();
                editText.setVisibility(View.GONE);
                btnGetDetails.setText("Your Device Info");
            }
        });


    }


    public void letCheck(){
        if (checkInternet()) {
            String myIMEI = editText.getText().toString();
            if (myIMEI.length() == 15) {
                imei = myIMEI;
                CloneChecker cloneChecker = new CloneChecker(MainActivity.this, imei);
                progressDialog.show();
                timerDelayRemoveDialog(3000, progressDialog);
                cloneChecker.onFinish(new CloneChecker.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(boolean okay) {
                        progressDialog.dismiss();
                        if (okay) {
                            Real();
                        } else {
                            Clone();
                        }
                    }

                    @Override
                    public void onError() {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Error!\nPlease try again!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please input valid IMEI\nIMEI is a 15 digits number!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void Real(){
        final String brand = "Brand: "+Build.MANUFACTURER+"\n";
        final String model = "Model: "+Build.MODEL+"\n";
        final String version = "Version: "+Build.VERSION.RELEASE+"\n";
        final String imei = "IMEI: "+this.imei+"\n";
        String ok = "Your phone is ORIGINAL.";

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage(brand+model+version+imei+ok)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                })
                .setPositiveButton("More", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lmodel.setVisibility(View.VISIBLE);
                        lmanufacturer.setVisibility(View.VISIBLE);
                        lime.setVisibility(View.VISIBLE);
                        loperator.setVisibility(View.VISIBLE);
                        lserial_no.setVisibility(View.VISIBLE);
                        num_layout.setVisibility(View.VISIBLE);


                        tVModel.setText(deviceName);
                        tvManufacturer.setText(deviceMan);
                        tvNetworkOperator.setText(simOperatorName);
                        phoneNumber.setText(phoneUmber);
                        ime.setText(stringIMEI);
//                        id.setText(deviceID);
                        serial.setText(Build.SERIAL);

                        btnGetDetails.setEnabled(false);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                })
                .setNegativeButton("Copy Info", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboardManager.setText(brand+model+version+imei);
                        if (clipboardManager.hasText()){
                            Toast.makeText(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void Clone(){
        final String brand = "Brand: "+Build.MANUFACTURER+"\n";
        final String model = "Model: "+Build.MODEL+"\n";
        final String version = "Version: "+Build.VERSION.RELEASE+"\n";
        final String imei = "IMEI: "+this.imei+"\n";
        String ok = "Your phone is CLONE.";

        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Sorry!")
                .setMessage(brand+model+version+imei+ok)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                })
                .setNegativeButton("Copy Info", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboardManager.setText(brand+model+version+imei);
                        if (clipboardManager.hasText()){
                            Toast.makeText(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void timerDelayRemoveDialog(long time, final Dialog d){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (d.isShowing()) {
                    d.dismiss();
                    Log.d("LOL", "ERROR");
                    Real();
//                    Toast.makeText(MainActivity.this, "Error!\nPlease try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, time);
    }

    public boolean checkInternet(){
        boolean what = false;
        CheckInternet checkNet = new CheckInternet(this);
        if (checkNet.isInternetOn()){
            what = true;
        }else{
            what = false;
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Error!");
            alertDialog.setMessage("No internet connection!");
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Try again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (checkInternet()){
                                progressDialog.show();
                                letCheck();
                            }
                        }
                    });
            alertDialog.show();
        }
        return what;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                ime.setText(Build.ID);
            } else {
                //not granted
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
//        serial.setText(Build.SERIAL + " Serial Number");
//        id.setText(Build.ID  + " Device Id");
//        String codename = Build.BRAND;
//        id.setText(codename);
    }    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Notice!");
        alertDialog.setMessage("Do you want to exit ?");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }



}