package domind.doc_management.tool;

public class TokenExpiryCheck {

    public static Boolean tokenExpiryCheck(String token) {
        if (((int) (System.currentTimeMillis() / 1000)
                - Integer.parseInt(token.substring(token.lastIndexOf("|") + 1))) < 57600) { // 57600 = 16h token
                                                                                            // validity
            return true;
        } else
            return false;
    }
}
