package com.univpm.cpp.emergencynotificationsmvc.models.session;

import com.univpm.cpp.emergencynotificationsmvc.models.user.User;

/**
 * Interfaccia che rappresenta il modello delle sessioni utente
 */
public interface SessionModel {

    /**
     * Aggiunge una nuova sessione
     * @param session
     * @return
     */
    public boolean newSession(Session session);

    /**
     * Aggiorna una sessione esistente
     * @param session
     * @return
     */
    public boolean updateSession(Session session);

    /**
     * Ottiene l'ultima sessione di un utente
     * @param user Utente
     * @return
     */
    public Session getLastSession(User user);


}
