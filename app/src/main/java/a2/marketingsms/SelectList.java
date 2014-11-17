/*
Texthem: Massive SMS Sender for Android

Copyright (C) 2014  Aarón Rosas Rodríguez aarr90@gmail.com

This file is part of Texthem.

Texthem is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Texthem is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Texthem.  If not, see <http://www.gnu.org/licenses/>.

*/

package a2.marketingsms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.danielme.blog.demo.listviewcheckbox.CustomArrayAdapter;
import com.danielme.blog.demo.listviewcheckbox.Row;

import java.util.ArrayList;
import java.util.List;

import a2.marketingsms.components.DialogHandler;
import a2.marketingsms.components.MyProgressDialog;
import a2.marketingsms.components.SMSSender;
import a2.marketingsms.model.Contact;
import a2.marketingsms.model.TemplateText;


public class SelectList extends Activity {


    List<Row> rows;
    ListView list;
    List<Contact> listContacts;


    /**
     * Confirm Send
     */
    private void confirmSend() {
        //get the rows checked
        final List<Row> checks = new ArrayList<Row>();
        for (Row row : rows)
            if (row.isChecked())
                checks.add(row);

        DialogHandler appdialog = new DialogHandler();
        String msg = getString(R.string.deseaEnviar) + " " + checks.size() + " " + getString(R.string.mensajes_interClose);
        appdialog.Confirm(SelectList.this,
                getString(R.string.Enviar),
                msg,
                getString(R.string.No),
                getString(R.string.Si),
                new Runnable() {
                    public void run() {
                        sendClick(checks);
                    }
                },
                null);
    }

    /**
     * Button Send Click
     *
     * @param checks rows checked to send
     */
    private void sendClick(final List<Row> checks) {

        //progress dialog
        final MyProgressDialog d = new MyProgressDialog(this, getString(R.string.EnviandoMensajes), getString(R.string.PorFavorEspere));
        //number of sends correct
        final int[] numSends = {0};
        d.setProgressTask(new Runnable() {
            @Override
            public void run() {
                SMSSender sender = new SMSSender();
                TemplateText sms = new TemplateText(getIntent().getExtras().getString("sms"));
                int maxChars = getIntent().getExtras().getInt("maxChars");
                for (int i = 0; i < list.getCount(); ++i) {
                    if (rows.get(i).isChecked()) {
                        //load the contact
                        Contact c = listContacts.get(i);
                        //check phone valid
                        if (c.checkPhone()) {
                            //apply the template to the contact
                            String msg = sms.getInstance(c);
                            //if max length is respect, send the message
                            if (msg.length() <= maxChars) {
                                if (sender.send(c.getPhone(), msg))
                                    numSends[0]++;
                            }
                            //a bit delay
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        //update progress
                        d.setProgress(i + 1);
                    }
                }
                //toast informative
                /*runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), numSends[0] + " " + getString(R.string.mensajesEnviados), Toast.LENGTH_LONG).show();
                    }
                });*/

                // Prepare data intent
                Intent data = new Intent();
                data.putExtra("numSends", numSends[0]);
                data.putExtra("numChecks", checks.size());
                // Activity finished ok, return the data
                setResult(-1, data);
                finish();

            }
        }, checks.size());
        d.start();
    }


    /**
     * Select all/none items on the list view
     *
     * @param all true for all, false for none
     */
    private void selectAllNone(final boolean all) {
        for (Row row : rows) {
            row.setChecked(all);
        }
        ((CustomArrayAdapter) list.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_list);
        list = (ListView) findViewById(R.id.list);

        //contacts
        listContacts = Contact.listAll(Contact.class);

        //rows for the list
        rows = new ArrayList<Row>(listContacts.size());

        //foreach contact create his row
        for (Contact c : listContacts) {
            Row row = new Row();
            row.setTitle(c.getName() + " " + c.getSurname());
            row.setSubtitle(c.getPhone());
            row.setChecked(true);
            rows.add(row);
        }

        //set rows on list
        list.setAdapter(new CustomArrayAdapter(this, rows));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Row row = rows.get(position);
                row.setChecked(!row.isChecked());
            }
        });

        /*
        SEND SMS EVENT
         */
        Button btnSendSMS = (Button) findViewById(R.id.bEnviar);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmSend();
            }
        });

        /*
        SELECT ALL
         */
        Button btnSelectAll = (Button) findViewById(R.id.bSelTodos);
        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectAllNone(true);
            }
        });


        /*
        SELECT NONE
         */
        Button btnSelectNone = (Button) findViewById(R.id.bSelNinguno);
        btnSelectNone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectAllNone(false);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.smslist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}
