package com.example.jmfs1.ebec.Fotos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.example.jmfs1.ebec.R;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.jmfs1.ebec.Fotos.PastaDropbox.Todas;

public class MinhasFotos extends Fragment
{

    static final String className = "MinhasFotos";
    File currentPhoto;
    GridView gv;
    PhotoGVAdapter adapter;
    final File photosDir = new File(Environment.getExternalStorageDirectory() + "/EBEC/Minhas/");
    DbxClientV2 client;

    ArrayList<File> photos = new ArrayList<>();

    final int REQUEST_IMAGE_CAPTURE = 1;



    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_minhas_fotos, container, false);

        view.findViewById(R.id.atualizarFotos).setOnClickListener(takePhoto);
        gv = (GridView) view.findViewById(R.id.gridViewMinhas);
        adapter = new PhotoGVAdapter();
        gv.setAdapter(adapter);

        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        client = new DbxClientV2(config, "QGpEAb8CbtAAAAAAAAAAC8UJg6ldvrThae9CjJVSeJNnLTfyzPENXVFGohw0egoY");

        new Thread(new Runnable()
        {
            @Override
            public void run ()
            {
                try
                {
                    FullAccount account = client.users().getCurrentAccount();
                    Log.d(className, String.format("name : %s", account.getName()));

                    ListFolderResult result = client.files().listFolder(Todas.withSeparator);
                    while (true)
                    {
                        for (Metadata metadata : result.getEntries())
                        {
                            System.out.println(metadata.getPathLower());
                        }

                        if (!result.getHasMore())
                        {
                            break;
                        }

                        result = client.files().listFolderContinue(result.getCursor());
                    }
                }
                catch (DbxException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

        return view;
    }


    View.OnClickListener takePhoto = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if(!checkForStoragePermission()) return;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                // Create the File where the photo should go
                try
                {
                    currentPhoto = createImageFile();
                }
                catch (IOException ex)
                {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (currentPhoto != null)
                {
                    Log.d(className, "file não é null");
                    Uri photoURI = Uri.fromFile(currentPhoto);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                else Log.d(className, "file é null");
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE)
        {
            switch(resultCode)
            {
                case RESULT_OK:
                    Log.d(className, "foto tirada com sucesso");
                    // assim que tiras uma foto ela é enviada para o Dropbox
                    new UploadToDropbox(client, currentPhoto).start();

                    adapter.notifyDataSetChanged();
                    break;
                case RESULT_CANCELED:
                    Log.d(className, "foto não tirada");
                    break;
            }
        }
    }

    public static Bitmap addWaterMark (Bitmap src, String watermark, Point location, int alpha, int red, int green, int blue, int size, boolean underline) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.argb(alpha, red, green, blue));
        paint.setAlpha(alpha);
        paint.setTextSize(size);
        paint.setAntiAlias(true);
        paint.setUnderlineText(underline);
        canvas.drawText(watermark, location.x, location.y, paint);

        return result;
    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.d(className, timeStamp);

        Log.d(className, getContext().getFilesDir().getAbsolutePath()); // este é interno à app
        Log.d(className, getContext().getExternalFilesDir("Innov").getAbsolutePath());
        Log.d(className, Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.d(className, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());

        if (!photosDir.exists()) photosDir.mkdirs();

        File image2 = new File(photosDir + File.separator + timeStamp + ".jpg");
        Log.d(className, image2.getAbsolutePath());

        return image2;
    }

    boolean checkForStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);

            Log.d(className, "request permissions chamada");
            return false;
        }
        return true;
    }

    public static Bitmap getPhotoThumbnail(File image)
    {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 5;
        Log.d("FileHandling","inSampleSize "+opts.inSampleSize);

        return BitmapFactory.decodeFile(image.getPath(), opts);
    }


    class PhotoGVAdapter extends BaseAdapter
    {


        PhotoGVAdapter()
        {
            getPhotos();
        }



        @Override
        public void notifyDataSetChanged()
        {
            getPhotos();
            super.notifyDataSetChanged();
        }

        void getPhotos()
        {
            if(!photosDir.exists())
            {
                if(!photosDir.mkdirs()) return;
            }
            if(!checkForStoragePermission()) return;
            photos = new ArrayList<>(Arrays.asList(photosDir.listFiles(new FileFilter()
            {
                @Override
                public boolean accept(File pathname)
                {
                    return (pathname.getAbsolutePath().endsWith(".jpg")
                            || pathname.getAbsolutePath().endsWith(".jpeg"));
                }
            })));

            for(File tmp : photos)  Log.d(className, tmp.getAbsolutePath());
        }

        @Override
        public int getCount()
        {
            return photos.size();
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                convertView = new ImageView(getContext());
                int width, height;
                width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                height = width;
                ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(width, height);
                convertView.setLayoutParams(lparams);
                ((ImageView)convertView).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }

//            ((ImageView)convertView).setImageBitmap(addWaterMark(getPhotoThumbnail(photos.get(position)), "aqui",
//                    new Point(0, 0), 255, 200, 200, 200, 40, false));
            Glide.with(getContext()).load(photos.get(position)).asBitmap().into((ImageView)convertView);
            return convertView;
        }
    }


}
