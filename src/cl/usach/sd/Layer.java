package cl.usach.sd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dynamics.WireKOut;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;

public class Layer implements Cloneable, EDProtocol {
	private static final String PAR_TRANSPORT = "transport";
	private static String prefix = null;
	private int transportId;
	private int layerId;
	
	private ExampleNode tokenShip;//Revisa quien tiene el proceso actual;
	//private Message [][] mensajesPendientes;// Mensajes pendientes de cada Nodo, [IdNodo,mensajes]
	private HashMap<Integer, Message[]> colaMSG = new HashMap<Integer, Message[]>();
	//ArrayList<ArrayList<Message>> mensajesPendientes = new ArrayList<ArrayList<Message>>();
	
	
	/**
	 * Método en el cual se va a procesar el mensaje que ha llegado al Nodo
	 * desde otro Nodo. Cabe destacar que el mensaje va a ser el evento descrito
	 * en la clase, a través de la simulación de eventos discretos.
	 */
	@Override
	public void processEvent(Node myNode, int layerId, Object event) {
		//reviso si el tokenship (tocken) no esta perdido para restaurarlo
		if(((ExampleNode) Network.get((int) tokenShip.getID()))==tokenShip)
		{
			System.out.print("El tocketship esta dentro de la red");
		}
		else
		{
			//Si hay un error en el tocketship se resetea en al primer nodo de la red segun el index;
			//tokenShip=(ExampleNode) Network.get((int) Configuration.getPid(prefix + ".link"));
			tokenShip=(ExampleNode) Network.get(0);
		}
		//Veo si el evento es de tipo mensaje para agregarlo a la cola
		if(event instanceof Message)
		{
//			if(((ExampleNode) myNode).getTockenFlag()==true)
//			{
//				//System.out.print("Evento es mensaje y tiene tokenFlag");
//				Message message = (Message) event;
//				sendmessage(myNode, layerId, message);
//				//System.out.println(" - SE ENVIO MENSAJE");
//				// agrego 1 uso al cantDeUsosDelToken
//				((ExampleNode) myNode).setTockenFlag(false);
//				
//				Observer.cantDeUsosDelToken.add(1);
//			}
//			else
//			{
//				System.out.println("Evento es mensaje pero NO tiene tokenFlag");
//			}
			//mensajesPendientes[(int) myNode.getID()][mensajesPendientes.length]=(Message) event;
			Message[] mensajesPendiente = colaMSG.get((int) myNode.getID());
			mensajesPendiente[mensajesPendiente.length]=(Message) event;
			colaMSG.remove((int) myNode.getID());
			colaMSG.put((int) myNode.getID(), mensajesPendiente);
		}
		// Veo si el tocken esta en el nodo
		if(tokenShip.getID()==myNode.getID())
		{
			if(((ExampleNode) myNode).getTockenFlag()==true)
			{
				Message[] mensajesPendiente = colaMSG.get((int) myNode.getID());
				//Message[] tempMessage=mensajesPendientes[(int) myNode.getID()];
				for(int i=0;i<mensajesPendiente.length;i++)
				{
					sendmessage(myNode, layerId, mensajesPendiente[i]);
				}
				colaMSG.remove((int) myNode.getID());
				((ExampleNode) myNode).setTockenFlag(false);
			}
			tokenShip=(ExampleNode) ((Linkable) myNode.getProtocol(0)).getNeighbor(0);
			
		}
		/*if(event.)//evento es Token
			if(utilizoRC){
				Message message = (Message) event;
				sendmessage(myNode, layerId, message);
				Observer.cantDeUsosDelToken.add(1);
			} else {
				                                                                                                            
			}
		else
			myNode.requieroToken()*/
	}

	public void sendmessage(Node currentNode, int layerId, Object message) {
		/**
		 * Random degree
		 */
		int randDegree = CommonState.r.nextInt(((Linkable) currentNode.getProtocol(0)).degree());
		
		/**
		 * sendNode ID del Nodo que se debe enviar
		 */
		Node sendNode = ((Linkable) currentNode.getProtocol(0)).getNeighbor(randDegree);

		System.out.println("CurrentNode: " + currentNode.getID() + " | Degree: " + ((Linkable) currentNode.getProtocol(0)).degree());
		
		for (int i = 0; i < ((Linkable) currentNode.getProtocol(0)).degree(); i++) {
			System.out.println("NeighborNode: "
					+ ((Linkable) currentNode.getProtocol(0)).getNeighbor(i).getID());
		}

		/**
		 * Envió del dato a través de la capa de transporte, la cual enviará
		 * según el ID del emisor y el receptor
		 */
		((Transport) currentNode.getProtocol(transportId)).send(currentNode, sendNode, message, layerId);
		// Otra forma de hacerlo
		// ((Transport)
		// currentNode.getProtocol(FastConfig.getTransport(layerId))).send(currentNode,
		// searchNode(sendNode), message, layerId);

	}

	/**
	 * Constructor por defecto de la capa Layer del protocolo construido
	 * 
	 * @param prefix
	 */
	public Layer(String prefix) {
		/**
		 * Inicialización del Nodo
		 */
		Layer.prefix = prefix;
		transportId = Configuration.getPid(prefix + "." + PAR_TRANSPORT);
		/**
		 * Siguiente capa del protocolo
		 */
		layerId = transportId + 1;
	}

	private Node searchNode(int id) {
		return Network.get(id);
	}

	/**
	 * Definir Clone() para la replicacion de protocolo en nodos
	 */
	public Object clone() {
		Layer dolly = new Layer(Layer.prefix);
		return dolly;
	}
}
