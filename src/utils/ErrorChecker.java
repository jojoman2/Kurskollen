package utils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fiona on 19/03/15.
 */
public class ErrorChecker {

    public static boolean checkNotNull(String[] parameters){
        for(String parameter : parameters){
            if(parameter == null){
                return false;
            }
        }
        return true;
    }

    private static final String PASSWORD_PATTERN= "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    public static boolean valadiatePassword(final String password){
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}

