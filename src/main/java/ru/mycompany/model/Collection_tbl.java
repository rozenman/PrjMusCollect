package ru.mycompany.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Collection_tbl {
    static final String DB_DRIVER = "org.h2.Driver";
    static String DB_CONNECTION = "jdbc:h2:~/test";
    static String DB_USER = "";
    static String DB_PASSWORD = "";

    public static final String TABLE_NAME = "collection";
    public static final String ID_COLUMN = "id";
    public static final String TITLE_COLUMN = "title";
    public static final String ARTISTS_COLUMN = "artists";
    public static final String COMPOSER_COLUMN = "composer";
    public static final String GENRE_COLUMN = "genre";
    public static final String ALBUM_COLUMN = "album";
    public static final String DURATION_COLUMN = "duration";
    public static final String PATH_LINK_COLUMN = "path_link";
    public static final String FNAME_COLUMN = "fname";


    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `" + TABLE_NAME + "` ( `" +
            ID_COLUMN + "` INT NOT NULL AUTO_INCREMENT, `" +
            TITLE_COLUMN + "` VARCHAR(255) NOT NULL, `" +
            ARTISTS_COLUMN + "` VARCHAR(255) NOT NULL, `" +
            COMPOSER_COLUMN + "` VARCHAR(255) NOT NULL, `" +
            GENRE_COLUMN + "` VARCHAR(255) NOT NULL, `" +
            ALBUM_COLUMN + "` VARCHAR(255) NOT NULL, `" +
            DURATION_COLUMN + "` INT NOT NULL, `" +
            PATH_LINK_COLUMN + "` VARCHAR(255) NOT NULL, `" +
            FNAME_COLUMN + "` VARCHAR(255) NOT NULL, " +
            "PRIMARY KEY ( `" + ID_COLUMN + "` )" +
            ")";
    public static final String SQL_FIND_ALL = "select * from `" + TABLE_NAME + "`";
    public static final String SQL_FIND_ID =  SQL_FIND_ALL + " where `" + ID_COLUMN + "` = ?";
    public static final String SQL_FIND_ITEMS = SQL_FIND_ALL + " where `" + PATH_LINK_COLUMN + "` = ?";
    public static final String SQL_FIND_ITEM =  SQL_FIND_ITEMS + " AND `" +
                                                FNAME_COLUMN + "` = ?";

    public static final String SQL_INSERT = "insert into `" + TABLE_NAME + "` (`" +
                                            TITLE_COLUMN + "`, `" +
                                            ARTISTS_COLUMN + "`, `" +
                                            COMPOSER_COLUMN + "`, `" +
                                            GENRE_COLUMN + "`, `" +
                                            ALBUM_COLUMN + "`, `" +
                                            DURATION_COLUMN + "`, `" +
                                            PATH_LINK_COLUMN + "`, `" +
                                            FNAME_COLUMN + "`, " +
            ") values (?, ?, ?, ?, ?, ?, ?, ?)";
//    public static final String SQL_UPDATE_ID = "update `" + TABLE_NAME + "` set ? = ? where `" + ID_COLUMN + "` = ?";
//    public static final String SQL_UPDATE_FNAME = "update `" + TABLE_NAME +
//                                                "` set `?` = ? " +
//                                                "where `" + PATH_LINK_COLUMN + "` = ? AND `" +
//                                                PATH_LINK_COLUMN + "` = ?";
    public static final String SQL_DELETE_ALL = "delete from `" + TABLE_NAME + "`";
    public static final String SQL_DELETE_ID = SQL_DELETE_ALL + " where `" + ID_COLUMN + "` = ?";
    public static final String SQL_DELETE_PATH_LINK = SQL_DELETE_ALL + " where `" + PATH_LINK_COLUMN + "` = ?";
    public static final String SQL_DELETE_FNAME = SQL_DELETE_ALL + " where `"  +PATH_LINK_COLUMN + "` = ? AND `" + FNAME_COLUMN + "` = ?";

    public void Collection_tbl(){

    }

    public Connection ConnectionDB() {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);

        }catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
                    DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("# " + e.getMessage());
        }
        return connection;
    }

    public void create_tbl(){

        long new_ID=0;
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);

            selectPreparedStatement = connection.prepareStatement(SQL_CREATE_TABLE);
            //selectPreparedStatement.setString(1,value);
            selectPreparedStatement.execute();
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }

    }

    private long exec_SQL(String SQL_str, String value){
        long new_ID=0;
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_str);
            selectPreparedStatement.setString(1,value);
            selectPreparedStatement.execute();
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }
        return new_ID;
    }
    public long Add(String title, String Artist, String Composer, String Genre, String Album, int duration, int path_link, String fname){

        long new_ID=Get_id(path_link, fname);

        if (new_ID>0) {
            //Такая запись уже есть
            return -new_ID;
        };

        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            selectPreparedStatement.setString(1,title);
            selectPreparedStatement.setString(2,Artist);
            selectPreparedStatement.setString(3,Composer);
            selectPreparedStatement.setString(4,Genre);
            selectPreparedStatement.setString(5,Album);
            selectPreparedStatement.setLong(6,duration);
            selectPreparedStatement.setLong(7,path_link);
            selectPreparedStatement.setString(8,fname);
            selectPreparedStatement.execute();
            ResultSet rs = selectPreparedStatement.getGeneratedKeys();
            if (rs.next()){
                new_ID = rs.getLong(1);
            }
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }
        return new_ID;
    }

    public long Get_id(int path_link, String fname){
        long new_ID=0;

        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_FIND_ITEM);
            selectPreparedStatement.setLong(1,path_link);
            selectPreparedStatement.setString(2,fname);
            selectPreparedStatement.execute();
            ResultSet rs = selectPreparedStatement.executeQuery();
            if (rs.next()){
                new_ID = rs.getLong(1);
            }
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }
        return new_ID;

    }

    public CollectionItem Get_item(int path_link, String fname){

        CollectionItem new_item = new CollectionItem();

        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_FIND_ITEM);
            selectPreparedStatement.setLong(1,path_link);
            selectPreparedStatement.setString(2,fname);
            selectPreparedStatement.execute();
            ResultSet rs = selectPreparedStatement.executeQuery();
            if (rs.next()){
                new_item = new CollectionItem(
                        -rs.getInt(ID_COLUMN),
                        rs.getString(TITLE_COLUMN),
                        rs.getString(ARTISTS_COLUMN),
                        rs.getString(COMPOSER_COLUMN),
                        rs.getString(GENRE_COLUMN),
                        rs.getString(ALBUM_COLUMN),
                        ""+rs.getInt(DURATION_COLUMN),
                        "-"+path_link+"-",
                        rs.getString(FNAME_COLUMN)
                        );
            }
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }
        return new_item;

    }
    public ObservableList<CollectionItem> Get_items(int path_link) {
        ObservableList<CollectionItem> CollectionItems = FXCollections.observableArrayList();

        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_FIND_ITEMS);
            selectPreparedStatement.setLong(1,path_link);
            //selectPreparedStatement.setString(2,fname);
            selectPreparedStatement.execute();
            ResultSet rs = selectPreparedStatement.executeQuery();
            while (rs.next()){
                CollectionItem new_item = new CollectionItem(
                        -rs.getInt(ID_COLUMN),
                        rs.getString(TITLE_COLUMN),
                        rs.getString(ARTISTS_COLUMN),
                        rs.getString(COMPOSER_COLUMN),
                        rs.getString(GENRE_COLUMN),
                        rs.getString(ALBUM_COLUMN),
                        ""+rs.getInt(DURATION_COLUMN),
                        "-"+path_link+"-",
                        rs.getString(FNAME_COLUMN)
                );
                new_item.print();
                CollectionItems.add(new_item);
            }
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }
        return CollectionItems;

    }

    public String Get_fname(long id){
        String _fname = "";
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_FIND_ID);
            selectPreparedStatement.setLong(1,id);
            selectPreparedStatement.execute();
            ResultSet rs = selectPreparedStatement.executeQuery();
            if (rs.next()){
                //rs.
                _fname = rs.getString(FNAME_COLUMN);
            }
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }
        return _fname;

    }

    public void del_id(long id){
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_DELETE_ID);
            selectPreparedStatement.setLong(1,id);
            //selectPreparedStatement.setString(2,fname);
            selectPreparedStatement.execute();
            ResultSet rs = selectPreparedStatement.getGeneratedKeys();
            if (rs.next()){
                long new_ID = rs.getLong(1);
            }
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }

    }

    public void del_path_link(long path_link){
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_DELETE_PATH_LINK);
            selectPreparedStatement.setLong(1, path_link);
            selectPreparedStatement.execute();
            ResultSet rs = selectPreparedStatement.getGeneratedKeys();
            if (rs.next()){
                long new_ID = rs.getLong(1);
            }
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
                System.out.println("# Exception 2");

            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
                System.out.println("# Exception 1");
            } // end finally try

        }

    }

    public long del_fname(int path_link, String fname){
        long new_ID = Get_id(path_link, fname);
        if (new_ID==0) {
            return new_ID;
        }
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_DELETE_FNAME);
            selectPreparedStatement.setLong(1,path_link);
            selectPreparedStatement.setString(2,fname);
            selectPreparedStatement.execute();
            ResultSet rs = selectPreparedStatement.getGeneratedKeys();
            if (rs.next()){
                new_ID = rs.getLong(1);
            }
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }
        return new_ID;
    }


    public void del_all(){
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_DELETE_ALL);
            //sselectPreparedStatement.setLong(1,id);
            selectPreparedStatement.execute();
            ResultSet rs = selectPreparedStatement.getGeneratedKeys();
            if (rs.next()){
                long new_ID = rs.getLong(1);
            }
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
                System.out.println("# Exception 2");

            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
                System.out.println("# Exception 1");
            } // end finally try

        }

    }
