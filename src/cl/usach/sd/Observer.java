package cl.usach.sd;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.util.IncrementalStats;

public class Observer implements Control {

	private int layerId;
	private String prefix;
	private int waitTime;/*Cada X segundos ejecuta verificar nodo*/
	private int lostTime;/*Cada Y segundos ejecuta perder token*/
	private int recoveryTime;

	public static IncrementalStats cantDeUsosDelToken = new IncrementalStats();

	public Observer(String prefix) {
		this.prefix = prefix;
		this.layerId = Configuration.getPid(prefix + ".protocol");
		this.waitTime=Configuration.getInt(prefix + ".waitTime");
		this.lostTime=Configuration.getInt(prefix + ".lostTime");
		this.recoveryTime=Configuration.getInt(prefix + ".recoveryTime");
	}

	@Override
	public boolean execute() {
		
		int size = Network.size();
		for (int i = 0; i < Network.size(); i++) {
			if (!Network.get(i).isUp()) {
				size--;
			}
		}

		String s = String.format("[time=%d]:[with N=%d nodes] [%d Total send message]", CommonState.getTime(), size,
				//(int) message.getSum()
				(int) cantDeUsosDelToken.getSum()
				);

		System.err.println(s);
		/*Revisando la red si alguien tiene el token*/
		/*-Cada %lostTime tiempo buscara el/los nodos y los quitara*/
		if(CommonState.getTime()%this.lostTime==0)
		{
			for (int i = 0; i < Network.size(); i++) {
				ExampleNode node = (ExampleNode) Network.get(i);
				if(node.getTengoToken()==true)
				{
					node.setTengoToken(false);
					System.err.println(" El token lo tenia el nodo:["+node.getID()+"] ");
					//break;
				}
			}
		}
		/*WaitTime es tiempo de espera minimo antes de iniciar recuperacion*/
		if(CommonState.getTime()>this.waitTime)
		{
			/*-Cada %waitTime tiempo buscara el/los nodos y revisara si existe el token, de no encontrar, se aplicaran politica de seleccion*/
			if(CommonState.getTime()%this.recoveryTime==0)
			{
				int encontrado=-1;
				/*Aprovecho de buscar el nodo con id mas grande para aplicar politica de seleccion*/
				ExampleNode nodoMasGrande=(ExampleNode) Network.get(0);
				for (int i = 0; i < Network.size(); i++) {
					ExampleNode node = (ExampleNode) Network.get(i);
	
					if(node.getTengoToken()==true)
					{
						encontrado=(int) node.getID();
						break;
					}
					
					if(node.getID()>nodoMasGrande.getID())
					{
						nodoMasGrande=node;
					}
				}
				if(encontrado!=-1)
				{
					System.err.println(" El token lo tiene nodo:["+encontrado+"] ");
				}
				else
				{
					nodoMasGrande.setTengoToken(true);
					System.err.println("Token perdido, dando al nodo:["+nodoMasGrande.getID()+"]");
					
				}
			}
		}
		return false;
	}

}
