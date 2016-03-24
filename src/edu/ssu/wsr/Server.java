package edu.ssu.wsr;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * Copyright © 2016 Keith Webb
 */

/*
Input : Software Signals
Output : Player Changes
 */

public class Server extends WebSocketServer
{
    public Server() throws UnknownHostException
    {
        super(new InetSocketAddress(8080));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake)
    {
        System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " connected!");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b)
    {
        System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " disconnected!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String message)
    {
        System.out.println(webSocket + ": " + message);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteBuffer message)
    {
        System.out.println(webSocket + ": " + message);
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

    public void sendToAll(String text) {
        Collection<WebSocket> con = connections();
        synchronized (con)
        {
            for(WebSocket c : con)
            {
                c.send(text);
            }
        }
    }

    public void sendToAll(ByteBuffer buffer) {
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
