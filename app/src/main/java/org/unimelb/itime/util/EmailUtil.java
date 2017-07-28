package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.bean.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Qiushuo Huang on 2017/3/8.
 */

public class EmailUtil {

    private List<String> emailSuffixes = new ArrayList<String>();
    private static final int K = 1;
    private Pattern emailPattern;
    private static EmailUtil instance;

    public static EmailUtil getInstance(Context context){
        if(instance==null){
            instance = new EmailUtil(context);
        }
        return instance;
    }

    private EmailUtil(Context context){
        String str="^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,})+)$";
        emailPattern = Pattern.compile(str);
//        List<Domain> list = DBManager.getInstance(context).getAllDomains();
//        for(Domain domain:list){
//            emailSuffixes.add(domain.getRule());
//        }
    }

    public boolean isValidDomain(String input){
        String domain = input.trim();
        if(domain==null){
            return false;
        }
        if(!isEmail(domain)){
            return false;
        }else{
            for(String suffix:emailSuffixes){
                if(domain.endsWith(suffix)){
                    return true;
                }
            }
        }
        return false;
    }

    public void setEmailSuffixes(List<String> emailSuffixes) {
        this.emailSuffixes = emailSuffixes;
    }

    public List<String> getAutoCompleteLists(String inputStr){
        List<String> result = new ArrayList<>();
        String input = inputStr.trim();
        if(!isLegal(input)){
            return result;
        }

        if (!input.isEmpty()) {
            String matchStr = decodeSuffix(input);
            List<String> suffixes = filterSuffixList(matchStr, emailSuffixes);
            for (String suffix : suffixes) {
                String completedStr = getCompleteEmail(input, suffix);
                if(isEmail(completedStr)){
                    result.add(completedStr);
                }
            }
        }
        return result;
    }

    public boolean isEmail(String input){
        String email = input.trim();
        return emailPattern.matcher(email).matches();
    }

    private String getCompleteEmail(String input, String suffix){
        return input.split("@")[0]+suffix;
    }

    private List<String> filterSuffixList(String suffix, List<String> suffixLists){
        ArrayList<String> result = new ArrayList<>();
        for(String str: suffixLists){
            if (str.startsWith(suffix)){
                result.add(str);
            }
        }
        return result;
    }

    private String decodeSuffix(String input){
        if(input.contains("@"))
            return input.substring(input.indexOf("@"));
        else{
            return "";
        }
    }

    private boolean isLegal(String input){
        return input.matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w*)*)$")
            && input.substring(input.indexOf("@")).length() > K;
    }
}
