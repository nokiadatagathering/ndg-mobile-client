package br.org.indt.ndg.mobile.submit;

import java.io.IOException;

public interface HttpPostRequest {

    public void cancel();
    public byte[] send() throws IOException;

}
