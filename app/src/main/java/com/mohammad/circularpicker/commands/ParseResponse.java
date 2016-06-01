package com.mohammad.circularpicker.commands;

/**
 * Created by mohammad on 5/31/2016.
 */
public class ParseResponse {


    public String ParseResponse(String response) {

        if (response.contains("SETPOINT"))
            return "SETPOINT " + response.substring(response.indexOf('*', 1) + 1, response.lastIndexOf('*'));

        else if (response.contains("SPCOOLAWAY"))
            return "SPCOOLAWAY " + response.substring(response.indexOf('*', 1) + 1, response.lastIndexOf('*'));

        else if (response.contains("SPHEATAWAY"))
            return "SPHEATAWAY " + response.substring(response.indexOf('*', 1) + 1, response.lastIndexOf('*'));

        else if (response.contains("ONOFF"))
            return "ONOFF " + response.substring(response.indexOf('*', 1) + 1, response.lastIndexOf('*'));

        else if (response.contains("AWAY"))
            return "AWAY " + response.substring(response.indexOf('*', 1) + 1, response.lastIndexOf('*'));

        else if (response.contains("MODE"))
            return "MODE " + response.substring(response.indexOf('*', 1) + 1, response.lastIndexOf('*'));

        else if (response.contains("TEMP"))
            return "TEMP " + response.substring(response.indexOf('*', 1) + 1, response.lastIndexOf('*'));

        else if (response.contains("FAN"))
            return "FAN " + response.substring(response.indexOf('*', 1) + 1, response.lastIndexOf('*'));

        else if (response.contains("GETALL")) {
            String result;
            result=response.replace('*',' ');
            String regex="(?=\\()|(?<=\\)\\d)";
            String [] request=result.split(regex);
            return request.toString();
        }
        return "the response is not in normal mode";
    }
}

