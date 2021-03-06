package cl.usach.sd;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
	private int mindelayTime;
	private int maxdelayTime;
	
	/**
	 * Método en el cual se va a procesar el mensaje que ha llegado al Nodo
	 * desde otro Nodo. Cabe destacar que el mensaje va a ser el evento descrito
	 * en la clase, a través de la simulación de eventos discretos.
	 */
	@Override
	public void processEvent(Node myNode, int layerId, Object event) 
	{
		/*Por cada evento en layer, busco al nodo con el token, y comparo su tiempo, si es igual o menos a el tiempo actual, envia un mensaje para pasar al token */	
		for (int i = 0; i < Network.size(); i++) 
		{
			ExampleNode node = (ExampleNode) Network.get(i);
			if(node.getTengoToken()==true)
			{
				if(((ExampleNode) node).getTiempoConToken()<=CommonState.getTime())
				{
					System.out.println("El tiempo para usar el token del nodo ["+((ExampleNode) node).getID()+"] expiro, pasando el token");
					Message msg = new Message("TOKEN");
					sendmessage(node,layerId,msg);
					break;
				}
			}
		}
		
		System.out.println("processEvent:{node:["+myNode.getID()+"],event["+((Message) event).getText()+"],onTime:["+CommonState.getTime()+"]}");
		
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
			
//			/*Tiempo de espera random para enviar mensaje token fuente: http://chuwiki.chuidiang.org/index.php?title=Generar_n%C3%BAmeros_aleatorios_en_Java */
//			
//			int delayTime=(int) (Math.floor(Math.random()*this.maxdelayTime+this.mindelayTime));
//			((ExampleNode) myNode).setTiempoConToken(CommonState.getTime()+delayTime);
//			System.out.println("El nodo ["+((ExampleNode) myNode).getID()+"] tendra el token por ["+((ExampleNode) myNode).getTiempoConToken()+"="+CommonState.getTime()+"+"+delayTime+"] tiempo");
			
//			Message msg = new Message("TOKEN");
//			sendmessage(myNode,layerId,msg);
			/*Termine de ocupar el recurso y reviso si mi tiempo para el uso de token termino*/
			if(((ExampleNode) myNode).getTiempoConToken()<=CommonState.getTime())
			{
				Message msg = new Message("TOKEN");
				sendmessage(myNode,layerId,msg);
			}
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
			((ExampleNode) currentNode).setTiempoConToken(0);/*Guardo en cero el tiempo que debo estar con token*/
			ExampleNode tokenShip=(ExampleNode) ((Linkable) currentNode.getProtocol(0)).getNeighbor(0);
			tokenShip.setTengoToken(true);/*le doy el token a mi vecino a mi vecino // cambio estado del nodo vecino para decir que ya tiene toen*/
			/*Tiempo de espera random para enviar mensaje token fuente: http://chuwiki.chuidiang.org/index.php?title=Generar_n%C3%BAmeros_aleatorios_en_Java */
			
			int delayTime=(int) (Math.floor(Math.random()*this.maxdelayTime+this.mindelayTime));
			((ExampleNode) currentNode).setTiempoConToken(CommonState.getTime()+delayTime);
			
//			System.out.println("El nodo ["+((ExampleNode) currentNode).getID()+"] tendra el token por ["+((ExampleNode) currentNode).getTiempoConToken()+"] tiempo");
			
			System.out.println("    Enviando token de ["+currentNode.getID()+"] a ["+tokenShip.getID()+"] y lo mantendra por ["+((ExampleNode) currentNode).getTiempoConToken()+"] tiempo");
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
		maxdelayTime = Configuration.getInt(prefix + ".maxdelay");
		mindelayTime = Configuration.getInt(prefix + ".mindelay");
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
