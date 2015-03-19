import javax.servlet.http.HttpServletRequest;

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
}