/*
    public long update_id(String column, String column_value, long id){

        //long new_ID=Get_item(id);
        // Остутствует имя для переименования
        //if (new_ID == 0) return new_ID;

        //new_ID=Get_id(new_path);
        // Новое имя уже присутствует в таблице
        //if (new_ID != 0) return -new_ID;

        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_UPDATE_ID);
            selectPreparedStatement.setString(1,column);
            selectPreparedStatement.setString(2,column_value);
            selectPreparedStatement.setLong(3,id);
            selectPreparedStatement.executeUpdate();
            //ResultSet rs = selectPreparedStatement.getResultSet();
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }
        //return new_ID;
        return 0;
    }
*/

    /*
    public long update_fname(String column, String column_value, long path_link, String fname){
//        long new_ID=Get_id(path);
        // Остутствует имя для переименования
//        if (new_ID == 0) return new_ID;

        //long new_ID=Get_id(new_path);
        // Новое имя уже присутствует в таблице
        //if (new_ID != 0) return -new_ID;

        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_UPDATE_FNAME);
            selectPreparedStatement.setString(1,column);
            selectPreparedStatement.setString(2,column_value);
            selectPreparedStatement.setLong(3,path_link);
            selectPreparedStatement.setString(4,fname);
            selectPreparedStatement.executeUpdate();
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }
        return 0;
    }

*/
    public void print_tale(){
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        long new_ID;
        String new_title;
        String new_artists;
        String new_composer;
        String new_genre;
        String new_album;
        long new_duration;
        long new_path_link;
        String new_fname;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_FIND_ALL);


            ResultSet rs = selectPreparedStatement.executeQuery();
            while (rs.next()) {
                new_ID = rs.getLong(1);
                new_title = rs.getString(2);
                new_artists = rs.getString(3);
                new_composer = rs.getString(4);
                new_genre = rs.getString(5);
                new_album = rs.getString(6);
                new_duration = rs.getLong(7);
                new_path_link = rs.getLong(8);
                new_fname = rs.getString(9);
                System.out.println(new_ID + "\t," +
                        new_title + "\t," +
                        new_artists + "\t," +
                        new_composer + "\t," +
                        new_genre + "\t," +
                        new_album + "\t," +
                        new_duration + "\t," +
                        new_path_link + "\t," +
                        new_fname);
            }
            connection.commit();
        }catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } finally {
            try {
                if(selectPreparedStatement!=null) selectPreparedStatement.close();;
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try

        }

    }
}
