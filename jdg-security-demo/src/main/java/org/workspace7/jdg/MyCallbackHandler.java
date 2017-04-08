package org.workspace7.jdg;

import javax.security.auth.callback.*;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.RealmCallback;
import java.io.IOException;

/**
 * @author kameshs
 */
public class MyCallbackHandler implements CallbackHandler {
    private String username;
    private char[] password;
    private String realm;

    public MyCallbackHandler(String username, String realm, char[] password) {
        this.realm = realm;
        //this.username = "uid=" + username + ",ou=People,dc=example,dc=com";
        this.username = username;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                ((NameCallback) callback).setName(username);
            } else if (callback instanceof PasswordCallback) {
                ((PasswordCallback) callback).setPassword(password);
            } else if (callback instanceof AuthorizeCallback) {
                AuthorizeCallback authorizeCallback = (AuthorizeCallback) callback;
                authorizeCallback.setAuthorized(authorizeCallback.getAuthenticationID()
                        .equals(authorizeCallback.getAuthorizationID()));
            } else if (callback instanceof RealmCallback) {
                ((RealmCallback) callback).setText(realm);
            } else {
                throw new UnsupportedCallbackException(callback);
            }

        }
    }
}
