package com.extralarge.fujitsu.xl.AutomaticSmsRead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.SmsManager;

import com.extralarge.fujitsu.xl.ReporterSection.Verifyotp;

/**
 * Created by Fujitsu on 10/05/2017.
 */

public class SmsReceiver extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent)
        {

            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null)
                {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++)
                    {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber ;
                        String message = currentMessage .getDisplayMessageBody();
                        try
                        {
                            if (senderNum.equals("HP-MITEST"))
                            {
                                Verifyotp Sms = new Verifyotp();
                                Sms.recivedSms(message );
                            }
                        }
                        catch(Exception e){}

                    }
                }

            } catch (Exception e)
            {

            }
        }

    }


