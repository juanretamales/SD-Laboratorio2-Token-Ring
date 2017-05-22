package cl.usach.sd;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;

public class RingTopology implements Control{
	int idLink;
	
	public RingTopology(String prefix) {
		this.idLink = Configuration.getPid(prefix + ".protocol");
		System.out.println("Id Link: " + this.idLink);
	}

	@Override
	public boolean execute() {
		System.out.println("RingTopology:Execute()");
		for (int i = 0; i < Network.size(); i++) {
			ExampleNode node = (ExampleNode) Network.get(i);
			ExampleNode randomVecino = (ExampleNode) Network.get(CommonState.r.nextInt(Network.size()));
			
			String s = String.format("A�adir vecinos [Node=%d] [RandomVecino=%d] [addNeighbor=%d]", 
					node.getID(), 
					randomVecino.getID(),
					0
					);
			System.out.println(s);
			
			boolean exito = ((Linkable) node.getProtocol(this.idLink)).addNeighbor(randomVecino);
			s = String.format("A�adir vecinos [Node=%d] [RandomVecino=%d] [addNeighbor=%d]", 
					node.getID(), 
					randomVecino.getID(),
					exito
					);
			System.out.println(s);
			
			
			
		}

		for (int i = 0; i < Network.size(); i++) {
			ExampleNode node = (ExampleNode) Network.get(i);
			System.out.print("Cant. de vecinos: "
					+ ((Linkable) node.getProtocol(this.idLink)).degree());
			System.out.println(" | Nodo Actual: "
					+ i
					+ " | Nodo vecino: "
					+ ((Linkable) node.getProtocol(this.idLink)).getNeighbor(0)
							.getID());
		}
		
		return false;
	}
}
