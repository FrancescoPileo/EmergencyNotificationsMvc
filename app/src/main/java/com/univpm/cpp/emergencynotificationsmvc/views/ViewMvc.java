package com.univpm.cpp.emergencynotificationsmvc.views;

import android.os.Bundle;

/**
 * Interfaccia generica di una ViewMvc MVC
 */
public interface ViewMvc {

    /**
     * Prende l'AndroidView principale che è usata internamente dalla ViewMvc per presentare i dati
     * all'utente.
     * L'AndrdoidView potrebbe essere usata da un ControllerMvc per interrogare o alterare le
     * proprietà dell'AndroidView stessa o di qualche figlio dell'AndroidView.
     * @return L'AndroidViewe principale di questa ViewMvc
     */
    public android.view.View getRootView();

    /**
     * Questo metodo aggrega tutte le indormazioni circa lo stato di questa ViewMvc in un oggetto di
     * tipo Bundle. Le chiavi del Bundle di risposta devono essere fornite come costanti pubbliche
     * all'intenro delle interfacce (o implementazioni se non c'è interfaccia definita) delle
     * ViewMvc concrete.
     * L'uso principale di questo metodo è esportare lo stato di AndroidView modificabili
     * sottostanti la ViewMvc. Queste informazioni possono essere usate dal ControllerMvc ad esempio
     * per processare input dell'utente o salvare lo stato della view durante gli eventi del ciclo
     * di vita della view.
     * @return Bundle che contiene lo stato di questa ViewMvc, null se la view non ha stato
     */
    public Bundle getViewState();

}
