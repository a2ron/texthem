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

package a2.marketingsms.components;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import a2.marketingsms.model.Contact;
import au.com.bytecode.opencsv.CSVReader;


public class ContactImporter {

    /**
     * Posible CSV separators
     */
    static char[] separators = {',', ';'};


    /**
     * Import the contacts
     *
     * @param filename file to parse
     * @param truncate true to clean the table, delete all old contacts
     * @return num of records imported
     */
    public static int importar(String filename, boolean truncate) {
        return _import(filename, truncate, 0);
    }

    /**
     * Import the contacts
     *
     * @param filename   file to parse
     * @param truncate   true to clean the table, delete all old contacts
     * @param iSeparator index of the separators array
     * @return num of records imported
     */
    private static int _import(String filename, boolean truncate, int iSeparator) {
        //num of records imported
        int i = 0;
        //parser CSV
        CSVReader reader;
        try {
            //detect charset
            byte[] buf = new byte[4096];
            FileInputStream fis = new FileInputStream(filename);
            UniversalDetector detector = new UniversalDetector(null);
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
            String encoding = detector.getDetectedCharset();

            //create CSVReader
            reader = new CSVReader(new InputStreamReader(new FileInputStream(filename), encoding), separators[iSeparator]);

            //the values parsered in each line
            String[] values;
            //the first iteration
            values = reader.readNext();
            if (values != null) {
                //try to save the contact
                if (importContact(values)) {
                    i++; //increase number of contacts imported
                    if (truncate) {//truncate if applicable
                        Contact.deleteAll(Contact.class);
                        importContact(values);
                    }
                } else if (iSeparator + 1 < separators.length) {
                    //try with other separator
                    return _import(filename, truncate, iSeparator + 1);
                }
            }
            //while the parser read a line
            while ((values = reader.readNext()) != null) {
                if (importContact(values))  //save the contact
                    i++; //increase number of contacts imported
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            i = -1;
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        return i;
    }

    /**
     * Import a Contact
     * @param values the values of the contact
     * @return success
     */
    private static boolean importContact(String[] values) {
        boolean ok = false;
        //if there are 3 or more values
        if (values.length >= 3) {
            //save the contact
            Contact contact = new Contact(values[0], values[1], values[2]);
            contact.save();
            ok = true;
        }
        return ok;
    }


}
