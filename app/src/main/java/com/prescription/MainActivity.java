package com.prescription;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.hardware.biometrics.BiometricManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button generatepdf,bt_share;
    EditText ed_bill,ed_date,ed_patientname,ed_age,ed_sex,ed_reg,ed_bedno,ed_add,ed_dateaddmision,ed_datedischarge,ed_diagonasis,ed_charenicu,ed_charenursing,ed_charedoctorround,ed_chareoxygen,
            ed_charecardicmonitor,ed_charesyringepump,ed_charerbs,ed_grandtotal,ed_paidamount;
    int pagewidth =1200;
    TextView add_signature;
    ImageView ivSignature;
    Bitmap bitmap, scaledMap;
    Uri pdfUri;
    Integer SELECT_IMAGE = 1;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_bill = findViewById(R.id.ed_bill);
        ed_date = findViewById(R.id.ed_date);
        ed_patientname = findViewById(R.id.ed_patientname);
        ed_age = findViewById(R.id.ed_age);
        ed_sex = findViewById(R.id.ed_sex);
        ed_reg = findViewById(R.id.ed_reg);
        ed_bedno = findViewById(R.id.ed_bedno);
        ed_add = findViewById(R.id.ed_add);
        ed_dateaddmision = findViewById(R.id.ed_dateaddmision);
        ed_datedischarge = findViewById(R.id.ed_datedischarge);
        ed_diagonasis = findViewById(R.id.ed_diagonasis);
        ed_charenicu = findViewById(R.id.ed_charenicu);
        ed_charenursing = findViewById(R.id.ed_charenursing);
        ed_charedoctorround = findViewById(R.id.ed_charedoctorround);
        ed_chareoxygen = findViewById(R.id.ed_chareoxygen);
        ed_charecardicmonitor = findViewById(R.id.ed_charecardicmonitor);
        ed_charesyringepump = findViewById(R.id.ed_charesyringepump);
        ed_grandtotal = findViewById(R.id.ed_grandtotal);
        ed_charerbs = findViewById(R.id.ed_charerbs);
        ed_paidamount = findViewById(R.id.ed_paidamount);
        generatepdf = findViewById(R.id.bt_generatepdf);
        add_signature = findViewById(R.id.add_signature);
        ivSignature = findViewById(R.id.ivSignature);
        bt_share = findViewById(R.id.bt_share);

        /*bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.placeholder);
        scaledMap = Bitmap.createScaledBitmap(bitmap,1200,510,false);*/

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        ed_date.setText(formattedDate);
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        add_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
            }
        });

        generatepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_bill.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter bill number",Toast.LENGTH_SHORT).show();
                    ed_bill.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_date.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Date",Toast.LENGTH_SHORT).show();
                    ed_date.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_patientname.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Patienet Name",Toast.LENGTH_SHORT).show();
                    ed_patientname.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_age.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Age",Toast.LENGTH_SHORT).show();
                    ed_age.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_sex.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Sex ",Toast.LENGTH_SHORT).show();
                    ed_sex.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_reg.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Registeraton Number",Toast.LENGTH_SHORT).show();
                    ed_reg.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_bedno.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Bed Number",Toast.LENGTH_SHORT).show();
                    ed_bedno.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_add.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Address",Toast.LENGTH_SHORT).show();
                    ed_add.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }

                if (ed_dateaddmision.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Date Of Admission",Toast.LENGTH_SHORT).show();
                    ed_dateaddmision.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_datedischarge.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Discharge Date",Toast.LENGTH_SHORT).show();
                    ed_datedischarge.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_diagonasis.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Diagnosis",Toast.LENGTH_SHORT).show();
                    ed_diagonasis.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_charenicu.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Bed Charge",Toast.LENGTH_SHORT).show();
                    ed_charenicu.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_charenursing.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Nursing Charge ",Toast.LENGTH_SHORT).show();
                    ed_charenursing.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_charedoctorround.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Doctor Round Charge",Toast.LENGTH_SHORT).show();
                    ed_charedoctorround.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_chareoxygen.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Oxygen Charge",Toast.LENGTH_SHORT).show();
                    ed_chareoxygen.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_charecardicmonitor.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Cardic Monitor Charge",Toast.LENGTH_SHORT).show();
                    ed_charecardicmonitor.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_charesyringepump.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Syringe Pump Charge",Toast.LENGTH_SHORT).show();
                    ed_charesyringepump.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_paidamount.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Paid Amount",Toast.LENGTH_SHORT).show();
                    ed_paidamount.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_charerbs.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter RBS",Toast.LENGTH_SHORT).show();
                    ed_charerbs.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (ed_grandtotal.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter Syringe Totla Amount",Toast.LENGTH_SHORT).show();
                    ed_grandtotal.getBackground().setColorFilter(getResources().getColor(R.color.red),
                            PorterDuff.Mode.SRC_ATOP);
                }
                if (!ed_bill.getText().toString().isEmpty()&&!ed_date.getText().toString().isEmpty()
                        &&!ed_patientname.getText().toString().isEmpty()&&!ed_age.getText().toString().isEmpty()
                &&!ed_sex.getText().toString().isEmpty()&&!ed_reg.getText().toString().isEmpty()
                        &&!ed_bedno.getText().toString().isEmpty()&&!ed_add.getText().toString().isEmpty()&&
                        !ed_dateaddmision.getText().toString().isEmpty()&&!ed_datedischarge.getText().toString().isEmpty()
                        &&!ed_diagonasis.getText().toString().isEmpty()&&!ed_charenicu.getText().toString().isEmpty()
                &&!ed_charenursing.getText().toString().isEmpty()&&!ed_charedoctorround.getText().toString().isEmpty()
                        &&!ed_chareoxygen.getText().toString().isEmpty()&&!ed_charecardicmonitor.getText().toString().isEmpty()
                        &&!ed_charesyringepump.getText().toString().isEmpty()&&!ed_paidamount.getText().toString().isEmpty()
                        &&!ed_charerbs.getText().toString().isEmpty()&&!ed_grandtotal.getText().toString().isEmpty()&& scaledMap!=null){
                    Toast.makeText(getApplicationContext(),"PDf is Generated ",Toast.LENGTH_SHORT).show();
                    createPOF();

               }

            }
        });
        bt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory().getPath(),"/Download/Koshal.pdf");

                if (file.exists()) {
                    if (Build.VERSION.SDK_INT < 24) {
                        pdfUri = Uri.fromFile(file);
                    } else{
                        Uri.parse(file.getPath());
                    }
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, pdfUri);
                    share.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(share, "Share"));

                }else {
                    Toast.makeText(MainActivity.this, "NO pdf Found",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPOF() {
       /* ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);*/
        PdfDocument myPdfDocument = new PdfDocument();
        Paint mypaint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(1200, 3000, 1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();


        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setTextSize(40);

        mypaint.setTextAlign(Paint.Align.LEFT);
        mypaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        mypaint.setTextSize(40);

        canvas.drawText("KAUSHALYA CHILD CARE ",40,200,titlePaint);
        canvas.drawText("CHILD AND NEWBORN CHILD SPECIALIST",40,250,titlePaint);
        canvas.drawText("PANJIYAAR COMPLECS",40,300,titlePaint);
        canvas.drawText("BEHIND HANUMAN TEMPLE SHAHGANJ BENTA",40,350,titlePaint);
        canvas.drawText("LAHERIYAASARAAY",40,400,titlePaint);

        canvas.drawText("BILL NO.:     "+ed_bill.getText(),40,500,mypaint);
        canvas.drawText("DATE:     "+ed_date.getText(),pagewidth/2,500,mypaint);

        canvas.drawText("NAME OF THE PATIENT:     "+ed_patientname.getText(),40,600,mypaint);

        canvas.drawText("AGE:     "+ed_age.getText(),40,700,mypaint);
        canvas.drawText("SEX:     "+ed_sex.getText(),pagewidth/2,700,mypaint);

        canvas.drawText("REG NO:     "+ed_reg.getText(),40,800,mypaint);
        canvas.drawText("BED NO. NICU:     "+ed_bedno.getText(),pagewidth/2,800,mypaint);

        canvas.drawText("ADDRESS:     "+ed_add.getText(),40,900,mypaint);

        canvas.drawText("DATE OF ADMISSION:     "+ed_dateaddmision.getText(),40,1000,mypaint);
        canvas.drawText("DATE OF DISCHARGE:     "+ed_datedischarge.getText(),pagewidth/2,1000,mypaint);

        canvas.drawText("NAME OF THE TREATING DOCTOR: DR. Shaantanu kumar",40,1100,titlePaint);
        canvas.drawText("M.B.B.S,M.D.(Paediatrics),D.M.C.H",40,1150,titlePaint);
        canvas.drawText("Senior Resident, DMCH Darbhanga",40,1200,titlePaint);

        canvas.drawText("DIAGNOSIS:     "+ed_diagonasis.getText(),40,1300,mypaint);

        canvas.drawText("CHARGES:     ",40,1400,mypaint);

        canvas.drawText("1. NICU BED CHARGE:     "+ed_charenicu.getText(),40,1500,mypaint);
        canvas.drawText("2. NURSING CHARGE:     "+ed_charenursing.getText(),40,1600,mypaint);
        canvas.drawText("3. DOCTOR ROUND CHARGE:    "+ed_charedoctorround.getText(),40,1700,mypaint);
        canvas.drawText("4. OXYGEN CHARGE::    "+ed_chareoxygen.getText(),40,1800,mypaint);

        canvas.drawText("5.CARDIC MONITOR CHARGE:    "+ed_charecardicmonitor.getText(),40,1900,mypaint);
        canvas.drawText("6. SYRINGE PUMP CHARGE:     "+ed_charesyringepump.getText(),40,2000,mypaint);
        canvas.drawText("7. RBS: "+ed_charerbs.getText(),40,2100,mypaint);
        canvas.drawText("7. GRAND TOTAL:     "+getResources().getString(R.string.rupees)+" "+ed_grandtotal.getText(),40,2200,mypaint);
        canvas.drawText("7. PAID AMOUNT:     "+getResources().getString(R.string.rupees)+" "+ed_paidamount.getText(),40,2300,mypaint);
        canvas.drawBitmap(scaledMap,900,2400, mypaint);

        myPdfDocument.finishPage(myPage);

       /* PdfDocument.PageInfo mypageInfo1 = new PdfDocument.PageInfo.Builder(1200, 1700, 1).create();
        PdfDocument.Page myPage1 = myPdfDocument.startPage(mypageInfo1);
        Canvas canvas1 = myPage1.getCanvas();





        myPdfDocument.finishPage(myPage1);*/

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialoge_pdf_name);
        EditText etPdfName =  dialog.findViewById(R.id.etPdfName);
        Button ok =  dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pdfName = etPdfName.getText().toString();

                if (pdfName!=""){
                    File file = new File(Environment.getExternalStorageDirectory().getPath(),"/Download/"+pdfName+".pdf");
                    try{
                        myPdfDocument.writeTo(new FileOutputStream(file));
                        myPdfDocument.close();
                        Toast.makeText(getApplicationContext(),"PDf is Generated ",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Please Enter PDf Name",Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.show();


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                        scaledMap = Bitmap.createScaledBitmap(bitmap,200,200,true);

                        ivSignature.setVisibility(View.VISIBLE);
                        add_signature.setVisibility(View.GONE);
                        ivSignature.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

}