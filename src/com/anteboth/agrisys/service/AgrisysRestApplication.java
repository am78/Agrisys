package com.anteboth.agrisys.service;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class AgrisysRestApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a
        // new instance of HelloWorldResource.
        Router router = new Router(getContext());

        
        // Defines a route for the resource "list of flurstueck"  
        router.attach("/schlagList", SchlagListResource.class);  
        // Defines a route for the resource "flurstueck"  
        router.attach("/schlag/{id}", SchlagResource.class);
        
        router.attach("/stammdaten", StammdatenResource.class);

        return router;
    }
}