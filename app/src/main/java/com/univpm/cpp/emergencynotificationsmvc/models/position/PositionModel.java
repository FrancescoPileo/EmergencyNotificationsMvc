package com.univpm.cpp.emergencynotificationsmvc.models.position;

import com.univpm.cpp.emergencynotificationsmvc.models.user.User;

/**
 * Interfaccia che rappresenta il modello delle posizioni utente
 */

public interface PositionModel {

    /**
     * Aggiunge una posizione utente
     * @param position posizione da aggiungere
     * @return
     */
    public boolean newPosition(Position position);

    /**
     * Ottiene l'ultima posizione di un utente
     * @param user Utente
     * @return
     */
    public Position getLastPositionByUser(User user);
}
