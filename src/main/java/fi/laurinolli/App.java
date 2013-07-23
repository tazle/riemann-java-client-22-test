package fi.laurinolli;

import com.aphyr.riemann.client.ExceptionReporter;
import com.aphyr.riemann.client.RiemannClient;
import com.aphyr.riemann.client.UdpTransport;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);

        final UdpTransport transport = new UdpTransport(host, port);
        transport.setExceptionReporter(new ExceptionReporter() {
            public void reportException(final Throwable t) {
                System.err.println("Reported ecxception:");
                t.printStackTrace();
            }
        });

        final RiemannClient riemannClient = new RiemannClient(transport);
        while (true) {
            try {
                if (!riemannClient.isConnected()) {
                    riemannClient.connect();
                }

                riemannClient.event()
                        .service("default")
                        .tag("serviceState")
                        .attribute("serviceType", "Foo")
                        .attribute("componentType", "Bar")
                        .state("ok")
                        .description("Whatever")
                        .send();

            } catch (final Exception e) {
                System.err.println("Caught exception");
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                // Foo
            }
        }

    }
}
