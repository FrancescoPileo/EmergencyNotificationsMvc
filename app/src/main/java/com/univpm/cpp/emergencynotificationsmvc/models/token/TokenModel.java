package com.univpm.cpp.emergencynotificationsmvc.models.token;

/**
 * Interfaccia che rappresenta il modello dei token
 */
public interface TokenModel {

    /**
     * Aggiunge un nuovo token
     * @param token
     * @return
     */
    public boolean newToken(Token token);

}
