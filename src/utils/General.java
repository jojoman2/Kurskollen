package utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by fiona on 19/03/15.
 */
public class General {

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static Random rnd = new SecureRandom();

    public static String randomString(int len)
    {
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
