package com.extralarge.fujitsu.xl.AutomaticSmsRead;

/**
 * Created by Fujitsu on 10/05/2017.
 */

public interface SmsListener {

    public void messageReceived(String messageText);
}
