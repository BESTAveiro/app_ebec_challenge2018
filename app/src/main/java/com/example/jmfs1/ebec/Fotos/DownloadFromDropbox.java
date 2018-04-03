package com.example.jmfs1.ebec.Fotos;

import android.os.Environment;
import android.util.Log;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.File;
import java.io.FileOutputStream;

import static com.example.jmfs1.ebec.Fotos.PastaDropbox.Escolhidas;

/**
 * Created by Ricardo on 30/09/2016.
 */

public class DownloadFromDropbox extends Thread
{
    static final String className = "DownloadFromDropbox";

    DbxClientV2 client;
    String fileToDownload;
    DownloadOver dover;

    public DownloadFromDropbox (DbxClientV2 client, String fileToDownload, DownloadOver dover)
    {
        this.client = client;
        this.fileToDownload = fileToDownload;
        this.dover = dover;
    }

    @Override
    public void run ()
    {
        try
        {
            FileOutputStream out = new FileOutputStream(new File
                    (Environment.getExternalStorageDirectory() + "/EBEC/Comunidade/" + fileToDownload));
            Log.d(className, "vai iniciar download");
            FileMetadata metadata = client.files().downloadBuilder(String.format("%s/%s", Escolhidas.withSeparator, fileToDownload))
                    .download(out);
//                                FileMetadata metadata = client.files().uploadBuilder("/b.jpg")
//                                        .uploadAndFinish(in);
            Log.d(className, String.format("%s downloaded com sucesso", fileToDownload));
            dover.downloadOver();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
