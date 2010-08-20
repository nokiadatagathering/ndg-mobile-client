package br.org.indt.ndg.mobile.error;

import br.org.indt.ndg.mobile.Resources;

public class ServerCantWriteResultsException extends Exception {
	public ServerCantWriteResultsException() {
		super(Resources.MSG_SERVER_CANT_WRITE_RESULTS);
	}
}
