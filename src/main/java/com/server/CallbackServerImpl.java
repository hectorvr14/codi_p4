// Computación Distribuída - Curso 2023/2024
// Práctica 4
// Autor - Héctor Varela Rodríguez

package com.example.p4;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class CallbackServerImpl extends UnicastRemoteObject implements CallbackServerInterface {

       private HashMap<CallbackClientInterface, Integer> clientList;

       public CallbackServerImpl() throws RemoteException {

           ////////////////////////////////////
           // PARTE 1 - Inicialización da clase

           super( );
           this.clientList = new HashMap<>();

           ////////////////////////////////////
           // PARTE 2 - Abrir o ficheiro

           InputStreamReader is = null;

           // Inténtase abrir o ficheiro
           try {
               is = new InputStreamReader(new FileInputStream("rr1.txt"));
           } catch (IOException ioe) {
               System.out.println("Houbo un problema intentando ler o ficheiro. En particular: ");
               System.out.println(ioe.getMessage());
               System.exit(-1);
           }
           // Buffered reader - para ler por liñas
           BufferedReader br = new BufferedReader(is);

           ////////////////////////////////////
           // PARTE 3 - Lectura do ficheiro e proporcionar o servicio

           // Defínese un Timer que lerá un dato por segundo e llo enviará aos clientes subscritos
           Timer timer = new Timer();
           TimerTask readData = new TimerTask()
           {
               @Override
               public void run() {

                   double data = 0;

                   // Lese un dato do archivo (unha línea)
                   try {
                       data = Double.parseDouble(br.readLine());
                   } catch (Exception e) {
                       System.out.println("Houbo un erro intentando ler o ficheiro. En particular:");
                       System.out.println(e.getMessage());
                       System.exit(-1);
                   }

                   System.out.println(data);

                   try {
                       sendData(data);
                   } catch(RemoteException re) {
                       System.out.println(re.getMessage());
                   }

               }
           };

           // O temporizador bota a andar desde o momento no que arranca a execución do programa, e o método execútase cada segundo
           timer.schedule(readData, new Date(System.currentTimeMillis()), 1000);

       }

      public String sayHello( )
        throws java.rmi.RemoteException {
          return("hello");
      }

      public synchronized void subscribe(CallbackClientInterface callbackClientObject, int time) throws RemoteException{
          // Almacénase a interfaz do cliente, ou se xa existe, actualízase o tempo de subscrición
          this.clientList.put(callbackClientObject, time);
      }

      public synchronized void unsubscribe(CallbackClientInterface callbackClientObject) throws RemoteException{
        if (clientList.remove(callbackClientObject) == null) {
            System.out.println("unregister: o cliente non estaba subscrito.");
        }
      }

      // Este método privado utilízase no temporizador, para poder acceder á lista de clientes. Intentar esto dentro do Timer non funciona
      private synchronized void sendData(double data) throws RemoteException {

           // Arraylist de clientes que deben ser eliminados
           ArrayList<CallbackClientInterface> toRemove = new ArrayList<>();

           for(CallbackClientInterface cci : this.clientList.keySet()) {

                    // Inténtase enviar o dato ao cliente
                   cci.newData(data);
                   // Actualizanse os tempos
                   this.clientList.put(cci, this.clientList.get(cci) - 1);

                   // Se o contador chega a cero, márcase o cliente para eliminar
                   if(this.clientList.get(cci) == 0) {
                       toRemove.add(cci);
		            }

           }

          // Procédese á eliminación
          removeClients(toRemove);
      }

      // Este método privado utilízase para eliminar os clientes que xa rematasen a subscrición
      private synchronized void removeClients(ArrayList<CallbackClientInterface> clients) {
           for (CallbackClientInterface cci : clients) {
               this.clientList.remove(cci);
           }
      }

}
