package ru.mycompany;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import org.h2.tools.DeleteDbFiles;
import ru.mycompany.audio_metadata.Decode_audio;
import ru.mycompany.model.CollectionItem;
import ru.mycompany.model.Collection_tbl;
import ru.mycompany.model.Path_tbl;
import ru.mycompany.tread.FindFiles;

import java.io.File;
import java.io.IOException;

public class Controller {
    //public Controller controller;
    public static byte DEBUG_LEVEL=2;

    private Path_tbl path_tbl;
    private Collection_tbl collection_tbl;
    private ObservableList<CollectionItem> music_collection = FXCollections.observableArrayList();
    private ObservableList<CollectionItem> music_collection_flt = FXCollections.observableArrayList();
    private int FoldersCNT;

    private boolean StopThread;
    private Thread reloadThread;

    //private Decode_audio decode_audio;

    @FXML
    private Button ClearTablesDB;

    @FXML
    private Button ShowTABinLOG;

    @FXML
    private Button Open;

    @FXML
    private Button Stop_btn;

    @FXML
    public Label path_lbl;

    @FXML
    private Label Dir_lbl;

    @FXML
    private TableView<CollectionItem> Collect_Mus_tblw;

    @FXML
    private TableColumn<CollectionItem, Integer> Id_tcol;

    @FXML
    private TableColumn<CollectionItem, String> Title_tcol;

    @FXML
    private TableColumn<CollectionItem, String> Artist_tcol;

    @FXML
    private TableColumn<CollectionItem, String> Compose_tcol;

    @FXML
    private TableColumn<CollectionItem, String> Genre_tcol;

    @FXML
    private TableColumn<CollectionItem, String> Album_tcol;

    @FXML
    private TableColumn<CollectionItem, Integer> Duration_tcol;

    @FXML
    private TableColumn<CollectionItem, String> Path_tcol;

    @FXML
    private TableColumn<CollectionItem, String> File_tcol;

    @FXML
    private Label Line_cnt_lbl;

    @FXML
    private Label Line_filter_cnt_lbl;

    @FXML
    private Label Folders_cnt_lbl;

    @FXML
    private TextField Filtr_text_tf;

    @FXML
    private Button Filtr_Apply;

    @FXML
    void ClearTablesDB_on_Action(ActionEvent event) {
        if (DEBUG_LEVEL>0) {
            System.out.println("ClearTablesDB_onAction");
        }

        // delete the H2 database named 'test' in the user home directory
        //DeleteDbFiles.execute("~", "test", true);

        path_tbl.del_table();
        //path_tbl.print_tale();
        collection_tbl.del_table();
        //collection_tbl.print_tale();
    }

    @FXML
    void Collect_Mus_tblw_onMClicked(MouseEvent event) throws IOException {
        if (DEBUG_LEVEL>0) {
            System.out.println("TableView_onClicked");
        }
        int click_cnt = event.getClickCount();
        //System.out.println("Click count: " + event.getClickCount());
        //System.out.println("GetTarget: " + event.getTarget());
        if (click_cnt>1) {
            int f_index = Collect_Mus_tblw.getSelectionModel().getFocusedIndex();
            CollectionItem item = music_collection.get(f_index);
            //item.print();
            String cur_path = item.getFile_path();
            if (DEBUG_LEVEL>1) {
                System.out.println("PATH: " + cur_path);
            }
            Process process = new ProcessBuilder("thunar",cur_path).start();
        }
        //System.out.println("GetIndex: " + Collect_Mus_tblw.getSelectionModel().getFocusedIndex());

    }

    @FXML
    void Open_on_Action(ActionEvent event) {
        if (DEBUG_LEVEL>0) {
            System.out.println("Open_onAction");
        }
        //Stage primaryStage = Main.getPrimaryStage();

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        configuringDirectoryChooser(directoryChooser);
        //File dir = directoryChooser.showDialog(Stage.getWindows().get(0));
        Node source = (Node) event.getSource();
        File dir = directoryChooser.showDialog(source.getScene().getWindow());
        String bDir;
        if (dir != null) {
            bDir =dir.getAbsolutePath();
            //new FindFilesInfo("FindFilesInfo", Path_lbl.getText()).start();
        } else {
            return;
            //bDir = "/home";
        }

        Dir_lbl.setText(bDir);
        MainFX.prop.setProperty("dir_path", bDir);

        path_tbl.create_tbl();
        collection_tbl.create_tbl();

        show_tableview(bDir);

    }

