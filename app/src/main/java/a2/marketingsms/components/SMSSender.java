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


import android.telephony.SmsManager;


public class SMSSender {

    private SmsManager smsManager = null;

    public SMSSender() {
        // Get the default instance of the SmsManager
        this.smsManager = SmsManager.getDefault();
    }

    /**
     * Send a message to a phoneNumber
     * @param phoneNumber the number
     * @param message the message
     * @return success
     */
    public Boolean send(String phoneNumber, String message) {
        Boolean success = false;
        try {
            //send the message
            smsManager.sendTextMessage(phoneNumber,
                    null,
                    message,
                    null,
                    null);
            success = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }
}
