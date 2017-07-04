package com.univpm.cpp.emergencynotificationsmvc.models.node;

import java.util.ArrayList;


/**
 * Interfaccia che rappresenta il modello dei nodi
 */
public interface NodeModel {

    /**
     * Ottiene il nodo con l'id specificato
     * @param idNode
     * @return
     */
    public Node getNodeById(int idNode);

    /**
     * Ottiene al lista di tutti i nodi presenti
     * @return
     */
    public ArrayList<Node> getAllNodes();

}
