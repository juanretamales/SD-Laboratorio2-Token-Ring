package cl.usach.sd;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;

public class TrafficGenerator implements Control {
	private final static String PAR_PROT = "protocol";
	private final int layerId;

	boolean initToken;
	
	int step=0;

	public TrafficGenerator(String prefix) {
		layerId = Configuration.getPid(prefix + "." + PAR_PROT);
		this.initToken = false;

	}

	@Override
	public boolean execute() {
//		step++;
//		System.out.println("Traffic generator:["+step+"]");
		if (!this.initToken) {
			System.out.println("Antes de inicializar, no esta inicializado el token");
			// Distribución uniforme entre [0-1]
			double uniformRandom = CommonState.r.nextDouble();
			
			//creo mensaje token para ir pasandolo
			Message token = new Message("TOKEN");
			
			
			
			// Considera cualquier nodo de manera aleatoria de la red
			Node initNode = Network.get(CommonState.r.nextInt(Network.size()));
			
//			((ExampleNode) initNode).setTockenFlag(true);

			int sendNode = CommonState.r.nextInt(Network.size());

			// Se crea un nuevo mensaje
			//Message message = new Message("Number random: " + uniformRandom, sendNode);

			// Y se envía, para realizar la simulación
			// Los parámetros corresponde a:
			// long arg0: Delay del evento
			// Object arg1: Evento enviado
			// Node arg2: Nodo por el cual inicia el envío del dato
			// int arg3: Número de la capa del protocolo que creamos (en este
			// caso
			// de layerId)

			this.initToken = true;
//			EDSimulator.add(0, message, initNode, layerId);
			EDSimulator.add(0, token, initNode, layerId);
			
		} else {
			System.out.println("Después de inicializar, esta inicializado el mensaje");
			// Distribución uniforme entre [0-1]
			double uniformRandom = CommonState.r.nextDouble();

			// Considera cualquier nodo de manera aleatoria de la red
			Node initNode = Network.get(CommonState.r.nextInt(Network.size()));

			((ExampleNode) initNode).setTockenFlag(true);
			
			int sendNode = CommonState.r.nextInt(Network.size());

			// Se crea un nuevo mensaje
			Message message = new Message("Necesito token",sendNode);

			// Y se envía, para realizar la simulación
			// Los parámetros corresponde a:
			// long arg0: Delay del evento
			// Object arg1: Evento enviado
			// Node arg2: Nodo por el cual inicia el envío del dato
			// int arg3: Número de la capa del protocolo que creamos (en este
			// caso
			// de layerId)
			EDSimulator.add(0, message, initNode, layerId);
			//this.initToken = false;
		}

		return false;
	}

}
