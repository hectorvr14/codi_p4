// Computación Distribuída - Curso 2023/2024
// Práctica 4
// Autor - Héctor Varela Rodríguez

package com.example.p4;

import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class CallbackServer {

  public static void main(String args[]) {

      // Arrancar o rexistro RMI

      String registryURL;
      try{

          // Se non está executándose xa, cráese un rexistro RMI para o servidor no porto introducido antes
          startRegistry(12345);

          // Instánciase a clase que implementa a interfaz remota
          CallbackServerImpl exportedObj = new CallbackServerImpl();

          // Créase unha URL para a referencia do obxecto que se vai almacenar no rexistro
          registryURL = "rmi://localhost:" + 12345 + "/callbackServer";

          // Sobreescríbese no rexistro calquera referencia co nome anterior
          Naming.rebind(registryURL, exportedObj);

          // Todo ben?
          System.out.println("O servidor está preparado");

      } catch(Exception re){
        System.out.println("Excepción no main: " + re.getMessage());
      }

  }

  // Este método inicia un rexistro RMI no host local, se non existe xa no porto especificado
  private static void startRegistry(int RMIPortNum) throws RemoteException{

      try{
          Registry reg = LocateRegistry.getRegistry(RMIPortNum);
          reg.list(); // Esta chamada lanzará unha excepción se o rexistro non existe xa
      } catch (RemoteException re){
          // Créase o rexistro
          Registry reg = LocateRegistry.createRegistry(RMIPortNum);
          System.out.println("Rexistro RMI creado no porto " + RMIPortNum);
      }

  }

}
