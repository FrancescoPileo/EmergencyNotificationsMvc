package com.univpm.cpp.emergencynotificationsmvc.models.local;

import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;

/**
 * Model che gestisce le preferenze locali dell'applicazione
 */
public interface LocalPreferences {

    /**
     * memorizza un utente nell'applicazione
     * @param user
     */
    public void rememberUser(User user);

    /**
     * elimina l'utente memorizzato nell'applicazione
     */
    public void deleteLogin();

    /**
     * memorizza una sessione nell'applicazone
     * @param session
     */
    public void storeSession(Session session);

    /**
     * elimina la sessione memorizzata nell'applicazione
     */
    public void deleteSession();

    /**
     * ottiene la sessione memorizzata
     * @return
     */
    public Session getSession();

    /**
     * Controlla che sia stato già effettuato un'accesso all'applicazione
     * @return
     */
    public boolean alreadyLoged();


    /**
     * Ottiene l'utente memorizzato nell'applicazione
     * @return
     */
    public User getUser();


}
