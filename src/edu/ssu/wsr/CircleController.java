package edu.ssu.wsr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Copyright © 2016 Keith Webb
 */
public class CircleController extends JFrame implements ActionListener
{
    /*
    How To Use
    Run this Java File
    Then go to: http://localhost:63342/WebSocketResearch/page/index.html
     */

    /*
    Work Log

    Tuesday 29th 6:15 to 6:30
     */

    private Server server;
    private JButton up, left, right, down;
    private JTextField amountField;

    private CircleController()
    {
        super("Circle Controller : Keith Webb");
        System.out.println("Creating Server");

        try
        {
            server = new Server();
            server.start();
            System.out.println("Server Launched");
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        up = new JButton("Up");
        left = new JButton("Left");
        right = new JButton("Right");
        down = new JButton("Down");
        amountField = new JTextField();

        up.addActionListener(this);
        left.addActionListener(this);
        right.addActionListener(this);
        down.addActionListener(this);

        add(up, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
        add(down, BorderLayout.SOUTH);
        add(amountField, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                try
                {
                    System.out.println("Shutting Down Server");
                    server.stop();
                }
                catch(IOException | InterruptedException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        setSize(300, 100);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(amountField.getText().equals(""))
            return;

        ByteBuffer byteBuffer = ByteBuffer.allocate(Byte.BYTES + Integer.BYTES);
        int amount = Integer.parseInt(amountField.getText());

        if(e.getSource() == up)
        {
            byteBuffer.put((byte) 2);
            byteBuffer.putInt(-amount);
        }
        else if(e.getSource() == left)
        {
            byteBuffer.put((byte) 1);
            byteBuffer.putInt(-amount);
        }
        else if(e.getSource() == right)
        {
            byteBuffer.put((byte) 1);
            byteBuffer.putInt(amount);
        }
        else if(e.getSource() == down)
        {
            byteBuffer.put((byte) 2);
            byteBuffer.putInt(amount);
        }

        byteBuffer.rewind();
        server.sendToAll(byteBuffer);
    }

    static void startCMDInterface() throws IOException, InterruptedException
    {
        System.out.println("Launching Server");
        Server server = null;
        try
        {
            server = new Server();
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Server Created");

        server.start();

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print(">>> ");
            String in = inputReader.readLine();

            if(in.equals("exit") || in.equals("stop"))
            {
                server.stop();
                return;
            }

            ByteBuffer byteBuffer = ByteBuffer.allocate(Byte.BYTES + Integer.BYTES);

            if(in.charAt(0) == 'x')
            {
                byteBuffer.put((byte) 1);
                byteBuffer.putInt(Integer.parseInt(in.substring(2, in.length())));
                byteBuffer.rewind();
                server.sendToAll(byteBuffer);
            }
            else if(in.charAt(0) == 'y')
            {
                byteBuffer.put((byte) 2);
                byteBuffer.putInt(Integer.parseInt(in.substring(2, in.length())));
                byteBuffer.rewind();
                server.sendToAll(byteBuffer);
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        new CircleController();
        //startCMDInterface();
    }
}
