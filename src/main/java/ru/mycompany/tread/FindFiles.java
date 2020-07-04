package ru.mycompany.tread;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import ru.mycompany.Controller;
import ru.mycompany.MainFX;
import ru.mycompany.audio_metadata.Decode_audio;
import ru.mycompany.model.CollectionItem;
import ru.mycompany.model.Collection_tbl;
import ru.mycompany.model.Path_tbl;

import java.io.File;

public class FindFiles extends Thread {
    private String bdir;
    private Path_tbl path_tbl;
    private Collection_tbl collection_tbl;
    //ObservableList<CollectionItem> collectionItems;
    //public ObservableList<CollectionItem> music_collection = FXCollections.observableArrayList();

    public FindFiles(String bDir, Path_tbl path_tbl, Collection_tbl collection_tbl) {
        this.bdir = bDir;
        this.path_tbl = path_tbl;
        this.collection_tbl = collection_tbl;
        //this.collectionItems = collectionItems;
    }

    private static CollectionItem cb_decode_file(File fn){
        Decode_audio decode_audio = new Decode_audio();
        CollectionItem item;
        item = decode_audio.decode_info(fn.getAbsolutePath());
        //item.print();
        return item;
    }

    public void find_all_files(String bDir) {
        File dir1 = new File(bDir);
        CollectionItem citem = new CollectionItem();
        // если объект представляет каталог
        Platform.runLater(() -> {
            //MainFX.controller.music_collection.add(new CollectionItem(2, "Title2", "Artists2", "Composer2", "Genre2", "Album2", "222", "--2", "++2"));
        });
        if (dir1.isDirectory()) {
            long path_ID = path_tbl.Get_id(dir1.getAbsolutePath());
            if (path_ID!=0) {
                // Путь есть в базе
            }
            else {
                path_tbl.Add(dir1.getAbsolutePath());
                // получаем все вложенные объекты в каталоге
                for (File f_item : dir1.listFiles()) {

                    if (f_item.isDirectory()) {

                        System.out.println(f_item.getName() + "  \t folder");
                        //System.out.println(item.getAbsolutePath() + "  \t folder");
                        find_all_files(f_item.getAbsolutePath());

                    } else {
                        if (f_item.getName().endsWith(".mp3") || f_item.getName().endsWith(".flac")) {
                            System.out.println(f_item.getName() + "\t file");
                            String s = f_item.getAbsolutePath();
                            citem = cb_decode_file(f_item);
                            //System.out.println(citem.getDuration());
                            if (citem.getFile_name()!="") {
                                collection_tbl.Add(
                                        citem.getTitle(),
                                        citem.getArtists(),
                                        citem.getCompose(),
                                        citem.getGenre(),
                                        citem.getAlbum(),
                                        Integer.getInteger(citem.getDuration()),
                                        (int) path_ID,
                                        citem.getFile_name());
                            }
                            Platform.runLater(() -> {
                                //collectionItems.add(citem);
                                //collectionItems.add(new CollectionItem(2,"Title2","Artists2", "Composer2", "Genre2", "Album2", "222","--2", "++2"));
                            });
                            //Platform.runLater(() -> {
                            //f_name_lbl.setText("S0");
                            /*
                            int _cnt1 = Collection_tbl.itemsProperty().getValue().size();

                            misic_collection.add(new mus_file(_cnt1, Title, Artists, Composer, Genre,
                                    Album, Duration, item.getParent(), item.getName()));

                             */
                            //});
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Привет из потока " +
                Thread.currentThread().getName());
        //System.out.println("BDIR: " + this.bdir);
        System.out.println(MainFX.controller);
        //Platform.runLater(() -> {

            //MainFX.controller.path_lbl.setText("1234");
                   //MainFX.controller.music_collection.add(new CollectionItem(2, "Title2", "Artists2", "Composer2", "Genre2", "Album2", "222", "--2", "++2"));
                //});
        find_all_files(bdir);
        path_tbl.print_tale();
        collection_tbl.print_tale();
    }


}
