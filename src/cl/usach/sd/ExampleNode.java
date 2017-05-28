package cl.usach.sd;

import peersim.core.GeneralNode;

public class ExampleNode extends GeneralNode {
	private int count;
	private boolean tockenFlag;//true o false segun necesita o no tocken
	private boolean tengoToken;

	public ExampleNode(String prefix) {
		super(prefix);
		this.setTengoToken(false);
		this.setTockenFlag(false);
		this.setCount(0);
	}
	
	public ExampleNode(String prefix, boolean flag, boolean token) {
		super(prefix);
		this.setCount(0);
		this.setTockenFlag(flag);
		this.setTengoToken(token);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean getTockenFlag() {
		return tockenFlag;
	}

	public void setTockenFlag(boolean tockenFlag) {
		this.tockenFlag = tockenFlag;
	}

	public boolean getTengoToken() {
		return tengoToken;
	}

	public void setTengoToken(boolean tengoToken) {
		this.tengoToken = tengoToken;
	}
}
