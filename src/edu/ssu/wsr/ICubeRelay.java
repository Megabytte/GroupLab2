package edu.ssu.wsr;

import java.net.UnknownHostException;

/**
 * Copyright © 2016 Keith Webb
 */
public class ICubeRelay
{
    private ICubeRelay()
    {
        try
        {
            new Server();
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        new ICubeRelay();
    }
}
