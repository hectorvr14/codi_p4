// Computación Distribuída - Curso 2023/2024
// Práctica 4
// Autor - Héctor Varela Rodríguez

package com.example.p4;

import java.rmi.*;

public interface CallbackServerInterface extends Remote {
  // Mensaxe de benvida aos clientes subscritos
  public String sayHello( ) throws RemoteException;

  // Método que lles permite aos clientes subscribirse ao servidor, ou renovar a súa subscrición actual
  public void subscribe(CallbackClientInterface callbackClientObject, int time) throws RemoteException;

  // Método que lles permite aos clientes cancelar a súa subscrición actual
  public void unsubscribe(CallbackClientInterface callbackClientObject) throws RemoteException;
}
