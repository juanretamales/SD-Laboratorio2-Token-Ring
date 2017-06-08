package cl.usach.sd;

import java.util.HashMap;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;

public class Layer implements Cloneable, EDProtocol {
	private static final String PAR_TRANSPORT = "transport";
	private static String prefix = null;
	private int transportId;
	private int layerId;
	
	/**
	 * Método en el cual se va a procesar el mensaje que ha llegado al Nodo
	 * desde otro Nodo. Cabe destacar que el mensaje va a ser el evento descrito
	 * en la clase, a través de la simulación de eventos discretos.
	 */
	@Override
	public void processEvent(Node myNode, int layerId, Object event) 
	{
		System.out.println("processEvent:{node:["+myNode.getID()+"],event["+((Message) event).getText()+"]}");
		
		if(((Message) event).getText().equals("Necesito token"))
		{
			/*Necesito token*/
			((ExampleNode) myNode).setTockenFlag(true);
		}
		
		if(((ExampleNode) myNode).getTengoToken()) /*reviso Si contiene TOKEN*/
		{
			if(((ExampleNode) myNode).getTockenFlag())/* reviso si Necesito token*/
			{
				/*consumo el recurso(envio un mensaje random)*/
				Message message = new Message("El nodo:["+myNode.getID()+"] consumio el token");
//				System.out.println(message.getText());
				sendmessage(myNode,layerId,message);
			}
			Message msg = new Message("TOKEN");
			sendmessage(myNode,layerId,msg);
		}
		if(((Message) event).getText().equals("TOKEN"))
		{
			sendmessage(myNode,layerId,event);
		}
//		if(((Message) event).getText().equals("TOKEN"))
//		{
//			Observer.cantDeUsosDelToken.add(1);/*Aumento el observer*/
//			((ExampleNode) myNode).setTockenFlag(false);/*Combio estado del nodo actual para decir que ya no necesita el token*/
//			((ExampleNode) myNode).setTengoToken(false);/*Combio estado del nodo actual para decir que ya tiene el token*/
//			ExampleNode tokenShip=(ExampleNode) ((Linkable) myNode.getProtocol(0)).getNeighbor(0);
//			tokenShip.setTengoToken(true);/*le doy el token a mi vecino a mi vecino // cambio estado del nodo vecino para decir que ya tiene toen*/
//			System.out.println("    Enviando token de ["+myNode.getID()+":"+((ExampleNode) myNode).getTengoToken()+"] a ["+tokenShip.getID()+":"+((ExampleNode) tokenShip).getTengoToken()+"]");
//		}
		/*Reviso si el mensaj es Necesito TOKEN*/
		
//		tokenShip=(ExampleNode) ((Linkable) myNode.getProtocol(0)).getNeighbor(0);
//		System.out.println("Enviando token de ["+myNode.getID()+"] a ["+tokenShip.getID()+"]");
		
	}

	public void sendmessage(Node currentNode, int layerId, Object message) {
		
		int randDegree = CommonState.r.nextInt(((Linkable) currentNode.getProtocol(0)).degree());
		
		Node sendNode = ((Linkable) currentNode.getProtocol(0)).getNeighbor(randDegree);
		
		System.out.println("- sendmessage:(Origen:["+currentNode.getID()+"],Destino:"+sendNode.getID()+",event["+((Message) message).getText()+"])");
		
//		if(((Message) message).getText().equals("Necesito token"))/*Necesito token*/
//		{
//			((ExampleNode) currentNode).setTockenFlag(true);
//		}
		
//		if(((ExampleNode) currentNode).getTengoToken() ) /*reviso Si contiene TOKEN*/
//		{
//			if(((ExampleNode) currentNode).getTockenFlag())/* reviso si Necesito token*/
//			{
//				/*&& !((Message) message).getText().equals("El nodo:[")*/
//				/*consumo el recurso(envio un mensaje random)*/
//				Message msg2 = new Message("El nodo:["+currentNode.getID()+"] consumio el token");
//				System.out.println(msg2.getText());
////				sendmessage(currentNode,layerId,msg2);
//				((Transport) currentNode.getProtocol(transportId)).send(currentNode, sendNode, msg2, layerId);
//			}
//			Message msg = new Message("TOKEN");
//			sendmessage(currentNode,layerId,msg);
//			
//		}
//		System.out.println(((Message) message).getText());
		if(((Message) message).getText().equals("TOKEN"))
		{
			
			Observer.cantDeUsosDelToken.add(1);/*Aumento el observer*/
			((ExampleNode) currentNode).setTockenFlag(false);/*Combio estado del nodo actual para decir que ya no necesita el token*/
			((ExampleNode) currentNode).setTengoToken(false);/*Combio estado del nodo actual para decir que ya tiene el token*/
			ExampleNode tokenShip=(ExampleNode) ((Linkable) currentNode.getProtocol(0)).getNeighbor(0);
			tokenShip.setTengoToken(true);/*le doy el token a mi vecino a mi vecino // cambio estado del nodo vecino para decir que ya tiene toen*/
			System.out.println("    Enviando token de ["+currentNode.getID()+"] a ["+tokenShip.getID()+"]");
//			if(((ExampleNode) tokenShip).getTockenFlag())
//			{
//				Message msg2 = new Message("El nodo:["+tokenShip.getID()+"] consumio el token");
////				System.out.println(message.getText());
//				sendmessage(tokenShip,layerId,msg2);
//			}
//			Message msg3 = new Message("TOKEN");
//			
//			sendmessage(tokenShip,layerId,msg3);
		}
		/**
		 * Envió del dato a través de la capa de transporte, la cual enviará
		 * según el ID del emisor y el receptor
		 */
		if(!((Message) message).getText().equals("TOKEN") && !((Message) message).getText().equals("Necesito token"))
		{
			((Transport) currentNode.getProtocol(transportId)).send(currentNode, sendNode, message, layerId);
		}
		
		// Otra forma de hacerlo
		// ((Transport)
		// currentNode.getProtocol(FastConfig.getTransport(layerId))).send(currentNode,
		// searchNode(sendNode), message, layerId);

	}
//public void sendmessage(Node currentNode, int layerId,Node sendNode, Object message) {
//
//		/**
//		 * Envió del dato a través de la capa de transporte, la cual enviará
//		 * según el ID del emisor y el receptor
//		 */
//		((Transport) currentNode.getProtocol(transportId)).send(currentNode, sendNode, message, layerId);
//		// Otra forma de hacerlo
//		// ((Transport)
//		// currentNode.getProtocol(FastConfig.getTransport(layerId))).send(currentNode,
//		// searchNode(sendNode), message, layerId);
//
//	}

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
