/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author raksha
 */
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.InetSocketAddress;

public class RazorpayHttpServer {
    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/razorpay_checkout", new RazorpayHandler());
        server.setExecutor(null);
        server.start();
    }

    static class RazorpayHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            File file = new File("src/view/razorpay_checkout.html");
            byte[] response = new byte[(int) file.length()];
            new FileInputStream(file).read(response);

            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }
    }
}

