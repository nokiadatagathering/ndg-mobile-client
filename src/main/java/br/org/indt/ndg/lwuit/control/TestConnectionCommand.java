package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;
import br.org.indt.ndg.mobile.submit.TestConnection;

public class TestConnectionCommand extends CommandControl {

    private static TestConnectionCommand instance;

    public static TestConnectionCommand getInstance() {
        if (instance == null)
            instance = new TestConnectionCommand();
        return instance;
    }

    protected Command createCommand() {
        return new Command(Resources.CMD_TEST_CONNECTION);
    }

    protected void doAction(Object parameter) {
        TestConnection.getInstance().doTest();
    }
}
