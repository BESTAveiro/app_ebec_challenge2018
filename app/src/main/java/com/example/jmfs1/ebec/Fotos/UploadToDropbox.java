package com.example.jmfs1.ebec.Fotos;

import android.util.Log;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static com.example.jmfs1.ebec.Fotos.Fotografias.className;
import static com.example.jmfs1.ebec.Fotos.PastaDropbox.Todas;

/**
 * Created by Ricardo on 30/09/2016.
 */

public class UploadToDropbox extends Thread
{
    DbxClientV2 client;
    File currentPhoto;

    public UploadToDropbox (DbxClientV2 client, File currentPhoto)
    {
        this.client = client;
        this.currentPhoto = currentPhoto;
    }

    @Override
    public void run ()
    {
        try
        {
            InputStream in = new FileInputStream(currentPhoto);
            Log.d(className, "vai iniciar upload");
            FileMetadata metadata = client.files().uploadBuilder(String.format("%s/%s", Todas.withSeparator, currentPhoto.getName()))
                    .uploadAndFinish(in);
//                                FileMetadata metadata = client.files().uploadBuilder("/b.jpg")
//                                        .uploadAndFinish(in);
            Log.d(className, String.format("%s uploaded com sucesso", currentPhoto.getName()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
