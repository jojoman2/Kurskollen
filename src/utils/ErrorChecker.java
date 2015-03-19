package utils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fiona on 19/03/15.
 */
public class ErrorChecker {

    public static boolean checkParameters(HttpServletRequest req, String[] parameterNames){
        for(String paramaterName : parameterNames){
            if (req.getParameter(paramaterName) ==null){
                return false;
            }
        }
        return true;
    }


    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    public boolean validate(final String hex) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }
}

