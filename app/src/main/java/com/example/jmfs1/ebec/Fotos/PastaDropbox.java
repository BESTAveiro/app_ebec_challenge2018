package com.example.jmfs1.ebec.Fotos;

/**
 * Created by Ricardo on 30/09/2016.
 */

public enum PastaDropbox
{
    Todas, Escolhidas;

    String withSeparator;

    PastaDropbox ()
    {
        withSeparator = String.format("/%s", this.toString());
    }
}
