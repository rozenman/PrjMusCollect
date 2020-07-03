package ru.mycompany;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import org.h2.tools.DeleteDbFiles;
import ru.mycompany.model.CollectionItem;
import ru.mycompany.model.Collection_tbl;
import ru.mycompany.model.Path_tbl;
import ru.mycompany.tread.FindFiles;

import java.io.File;

public class Controller {
    public Controller controller;
    public ObservableList<CollectionItem> music_collection = FXCollections.observableArrayList();

    @FXML
    private Button ClearTablesDB;

    @FXML
    private Button Refresh;

    @FXML
    private Button Open;

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
    void ClearTablesDB_on_Action(ActionEvent event) {
        // delete the H2 database named 'test' in the user home directory
        DeleteDbFiles.execute("~", "test", true);
    }

    @FXML
    void Collect_Mus_tblw_onMClicked(MouseEvent event) {

    }

    @FXML
    void Open_on_Action(ActionEvent event) {
        System.out.println("Open_onAction");
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
            bDir = "/home";
        }

        Dir_lbl.setText(bDir);

        Path_tbl path_tbl = new Path_tbl();
        path_tbl.create_tbl();

        Collection_tbl collection_tbl = new Collection_tbl();
        collection_tbl.create_tbl();

        //System.out.println("BRIR:"+ bDir);
        FindFiles findFiles = new FindFiles(bDir, path_tbl, collection_tbl);
        findFiles.start();

    }

    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Some Directories");

        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
    void geteee(){
        System.out.println("EEEEE");
    }

    @FXML
    void Refresh_on_Action(ActionEvent event) {
        music_collection.add(new CollectionItem(1,"Title","Artists", "Composer", "Genre", "Album", "111","--", "++"));
        final Thread reloadThread =
                new Thread(
                        () -> {


                            while (true) {
                                geteee();
                                try {
                                    Thread.sleep(600);
                                    javafx.application.Platform.runLater(
                                            () -> {
                                                music_collection.add(new CollectionItem(1,"Title","Artists", "Composer", "Genre", "Album", "111","--", "++"));
                                            });
                                } catch (final Exception e) {
                                }
                            }
                        });
        reloadThread.setDaemon(true);
        reloadThread.start();
    }

    // инициализируем форму данными
    @FXML
    private void initialize(){
        System.out.println("Метод Controller.initialize()");
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
        Collect_Mus_tblw.setItems(music_collection);

    }

}
