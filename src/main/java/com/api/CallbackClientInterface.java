// Computación Distribuída - Curso 2023/2024
// Práctica 4
// Autor - Héctor Varela Rodríguez

package com.example.p4;

import java.rmi.*;

public interface CallbackClientInterface extends Remote {

    // Método a invocar polo servidor para enviarlle ao cliente un novo dato
    public void newData(double data) throws RemoteException;

}
