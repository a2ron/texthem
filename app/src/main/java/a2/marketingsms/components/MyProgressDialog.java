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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * Dialog that show a progress bar/spinner and update progress
 */
public class MyProgressDialog extends ProgressDialog {

    //to update progress
    Handler updateBarHandler;
    //to control the progress
    Runnable progressTask;
    //current progress and max
    int progress;
    int max;

    /**
     * Constructor
     * @param context the context
     * @param title the title of the dialog
     * @param message the message of the dialog
     */
    public MyProgressDialog(Context context, String title, String message) {
        super(context);
        //handler to update progress
        updateBarHandler = new Handler();
        //set title and message
        setTitle(title);
        setMessage(message);
        //default progress bar
        setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //default init progress and max
        setProgress(0);
        setMax(0);
        //default not cancelable
        setCancelable(false);
    }

    /**
     * Set Spinner Style
     */
    public void setSpinnerStyle() {
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }


    /**
     * Init, show and start the progress dialog and the task to control the progress.
     * The dialog will be closed when progress==max
     */
    public void start() {
        //init and show
        progress = 0;
        show();
        //task to update the progress and close the dialog when finish
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //while it is not finished
                    while (progress < max) {
                        Thread.sleep(100);
                        //update the progress
                        updateBarHandler.post(new Runnable() {
                            public void run() {
                                setProgress(progress);
                            }
                        });
                    }
                    //finish, close it
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //start the task that update the progress
        Thread task = new Thread(progressTask);
        task.start();
    }

    /**
     * Set the task which update de progress
     *
     * @param progressTask The task to do that update the progress (view setProgress, setMax)
     * @param max the value max of the progress bar
     */
    public void setProgressTask(Runnable progressTask, int max) {
        this.progressTask = progressTask;
        setMax(max);
    }

    public int getProgress() {
        return progress;
    }

    /**
     * Set the progress value [0,Max]
     * @param progress the value
     */
    public void setProgress(int progress) {
        super.setProgress(progress);
        this.progress = progress;
    }

    public int getMax() {
        return max;
    }

    /**
     * Set the max value (to control when the dialog must be closed)
     * @param max the value max of the progress bar
     */
    public void setMax(int max) {
        super.setMax(max);
        this.max = max;
    }


}
