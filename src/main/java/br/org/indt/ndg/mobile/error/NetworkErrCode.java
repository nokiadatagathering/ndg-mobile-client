/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.error;

import br.org.indt.ndg.mobile.Resources;

public class NetworkErrCode {

    public static String codeToString(String message) {

        // Note that error codes should be positioned from most digits to least digits with exclusion of ones that begin with '-'

        // Socket
        if(message.endsWith("10053")) {
            message = Resources.SOFTWARE_CONN_ABORT + " " + Resources.CHECK_NETWORK;
        }
        else if(message.endsWith("10065") || message.endsWith("-191")) {
            message = Resources.NO_ROUTE_TO_HOST + " " + Resources.CHECK_NETWORK;
        }
        else if(message.endsWith("10061")) {
            message = Resources.CONNECTION_REFUSED + " " + Resources.CHECK_NETWORK;
        }
        else if(message.endsWith("10013")) {
            message = Resources.PERMISSION_DENIED;
        }
        else if(message.endsWith("10050")) {
            message = Resources.NETWORK_DOWN + " " + Resources.CHECK_NETWORK;
        }
        else if(message.endsWith("10051") || message.endsWith("-190")) {
            message = Resources.NETWORK_UNREACHABLE + " " + Resources.CHECK_NETWORK;
        }
        else if(message.endsWith("10060") || message.endsWith("-33")) {
            message = Resources.CONNECTION_TIMEOUT + " " + Resources.TRY_AGAIN_LATER;
        }
        else if(message.endsWith("10091")) {
            message = Resources.NETWORK_UNAVAILABLE + " " + Resources.CHECK_NETWORK;
        }
        else if(message.endsWith("11001")) {
            message = Resources.HOST_NOT_FOUND + " " + Resources.CHECK_SERVER;
        }


        else if(message.endsWith("404")) {
            message = Resources.HTTP_NOT_FOUND + " " + Resources.CHECK_SERVER;
        }
        else if(message.endsWith("403")) {
            message = Resources.HTTP_FORBIDDEN + " " + Resources.CHECK_SERVER;
        }
        else if(message.endsWith("400")) {
            message = Resources.HTTP_BAD_REQUEST;
        }
        else if(message.endsWith("401")) {
            message = Resources.HTTP_UNAUTHORIZED;
        }
        else if(message.endsWith("500")) {
            message = Resources.HTTP_INTERNAL_ERROR + " " + Resources.TRY_AGAIN_LATER;
        }
        else if(message.endsWith("502") || message.endsWith("503")) {  // from user point of view
            message = Resources.HTTP_OVERLOADED + " " + Resources.TRY_AGAIN_LATER;
        }
        else if (message.endsWith("-5120")) {
            message = Resources.NO_DNS + " " + Resources.CHECK_NETWORK;
        }
        else if (message.endsWith("-5105")) {
            message = Resources.NO_ROUTE_TO_HOST+ " " + Resources.CHECK_NETWORK;
        }
        else if(message.endsWith("-34") || message.endsWith("-18")) {
            message = Resources.EWEBSERVER_ERROR + " " + Resources.CHECK_NETWORK;
        }
        else if(message.endsWith("-3")) {
            message = Resources.NEWUI_CANCEL;
        }

        try {
            int errCode = Integer.parseInt(message);
            if( errCode > -30179 && errCode <= -30170) {
                message = Resources.WIRELESS_INTERFACE_ERROR;
            }
        } catch( Exception ex ) {
        }

        message = Resources.FAILED_REASON + " " + message;
        
        return message;
    }
}

