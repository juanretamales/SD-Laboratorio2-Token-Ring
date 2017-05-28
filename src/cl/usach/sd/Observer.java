package cl.usach.sd;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.util.IncrementalStats;

public class Observer implements Control {

	private int layerId;
	private String prefix;

	public static IncrementalStats cantDeUsosDelToken = new IncrementalStats();

	public Observer(String prefix) {
		this.prefix = prefix;
		this.layerId = Configuration.getPid(prefix + ".protocol");
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
		int waitTime=2000;/*Cada 2 segundos ejecuta el siguiente segmento*/
		if(CommonState.getTime()%waitTime==0)
		{
			int encontrado=-1;
			for (int i = 0; i < Network.size(); i++) {
				ExampleNode node = (ExampleNode) Network.get(i);
				if(node.getTengoToken()==true)
				{
					encontrado=(int) node.getID();
					break;
				}
			}
			if(encontrado!=-1)
			{
				System.err.println(" El token lo tiene nodo:["+encontrado+"] ");
			}
			else
			{
				System.err.println("Token perdido, dando al nodo al primero de la red");
				ExampleNode node = (ExampleNode) Network.get(0);
				node.setTengoToken(true);
			}
		}
		return false;
	}

}
