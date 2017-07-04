package com.univpm.cpp.emergencynotificationsmvc.models.user;


/**
 * Interfaccia che rappresenta il modello degli utenti
 */
public interface UserModel {

    /**
     * Ottiene l'utente con l'username specificato
     * @param username username
     * @return
     */
    public User getUser(String username);

    /**
     * Ottiene l'ultimo utente ospite registrato nell'applicazione
     * @return
     */
    public User getLastGuestUser();

    /**
     * Aggiunge un nuovo utente
     * @param user
     * @return
     */
    public boolean newUser(User user);

    /**
     * Aggiunge un nuovo utente ospite
     * @param index indice del nuovo utente ospite
     * @return
     */
    public boolean newGuestUser(int index);

    /**
     * Aggiorna un utente esistente
     * @param name
     * @param surname
     * @param age
     * @param mobilephone
     * @param username
     * @param email
     * @param password
     * @return
     */
    public boolean updateUser(String name, String surname, int age, String mobilephone,
                              String username, String email, String password);

    /**
     * Elimina un utente esistente
     * @param username username dell'utente da eliminare
     * @return
     */
    public boolean deleteUser(String username);

}
