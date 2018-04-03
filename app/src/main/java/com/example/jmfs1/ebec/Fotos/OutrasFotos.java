//package com.example.jmfs1.ebec.Fotos;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.Nullable;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.dropbox.core.DbxException;
//import com.dropbox.core.DbxRequestConfig;
//import com.dropbox.core.v2.DbxClientV2;
//import com.dropbox.core.v2.files.FileMetadata;
//import com.dropbox.core.v2.files.ListFolderResult;
//import com.dropbox.core.v2.files.Metadata;
//import com.example.jmfs1.ebec.R;
//
//import java.io.File;
//import java.io.FileFilter;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import static com.example.jmfs1.ebec.Fotos.PastaDropbox.Escolhidas;
//
//
//public class OutrasFotos extends Fragment
//{
//    static final String className = "OutrasFotos";
//    File currentPhoto;
//    GridView gv;
//    PhotoGVAdapter adapter;
//    final File photosDir = new File(Environment.getExternalStorageDirectory() + "/EBEC/Comunidade/");
//    DbxClientV2 client;
//
//    ArrayList<File> photos = new ArrayList<>();
//
//    ConnectivityManager conManager;
//    NetworkInfo activeNetwork;
//
//    @Override
//    public void onCreate (@Nullable Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        conManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//    }
//
//    @Override
//    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
//        // Inflate the layout for this fragment
//        View view =  inflater.inflate(R.layout.fragment_outras_fotos, container, false);
//
//        gv = (GridView) view.findViewById(R.id.gridViewOutras);
//        adapter = new PhotoGVAdapter();
//        gv.setAdapter(adapter);
//
//        view.findViewById(R.id.atualizarFotos).setOnClickListener(atualizar);
//
//        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
//        client = new DbxClientV2(config, "QGpEAb8CbtAAAAAAAAAAC8UJg6ldvrThae9CjJVSeJNnLTfyzPENXVFGohw0egoY");
//
//        listarEDescarregarFotosNovasDoDropbox();
//
//        return view;
//    }
//
//
//    View.OnClickListener atualizar = new View.OnClickListener()
//    {
//        @Override
//        public void onClick (View v)
//        {
//            // ir buscar mais fotos à pasta
//            listarEDescarregarFotosNovasDoDropbox();
//        }
//    };
//
//    boolean checkForStoragePermission()
//    {
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED)
//        {
//
//
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    100);
//
//            Log.d(className, "request permissions chamada");
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * descarrega as fotos que não estão no telemóvel (na pasta das da comunidade)
//     */
//    void listarEDescarregarFotosNovasDoDropbox ()
//    {
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run ()
//            {
//                try
//                {
//                    // verificar se tem net
//                    if(!checkInternetConnectivity())
//                    {
//                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.fotosCoordLayout)
//                                , "liga-te à net para atualizar", Snackbar.LENGTH_LONG);
//                        snackbar.show();
//                        return;
//                    }
//
//                    // verificar se está ligado à net e depois avisar
//                    ListFolderResult result = client.files().listFolder(Escolhidas.withSeparator);
//                    while (true)
//                    {
//                        for (Metadata metadata : result.getEntries())
//                        {
//                            System.out.println(metadata.getPathLower());
//                            FileMetadata fm = (FileMetadata) metadata;
//                            Log.d(className, fm.getSize()+"");
//
//                            // descarregar o ficheiro se ele não estiver no telemóvel
//                            if(!new File(photosDir + File.separator + metadata.getName()).exists())
//                            {
//                                new DownloadFromDropbox(client, metadata.getName(), new DownloadOver()
//                                {
//                                    @Override
//                                    public void downloadOver ()
//                                    {
//                                        Log.d(className, "downloadOver chamado");
//                                        getActivity().runOnUiThread(new Runnable()
//                                        {
//                                            @Override
//                                            public void run ()
//                                            {
//                                                // isto vai chamar esta função muitas vezes
//                                                adapter.notifyDataSetChanged();
//                                            }
//                                        });
//                                    }
//                                }).start();
//                            }
//                        }
//
//                        if (!result.getHasMore())
//                        {
//                            break;
//                        }
//
//                        result = client.files().listFolderContinue(result.getCursor());
//                    }
//                }
//                catch (DbxException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    boolean checkInternetConnectivity()
//    {
//        activeNetwork = conManager.getActiveNetworkInfo();
//
//        return (activeNetwork != null && activeNetwork.isConnected());
//    }
//
//    class PhotoGVAdapter extends BaseAdapter
//    {
//
//
//        PhotoGVAdapter()
//        {
//            getPhotos();
//        }
//
//        @Override
//        public void notifyDataSetChanged()
//        {
//            getPhotos();
//            super.notifyDataSetChanged();
//        }
//
//        void getPhotos()
//        {
//            if(!photosDir.exists())
//            {
//                if(!photosDir.mkdirs()) return;
//            }
//            if(!checkForStoragePermission()) return;
//
//            photos = new ArrayList<>(Arrays.asList(photosDir.listFiles(new FileFilter()
//            {
//                @Override
//                public boolean accept(File pathname)
//                {
//                    return (pathname.getAbsolutePath().endsWith(".jpg")
//                            || pathname.getAbsolutePath().endsWith(".jpeg"));
//                }
//            })));
//
//            for(File tmp : photos)  Log.d(className, tmp.getAbsolutePath());
//        }
//
//        @Override
//        public int getCount()
//        {
//            return photos.size();
//        }
//
//        @Override
//        public Object getItem(int position)
//        {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position)
//        {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent)
//        {
//            if(convertView == null)
//            {
//                convertView = new ImageView(getContext());
//                int width, height;
//                width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
//                height = width;
//                ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(width, height);
//                convertView.setLayoutParams(lparams);
//                ((ImageView)convertView).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            }
//
//            //((ImageView)convertView).setImageBitmap(getPhotoThumbnail(photos.get(position)));
//            Glide.with(getContext()).load(photos.get(position)).into((ImageView)convertView);
//            return convertView;
//        }
//    }
//
//}