    public void show_tableview(String bDir){
        if (bDir=="") return;
        reloadThread =
                new Thread(
                        () -> {
                            try {
                                FoldersCNT = 0;
                                Platform.runLater(() -> {
                                    Open.setDisable(true);
                                    ClearTablesDB.setDisable(true);
                                    ShowTABinLOG.setDisable(true);
                                    Stop_btn.setDisable(false);
                                    Line_filter_cnt_lbl.setText("");
                                    music_collection.clear();
                                    music_collection_flt.clear();
                                });
                                find_all_files(bDir);
                                //path_tbl.print_tale();
                                //collection_tbl.print_tale();
                            } catch (final Exception e) {
                                System.out.println("in thread error: "+e);
                            } finally {
                                Platform.runLater(() -> {
                                    getFilterCollection("");
                                    ShowTABinLOG.setDisable(false);
                                    ClearTablesDB.setDisable(false);
                                    Open.setDisable(false);
                                    Stop_btn.setDisable(true);
                                });
                                StopThread = false;

                            }



                        });
        reloadThread.setDaemon(true);
        reloadThread.start();
    }
    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Some Directories");

        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    @FXML
    void ShowTABinLOG_on_Action(ActionEvent event) {
        if (DEBUG_LEVEL>0) {
            System.out.println("ShowTABinLOG_onAction");
        }



        String bDir = Dir_lbl.getText();
        //find_all_files(bDir);
        path_tbl.print_tale();
        collection_tbl.print_tale();
    }

    @FXML
    void Stop_btn_onAction(ActionEvent event) {
        StopThread = true;
    }

    @FXML
    void Filtr_Apply_onAction(ActionEvent event) {
        System.out.println("Filtr_Apply_onAction ()" );
        getFilterCollection(Filtr_text_tf.getText());
    }

    @FXML
    void Filtr_text_tf_onAction(ActionEvent event) {
        System.out.println("Filtr_text_tf_onAction ()" );
        getFilterCollection(Filtr_text_tf.getText());
    }

    @FXML
    void Filtr_text_tf_onTextChanged(InputMethodEvent event) {
        System.out.println("Filtr_text_tf_onTextChanged ()" );

    }

    // инициализируем форму данными
    @FXML
    private void initialize(){
        if (DEBUG_LEVEL>0) {
            System.out.println("Метод Controller.initialize()");
        }

        // устанавливаем тип и значение которое должно хранится в колонке

        Id_tcol.setCellValueFactory(new PropertyValueFactory<CollectionItem, Integer>("id"));
        Title_tcol.setCellValueFactory(new PropertyValueFactory<CollectionItem, String>("Title"));
        Artist_tcol.setCellValueFactory(new PropertyValueFactory<CollectionItem, String>("Artists"));
        Compose_tcol.setCellValueFactory(new PropertyValueFactory<CollectionItem, String>("Compose"));
        Genre_tcol.setCellValueFactory(new PropertyValueFactory<CollectionItem, String>("Genre"));
        Album_tcol.setCellValueFactory(new PropertyValueFactory<CollectionItem, String>("Album"));
        Duration_tcol.setCellValueFactory(new PropertyValueFactory<CollectionItem, Integer>("Duration"));
        Path_tcol.setCellValueFactory(new PropertyValueFactory<CollectionItem, String>("File_path"));
        File_tcol.setCellValueFactory(new PropertyValueFactory<CollectionItem, String>("File_name"));

        // заполняем таблицу данными
        Collect_Mus_tblw.setItems(music_collection_flt);

        path_tbl = new Path_tbl();
        collection_tbl = new Collection_tbl();

        //path_tbl.del_table();
        //collection_tbl.del_table();

        path_tbl.create_tbl();
        collection_tbl.create_tbl();

        String dp = MainFX.prop.getProperty("dir_path");
        //System.out.println("dir_path:" + dp);

        Dir_lbl.setText(dp);

        show_tableview(dp);

        Line_filter_cnt_lbl.setText("");
        Stop_btn.setDisable(true);
    }

