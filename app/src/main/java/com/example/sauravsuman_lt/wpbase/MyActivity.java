package com.example.sauravsuman_lt.wpbase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class MyActivity extends Activity {


    private ProgressDialog progressBar;
    private int progressBarStatus = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        Button buttonn1=(Button) this.findViewById(R.id.button1);
        Button buttonn2=(Button) this.findViewById(R.id.button2);
        Button buttonn3=(Button) this.findViewById(R.id.button3);
        Button buttonn4=(Button) this.findViewById(R.id.button4);


        buttonn1.setOnClickListener(onClickListener);
        buttonn2.setOnClickListener(onClickListener);
        buttonn3.setOnClickListener(onClickListener);
        buttonn4.setOnClickListener(onClickListener);

          }


    private View.OnClickListener onClickListener = new View.OnClickListener() {

        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.button1:
                    try {
                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibe.vibrate(100);

                        TextView tvStatus = (TextView) findViewById(R.id.tv1);
                        tvStatus.setText("PLEASE IMPORT CONTACT AS PER AS OUR INSTRUCTION");

                    } catch (Exception e) {

                    }

                    break;
                case R.id.button2:

                    Intent i;

                    PackageManager manager = getPackageManager();
                    try {
                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(100);
                        TextView tvStatus = (TextView) findViewById(R.id.tv1);
                        tvStatus.setText("WP-APP OPENING PROCESS COMPLETE");
                        i = manager.getLaunchIntentForPackage("com.whatsapp");
                        if (i == null)
                            throw new PackageManager.NameNotFoundException();
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        startActivity(i);
                    } catch (PackageManager.NameNotFoundException e) {

                    }


                    break;
                case R.id.button3:
                    Vibrator vibe1 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe1.vibrate(100);
                    progressBarStatus = 0;
                    // prepare for a progress bar dialog
                    progressBar = new ProgressDialog(MyActivity.this);
                    progressBar.setMessage("Exporting... Please Wait! ");
                    progressBar.setIndeterminate(true);
                    progressBar.setCancelable(false);
                    progressBar.show();
                    //reset progress bar status
                    new Thread(new Runnable() {
                        public void run() {
                            while (progressBarStatus < 100) {
                                // process some tasks
                                progressBarStatus = exportc();
                                //Log.d("INCREMENT", String.valueOf(progressBarStatus));
                                // Update the progress bar
                            }
                            // close the progress bar dialog
                            progressBar.dismiss();
                            runOnUiThread(new Runnable() {
                                public void run()
                                {
                                    try {
                                        TextView tvStatus = (TextView) findViewById(R.id.tv1);
                                        tvStatus.setText("All Contact Exported");
                                    }
                                    catch(Exception e)
                                    {
                                        //System.out.println("errr11-"+e.toString());
                                    }

                                    //Toast.makeText(MyActivity.this,"DELETED", Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    }).start();


                    break;
                case R.id.button4:
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    progressBarStatus = 0;
                    // prepare for a progress bar dialog
                    progressBar = new ProgressDialog(MyActivity.this);
                    progressBar.setMessage("Deleting... Please Wait! ");
                    progressBar.setIndeterminate(true);
                    progressBar.setCancelable(false);
                    progressBar.show();
                    //reset progress bar status
                    new Thread(new Runnable() {
                        public void run() {
                            while (progressBarStatus < 100) {
                                // process some tasks
                                progressBarStatus = deletec();
                                //Log.d("INCREMENT", String.valueOf(progressBarStatus));
                                // Update the progress bar
                            }
                            // close the progress bar dialog
                            progressBar.dismiss();
                            runOnUiThread(new Runnable() {
                                public void run()
                                {
                                    try {
                                        TextView tvStatus = (TextView) findViewById(R.id.tv1);
                                        tvStatus.setText("All Contact Deleted");
                                    }
                                    catch(Exception e)
                                    {
                                        //System.out.println("errr11-"+e.toString());
                                    }

                                    //Toast.makeText(MyActivity.this,"DELETED", Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    }).start();

                    break;
            }

        }
    };

    public int exportc()
    {
        Cursor c = getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[] { ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY },
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[] { "com.whatsapp" },
                null);

        ArrayList<String> myWhatsappContacts = new ArrayList<String>();
        int contactNameColumn = c.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
        int contactNameColumnc = c.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID);

        int co=0;
        while (c.moveToNext())
        {
            co++;
            try {
                //name.setText(c.getString(contactNameColumn));
                Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.TYPE},

                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",//AND " +
                                //ContactsContract.CommonDataKinds.Phone.TYPE + " = "+
                                //ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE+" OR "+
                                //ContactsContract.CommonDataKinds.Phone.TYPE + " = "+
                                //ContactsContract.CommonDataKinds.Phone.TYPE_HOME,

                                //ContactsContract.CommonDataKinds.Phone.TYPE_HOME,

                        new String[]{c.getString(contactNameColumnc)},
                        null);

                String contactNumber="";
                //int types="";
                //if (cursorPhone.moveToFirst())
                while (cursorPhone.moveToNext()) {

                    //contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.));
                    int types = cursorPhone.getInt(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                    //System.out.println(contactNumber);
                    System.out.println(types);

                    switch (types) {
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //System.out.println(contactNumber);
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //System.out.println(contactNumber);
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //System.out.println(contactNumber);
                            break;
                        default:
                            contactNumber="0000000000";
                    }
                    //TextView contact = new TextView(this);
                    //contact.setText(contactNumber);

                    try {
                        if(contactNumber!="0000000000") {
                            String str = contactNumber + "," + c.getString(contactNameColumn);
                            TextView tvStatus = (TextView) findViewById(R.id.tv1);
                            tvStatus.setText("EXPORT PROCESS COMPLETE");
                            SaveText(str);
                        }
                    } catch (Exception e) {

                    }

                }

                cursorPhone.close();



            }
            catch(Exception e) {
                System.out.println(e.toString());
            }
            //System.out.println(phoneNumber);

        }

        return 101;
    }

    public int deletec()
    {
        //try {
            ContentResolver contentResolver =getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            //TextView tvStatus = (TextView) findViewById(R.id.tv1);
            //tvStatus.setText("CONTACT DELETION IN-PROGRESS");
        //int c=0;



            while (cursor.moveToNext()) {
                //c++;
                //System.out.println("done"+c);
                String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                contentResolver.delete(uri, null, null);
            }



        return 101;
    }

    public void SaveText(String st){

        File file = new File(Environment.getExternalStorageDirectory() + "/wpbaseResult/result.txt");
        File folder = new File(Environment.getExternalStorageDirectory() + "/wpbaseResult");
        folder.mkdir();
        try {
            if (file.createNewFile()) {

                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
                //file.delete();
                //file.createNewFile();

            }
        }
        catch(Exception e)
        {

        }
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);

                String text = st;
                bw.write(text);
                bw.write('\n');
                bw.close();


        }

        catch (java.io.IOException e) {
            System.out.println(e.toString());
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
