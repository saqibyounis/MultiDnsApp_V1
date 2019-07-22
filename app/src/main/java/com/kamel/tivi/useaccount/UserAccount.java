package com.kamel.tivi.useaccount;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserAccount {

    private final String maxConnection;
    String userName;
    String expirationDate;
    String status;
    String activeAccounts;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String password;
    public String getMaxConnection() {
        return maxConnection;
    }

    public String getTrail() {
        return trail;
    }

    String trail;
    public static  UserAccount userAccount;
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        java.util.Date newDate =new Date(expirationDate);
        this.expirationDate = newDate.toString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActiveAccounts() {
        return activeAccounts;
    }

    public void setActiveAccounts(String activeAccounts) {
        this.activeAccounts = activeAccounts;
    }

    public String isTrail() {
        return trail;
    }

    public void setTrail(String trail) {
        this.trail = trail;
    }

    public UserAccount(String userName, String expirationDate, String status, String activeAccounts, String trail, String maxConnection,String password) {



        Calendar instance = Calendar.getInstance();
       /* if (loginModel.getExp_date().equals("1515847586")) {
            this.txt_exp_date.setText("unlimited");
        } else *///{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        long tempExDate = Long.parseLong(expirationDate) * 1000;
            instance.setTimeInMillis(tempExDate);
            //this.txt_exp_date.setText(dateFormat.format(instance.getTime()));
        //}

        this.maxConnection=maxConnection;
        this.userName = userName;
        this.expirationDate = dateFormat.format(instance.getTime());
        this.status = status;
        this.activeAccounts = activeAccounts;
        if(trail.equalsIgnoreCase("0")){

            this.trail = "No";


        }else{
            this.trail="Yes";

        }
        this.password=password;
        userAccount=this;
    }
}
