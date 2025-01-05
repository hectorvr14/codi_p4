// Computación Distribuída - Curso 2023/2024
// Práctica 4
// Autor - Héctor Varela Rodríguez

package com.example.p4;

import java.rmi.*;
import java.rmi.server.*;

public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface {

   private ClientController controller;
  
   public CallbackClientImpl() throws RemoteException {
      super( );
   }

   // Este método imprime por pantalla o dato recibido e pásallo ao controlador para que se amose por pantalla
   public void newData(double data) throws RemoteException {
      System.out.println(data);
      this.controller.newData(data);
   }

   public void setController (ClientController cc) { this.controller = cc; }

}
