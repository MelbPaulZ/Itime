package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.bean.User;

/**
 * Created by Qiushuo Huang on 2017/3/15.
 */

public class UserValidator {
    private static UserValidator instance = null;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 16;
    public static final int NAME_MIN_LENGTH = 2;
    public static final int NAME_MAX_LENGTH = 40;
    private Context context;
    private EmailUtil emailUtil;
    private UserValidator(Context context){
        this.context = context.getApplicationContext();
        emailUtil = EmailUtil.getInstance(context);
    }

    public static UserValidator getInstance(Context context){
        if(instance==null){
            instance = new UserValidator(context);
        }
        return instance;
    }

    public boolean isValidUser(User user){

        return isValidEmail(user.getEmail())
                && isValidPassword(user.getPassword())
                && isValidName(user.getPersonalAlias());
    }

    public boolean isValidEmail(String email){
        return emailUtil.isValidDomain(email);
    }

    public boolean isValidPassword(String password) {
        if(password==null){
            return false;
        }

        if(password.length()>PASSWORD_MAX_LENGTH){
            return false;
        }

        if(password.length()<PASSWORD_MIN_LENGTH){
            return false;
        }

        return true;
    }

    public boolean isValidName(String input){
        String name = input.trim();
        if(name==null){
            return false;
        }

        if(name.length()>NAME_MAX_LENGTH){
            return false;
        }

        if(name.length()<NAME_MIN_LENGTH){
            return false;
        }

        return true;
    }
}
