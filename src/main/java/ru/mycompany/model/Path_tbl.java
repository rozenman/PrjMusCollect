package ru.mycompany.model;

import java.sql.*;

public class Path_tbl {
    static final String DB_DRIVER = "org.h2.Driver";
    static String DB_CONNECTION = "jdbc:h2:~/test";
    static String DB_USER = "";
    static String DB_PASSWORD = "";

    public static final String TABLE_NAME = "`path`";
    public static final String ID_COLUMN = "`id`";
    public static final String NAME_COLUMN = "`path`";

    public static final String SQL_FIND_ALL = "select * from " + TABLE_NAME;
    public static final String SQL_FIND_ALL_ID = "select id from " + TABLE_NAME;
    public static final String SQL_FIND_ID = SQL_FIND_ALL_ID + " where " + NAME_COLUMN + " = ?";
    public static final String SQL_INSERT = "insert into " + TABLE_NAME + " (" + NAME_COLUMN + ") values (?)";
    public static final String SQL_UPDATE = "update " + TABLE_NAME + " set " + NAME_COLUMN  + " = ? where " + NAME_COLUMN + " = ?";
    public static final String SQL_UPDATE_ID = "update " + TABLE_NAME + " set " + NAME_COLUMN + " = ? where " + ID_COLUMN + " = ?";
    public static final String SQL_DELETE_ALL = "delete from " + TABLE_NAME;
    public static final String SQL_DELETE_NAME = SQL_DELETE_ALL + " where " + NAME_COLUMN + " = ?";
    public static final String SQL_DELETE_ID = SQL_DELETE_ALL + " where " + ID_COLUMN + " = ?";

    public void Path_tbl(){

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
            selectPreparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS path (id INT NOT NULL AUTO_INCREMENT, path VARCHAR(255) NOT NULL, PRIMARY KEY (id))");
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
    public long Add(String path){

        long new_ID=Get_id(path);
        if (new_ID>0) return  new_ID;

        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            selectPreparedStatement.setString(1,path);
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

    public long Get_id(String path){
        long new_ID=0;
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_FIND_ID);
            selectPreparedStatement.setString(1,path);
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

    public String Get_path(long id){
        String path = "";
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_FIND_ID);
            selectPreparedStatement.setLong(1,id);
            selectPreparedStatement.execute();
            ResultSet rs = selectPreparedStatement.executeQuery();
            if (rs.next()){
                path = rs.getString(1);
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
        return path;

    }

    public void del_path(String path){
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_DELETE_NAME);
            selectPreparedStatement.setString(1,path);
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

    public void del_id(long id){
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_DELETE_ID);
            selectPreparedStatement.setLong(1,id);
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

    public long update(String path, String new_path){

        long new_ID=Get_id(path);
        // Остутствует имя для переименования
        if (new_ID == 0) return new_ID;

        new_ID=Get_id(new_path);
        // Новое имя уже присутствует в таблице
        if (new_ID != 0) return -new_ID;

        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_UPDATE);
            selectPreparedStatement.setString(1,new_path);
            selectPreparedStatement.setString(2,path);
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
        return new_ID;
    }

    public long update_id(long id, String new_path){
//        long new_ID=Get_id(path);
        // Остутствует имя для переименования
//        if (new_ID == 0) return new_ID;

        long new_ID=Get_id(new_path);
        // Новое имя уже присутствует в таблице
        if (new_ID != 0) return -new_ID;

        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_UPDATE_ID);
            selectPreparedStatement.setString(1,new_path);
            selectPreparedStatement.setLong(2,id);
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
        return new_ID;
    }


    public void print_tale(){
        Connection connection = ConnectionDB();
        PreparedStatement selectPreparedStatement = null;
        long new_ID;
        String new_path;
        try {
            connection.setAutoCommit(false);
            selectPreparedStatement = connection.prepareStatement(SQL_FIND_ALL);


            ResultSet rs = selectPreparedStatement.executeQuery();
            while (rs.next()) {
                new_ID = rs.getLong(1);
                new_path = rs.getString(2);
                System.out.println(new_ID + "\t," + new_path);
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
