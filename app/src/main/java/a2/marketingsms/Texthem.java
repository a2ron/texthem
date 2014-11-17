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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import a2.marketingsms.components.ContactImporter;
import a2.marketingsms.components.DialogHandler;
import a2.marketingsms.components.MyProgressDialog;
import filechooser.FileChooser;

public class Texthem extends Activity {

    //intents results
    static final int FILE_CHOOSER = 11;
    static final int SELECT_LIST = 12;
    //to load max chars established in string values
    int maxChars;
    //the message in the UI
    EditText et_sms;
    //the button
    Button b_selContacts;


    /**
     * To control max chars
     */
    private void controlMaxChars() {
        //num current characters
        int num = et_sms.getText().length();
        //control max chars
        if (num > maxChars) {
            et_sms.setText(et_sms.getText().toString().substring(0, maxChars));
            et_sms.setSelection(maxChars);//cursor to end
            num = et_sms.getText().length();
        }
        //update count
        TextView t_count = (TextView) findViewById(a2.marketingsms.R.id.t_charCount);
        t_count.setText(String.valueOf(num));
        b_selContacts.setEnabled(num > 0);
    }

    /**
     * Import contacts
     *
     * @param filename file to parse
     * @param truncate true to clean the table, delete all old contacts
     */
    private void importar(final String filename, final boolean truncate) {
        //progress dialog
        final MyProgressDialog d = new MyProgressDialog(this, getString(R.string.ImportandoContactos), getString(R.string.PorFavorEspere));
        d.setSpinnerStyle();
        d.setProgressTask(new Runnable() {
            @Override
            public void run() {
                //do the import
                int num = ContactImporter.importar(filename, truncate);
                //finalize the progress dialog
                d.setProgress(100);
                //toast informative
                final String msg = (num >= 0) ? num + " " + getString(R.string.contactosFueronImportados) : getString(R.string.e_0xa201);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, 100);
        d.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_marketing_sms);

        //sms edit text
        et_sms = (EditText) findViewById(R.id.et_sms);
        //button
        b_selContacts = (Button) findViewById(R.id.b_selContacts);

        //init max chars
        maxChars = Integer.parseInt(((TextView) findViewById(R.id.t_charMax)).getText().toString());
        et_sms.setSelection(et_sms.getText().length());//cursor to end
        controlMaxChars();

        /*
         SMS ON KEY: control max chars
         */
        et_sms.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                controlMaxChars();

            }
        });

        /*
        BUTTON EVENT
         */
        b_selContacts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String template = (String.valueOf(et_sms.getText()));
                if (template.length() > 0) {
                    //intent fot launch SelectList
                    Intent intent = new Intent(getApplicationContext(), SelectList.class);
                    intent.putExtra("sms", template);
                    intent.putExtra("maxChars", maxChars);
                    //launch SelectList
                    startActivityForResult(intent, SELECT_LIST);
                }
            }
        });
    }

    /* CONTROL INTENT RESULT */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* IMPORT CONTACTS */
        if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
            //get the file selected
            final String fileSelected = data.getStringExtra("fileSelected");
            //dialog to decide if truncate contacts
            DialogHandler appdialog = new DialogHandler();
            appdialog.Confirm(this,
                    getString(R.string.Importacion),
                    getString(R.string.deseaBorrarContactosExistentes),
                    getString(R.string.No),
                    getString(R.string.Si),
                    new Runnable() {
                        public void run() {
                            importar(fileSelected, true);
                        }
                    },
                    new Runnable() {
                        public void run() {
                            importar(fileSelected, false);
                        }
                    });

        }
        if ((requestCode == SELECT_LIST) && (resultCode == -1)) {

            int numSends = data.getExtras().getInt("numSends");
            String text = numSends + " " + getString(R.string.mensajesEnviados);
            DialogHandler appdialog = new DialogHandler();
            appdialog.Confirm(this,
                    getString(R.string.EnvioRealizado),
                    text,
                    null,
                    getString(R.string.Ok),
                    null,
                    null);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.marketing_sm, menu);
        return true;
    }

    /*
    MENU SELECTION
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean res;
        switch (item.getItemId()) {
            case R.id.menu_import:
                //intent fot launch FileChooser
                Intent intent = new Intent(this, FileChooser.class);
                //filter extensions
                ArrayList<String> extensions = new ArrayList<String>();
                extensions.add(".csv");
                extensions.add(".txt");
                intent.putStringArrayListExtra("filterFileExtension", extensions);
                //launch FileChooser
                startActivityForResult(intent, FILE_CHOOSER);
            default:
                res = super.onOptionsItemSelected(item);
        }
        return res;
    }

}
