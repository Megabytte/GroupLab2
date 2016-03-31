package edu.ssu.wsr;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashSet;

/**
 * Copyright © 2016 Keith Webb
 */

class Server extends WebSocketServer
{
    private WebSocket webGLServer;
    private HashSet<WebSocket> clients;

    Server() throws UnknownHostException
    {
        super(new InetSocketAddress(8080));
        clients = new HashSet<>();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake)
    {
        if(webGLServer == null)
            webGLServer = webSocket;
        else
            clients.add(webSocket);

        System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " connected!");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b)
    {
        if(webSocket == webGLServer)
        {
            System.err.println("WebGL Server Disconnected");
            webGLServer = null;
            for(WebSocket ws : clients)
                ws.close();

            clients.clear();
        }
        else
        {
            clients.remove(webSocket);
            System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " disconnected!");
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String message)
    {
        System.err.println(webSocket + ": " + message);

        webGLServer.send(message);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteBuffer message)
    {
        System.out.println(webSocket + ": " + message);

        webGLServer.send(message);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e)
    {
        e.printStackTrace();
        if(webSocket != null)
        {
            System.out.println("Error with: " + webSocket);
        }
    }

    @Override
    public void onFragment(WebSocket conn, Framedata fragment)
    {
        System.out.println("Received fragment: " + fragment);
    }

    void sendToAll(String text) {
        Collection<WebSocket> con = connections();
        synchronized (con)
        {
            for(WebSocket c : con)
            {
                c.send(text);
            }
        }
    }

    void sendToAll(ByteBuffer buffer) {
        Collection<WebSocket> con = connections();
        synchronized (con)
        {
            for(WebSocket c : con)
            {
                c.send(buffer);
            }
        }
    }

}