    public void find_all_files(String bDir) {

        File dir1 = new File(bDir);
        CollectionItem citem = new CollectionItem();
        // если объект представляет каталог
        long path_ID;
        if (dir1.isDirectory()) {
            path_ID = path_tbl.Get_id(dir1.getAbsolutePath());
            if (path_ID!=0) {
                // Путь есть в базе
                int path_ID_i = (int) path_ID;
                Platform.runLater(() -> {
                    ObservableList<CollectionItem> Citem0;

                    Citem0 = collection_tbl.Get_items(path_ID_i);
                    //music_collection.addAll(Citem0);

                    for (CollectionItem item : Citem0) {
                        if (StopThread){
                            if(DEBUG_LEVEL>1){
                                System.out.println("StopThread = 1 EXIT");
                            }
                            return;
                        }
                        item.setId(music_collection.size());
                        item.setFile_path(dir1.getAbsolutePath());
                        TableView_addLine(item);
                        //music_collection.add(item);

                    }


                });

                    for (File f_item : dir1.listFiles()) {

                    if (f_item.isDirectory()) {
                        FoldersCNT++;
                        if (StopThread){
                            if(DEBUG_LEVEL>1){
                                System.out.println("StopThread = 1 EXIT");
                            }
                            return;
                        }
                        Platform.runLater(() -> {
                                    Folders_cnt_lbl.setText("[" + FoldersCNT + "]");
                                });
                        find_all_files(f_item.getAbsolutePath());
                    }
                    }

            }
            else {
                // Путь в базе отсутствует, добавляем его
                if (StopThread){
                    if(DEBUG_LEVEL>1){
                        System.out.println("StopThread = 1 EXIT");
                    }
                    return;
                }
                path_ID = path_tbl.Add(dir1.getAbsolutePath());
                //long collect_ID;
                // получаем все вложенные объекты в каталоге
                for (File f_item : dir1.listFiles()) {


                    if (f_item.isDirectory()) {
                        FoldersCNT++;
                        Platform.runLater(() -> {
                            Folders_cnt_lbl.setText("["+FoldersCNT+"]");

                        });
                        if (DEBUG_LEVEL>1) {
                            System.out.println(f_item.getName() + "  \t folder");
                            //System.out.println(item.getAbsolutePath() + "  \t folder");
                        }
                        find_all_files(f_item.getAbsolutePath());

                    } else {
                        // это файл

                        if (f_item.getName().endsWith(".mp3") || f_item.getName().endsWith(".flac")) {
                            if (DEBUG_LEVEL>1) {
                                System.out.println(f_item.getName() + "\t file");
                            }

                            String s = f_item.getAbsolutePath();
                            citem = cb_decode_file(f_item);
                            if (DEBUG_LEVEL>1) {
                                citem.print();
                            }


                            if (citem.getFile_name()!="") {
                                int duration = Integer.parseInt (citem.getDuration());
                                long id = collection_tbl.Add(citem.getTitle(), citem.getArtists(), citem.getCompose(), citem.getGenre(), citem.getAlbum(), duration, (int) path_ID, citem.getFile_name());

                                CollectionItem finalCitem = citem;
                                Platform.runLater(() -> {

                                    finalCitem.setId(music_collection.size());
                                    TableView_addLine(finalCitem);
                                    //music_collection.add(finalCitem);

                                    });
                            }

                        }
                    }
                }
            }
        }
    }

    private static CollectionItem cb_decode_file(File fn){
        Decode_audio decode_audio = new Decode_audio();
        CollectionItem item;
        item = decode_audio.decode_info(fn.getAbsolutePath());
        //item.print();
        return item;
    }

    public Controller() {
        if (DEBUG_LEVEL>0) {
            System.out.println("Constructor Controller()");
        }

    }

    public int TableView_addLine(CollectionItem item){
        music_collection.add(item);
        int size = music_collection.size();
        Line_cnt_lbl.setText("[" + size + "]");
        music_collection_flt.add(item);
        size = music_collection_flt.size();
        Line_filter_cnt_lbl.setText("["+size+"]");
        return size;
    }

    public String getcurPath(){
        return Dir_lbl.getText();
    }

    public void getFilterCollection(
            String filterString){
        music_collection_flt.clear();
        for (CollectionItem item : music_collection) {
            if (item.filter_passed(filterString)){
                music_collection_flt.add(item);
            }
        }
        int size = music_collection_flt.size();
        Line_filter_cnt_lbl.setText("["+size+"]");

    }
}
