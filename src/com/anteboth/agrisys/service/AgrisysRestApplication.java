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

        
        router.attach("/schlagList", SchlagListResource.class);  
        router.attach("/schlag/{id}", SchlagResource.class);
        router.attach("/stammdaten", StammdatenResource.class);
        router.attach("/aktivitaetList/{id}", AktivitaetListResource.class);
        router.attach("/aktivitaet/{id}", AktivitaetResource.class);
        router.attach("/imageUploadUrl", ImageUploadUrlResource.class);

        return router;
    }
}