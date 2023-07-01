package com.example.hicaz.data;

import com.example.hicaz.model.*;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DataSource {
    private Connection connection;

    private static final String DB_NAME="anbar.db";

    private static final String DB_PATH="jdbc:sqlite:/Users/muhammadfeyzullayev/Documents/Java/Hicaz/";

    private static final String CONNECTION_STRING=DB_PATH+DB_NAME;


    private PreparedStatement insertKreditor;
    private PreparedStatement insertMal;
    private PreparedStatement queryMal;
    private PreparedStatement existsInAnbar;
    private PreparedStatement increaseAnbar;
    private PreparedStatement insertAnbar;
    private PreparedStatement insertMedaxil;
    private PreparedStatement insertMedaxilFaktura;
    private PreparedStatement insertMexaric;
    private PreparedStatement insertMexaricFaktura;
    private PreparedStatement queryMedaxilItems;
    private PreparedStatement queryMexaricItems;

    private PreparedStatement updateMalValue;

    private PreparedStatement decreaseAnbar;

    private PreparedStatement queryAnbarItem;

    private PreparedStatement deleteMedaxil;
    private PreparedStatement deleteMedaxilFaktura;
    private PreparedStatement deleteMexaric;
    private PreparedStatement deleteMexaricFaktura;

    private PreparedStatement increaseAnbarForDeletedMexaric;

    private PreparedStatement deleteMal;
    private PreparedStatement querymedaxilIdKreditor;

    private PreparedStatement getTarixMedaxil;


    private static final String TABLE_ANBAR="Anbar";
    private static final String COLUMN_ANBAR_NR="Nr";
    private static final String COLUMN_ANBAR_MAL="Mal";
    private static final String COLUMN_ANBAR_VAHID="Vahid";
    private static final String COLUMN_ANBAR_CEKI="Ceki";
    private static final String COLUMN_ANBAR_mebleg="mebleg";

    private static final String TABLE_MAL="Mal";
    private static final String COLUMN_MAL_NR="Nr";
    private static final String COLUMN_MAL_AD ="Ad";
    private static final String COLUMN_MAL_Vahid="Vahid";
    private static final String COLUMN_MAL_ORTAQIYMET="OrtaGiymet";

    private static final String TABLE_MEDAXIL="Medaxil";
    private static final String COLUMN_MEDAXIL_NR="Nr";
    private static final String COLUMN_MEDAXIL_TARIX="Tarix";
    private static final String COLUMN_MEDAXIL_KREDITOR="Kreditor";
    private static final String COLUMN_MEDAXIL_MEBLEG="Mebleg";


    private static final String TABLE_MEDAXILFAKTURA="MedaxilFaktura";
    private static final String COLUMN_MEDAXILFAKTURA_NR="Nr";
    private static final String COLUMN_MEDAXILFAKTURA_MALNR="MalNr";
    private static final String COLUMN_MEDAXILFAKTURA_MAL="Mal";
    private static final String COLUMN_MEDAXILFAKTURA_VAHID="Vahid";
    private static final String COLUMN_MEDAXILFAKTURA_CEKI="Ceki";
    private static final String COLUMN_MEDAXILFAKTURA_MEBLEG="mebleg";
    private static final String COLUMN_MEDAXILFAKTURA_MEDAXILNR ="MedaxilNr";

    private static final String TABLE_MEXARIC="Mexaric";
    private static final String COLUMN_MEXARIC_NR="Nr";
    private static final String COLUMN_MEXARIC_TARIX="Tarix";
    private static final String COLUMN_MEXARIC_TEHVILALAN="TehvilAlan";


    private static final String TABLE_MEXARICFAKTURA="MexaricFaktura";
    private static final String COLUMN_MEXARICFAKTURA_NR="Nr";
    private static final String COLUMN_MEXARICFAKTURA_MALNR="MalNr";
    private static final String COLUMN_MEXARICFAKTURA_MAL="Mal";
    private static final String COLUMN_MEXARICFAKTURA_VAHID="Vahid";
    private static final String COLUMN_MEXARICFAKTURA_CEKI="Ceki";
    private static final String COLUMN_MEXARICFAKTURA_MEXARICNR="MexaricNr";

    private static final String TABLE_USER="User";
    private static final String COLUMN_USER_NR="Nr";
    private static final String COLUMN_USER_AD="Ad";
    private static final String COLUMN_USER_PASSWORD="Password";

    private static final String TABLE_KREDITOR="Kreditor";
    private static final String COLUMN_KREDITOR_NR="nr";

    private static final String COLUMN_KREDITOR_NAME="name";


    private static final String INSERT_KREDITOR="INSERT INTO " + TABLE_KREDITOR + "( " + COLUMN_KREDITOR_NAME + ") VALUES(?)";
    private static final String INSERT_MAL="INSERT INTO " + TABLE_MAL + "(" + COLUMN_MAL_AD +"," + COLUMN_MAL_Vahid +","
            +COLUMN_MAL_ORTAQIYMET+ ")"
            + " VALUES(?,?,?)";
    private static final String UPDATE_MAL_VALUE="UPDATE " + TABLE_MAL + " SET " + COLUMN_MAL_ORTAQIYMET  + " = ? "
    + " WHERE " + COLUMN_MAL_NR + " = ?";

    private static final String INSERT_ANBAR="INSERT INTO " + TABLE_ANBAR + "(" + COLUMN_ANBAR_MAL + ", " + COLUMN_ANBAR_VAHID
            + ", " + COLUMN_ANBAR_CEKI + ", " + COLUMN_ANBAR_mebleg +")" + " VALUES(?,?,?,?)";

    private static final String INSERT_MEDAXIL="INSERT INTO " + TABLE_MEDAXIL + "("  + COLUMN_MEDAXIL_TARIX + ", " +
            COLUMN_MEDAXIL_KREDITOR + ", " + COLUMN_MEDAXIL_MEBLEG + ") VALUES(?,?,?)";
    private static final String INSERT_MEDAXILFAKTURA="INSERT INTO " + TABLE_MEDAXILFAKTURA + "(" + COLUMN_MEDAXILFAKTURA_MALNR + ", "
            + COLUMN_MEDAXILFAKTURA_MAL + ", " + COLUMN_MEDAXILFAKTURA_VAHID + ", " + COLUMN_MEDAXILFAKTURA_CEKI + ", " +
            COLUMN_MEDAXILFAKTURA_MEBLEG + ", " + COLUMN_MEDAXILFAKTURA_MEDAXILNR + ") VALUES(?,?,?,?,?,?)";

    private static final String INSERT_MEXARIC="INSERT INTO " + TABLE_MEXARIC + "(" + COLUMN_MEXARIC_TARIX + ", "
            + COLUMN_MEXARIC_TEHVILALAN + ") VALUES (?,?)";

    private static final String INSERT_MEXARICFAKTURA="INSERT INTO " + TABLE_MEXARICFAKTURA + "(" +
            COLUMN_MEXARICFAKTURA_MALNR +", " + COLUMN_MEXARICFAKTURA_MAL + ", " + COLUMN_MEXARICFAKTURA_VAHID
            + ", " + COLUMN_MEXARICFAKTURA_CEKI + ", " + COLUMN_MEXARICFAKTURA_MEXARICNR
            + ") VALUES(?,?,?,?,?)";


    private static final String INCREASE_ANBAR="UPDATE " + TABLE_ANBAR +" SET " + COLUMN_ANBAR_CEKI + " = " + COLUMN_ANBAR_CEKI
            + " + ?" +", " + COLUMN_ANBAR_mebleg + " = " + COLUMN_ANBAR_mebleg + " + ? WHERE " + COLUMN_ANBAR_MAL + " = ?";
    private static final String INCREASE_ANBAR_FOR_DELETED_MEXARIC="UPDATE " + TABLE_ANBAR +" SET " + COLUMN_ANBAR_CEKI + " = " + COLUMN_ANBAR_CEKI
            + " + ?" + " WHERE " + COLUMN_ANBAR_MAL + " = ?";

    private static final String DECREASE_ANBAR="UPDATE " + TABLE_ANBAR +" SET " + COLUMN_ANBAR_CEKI + " = " + COLUMN_ANBAR_CEKI
            + " - ?" +", " + COLUMN_ANBAR_mebleg + " = ? " + " WHERE " + COLUMN_ANBAR_MAL + " = ?";
    private static final String QUERY_MAL ="SELECT * FROM " + TABLE_MAL + " WHERE " + COLUMN_MAL_AD + " = ?";

    private static final String QUERY_KREDITOR="SELECT * FROM " + TABLE_KREDITOR;

    private static final String EXIST_IN_ANBAR="SELECT COUNT(*) FROM " + TABLE_ANBAR + " WHERE " + COLUMN_ANBAR_MAL + " = ?";

    private static final String QUERY_ANBAR_ITEM="SELECT * FROM " + TABLE_ANBAR + " WHERE " + COLUMN_ANBAR_MAL +" = ?";

    private static final String QUERY_USERS="SELECT * FROM " + TABLE_USER;

    private static final String QUERY_ALL_MEDAXIL="SELECT * FROM " + TABLE_MEDAXIL;

    private static final String QUERY_ALL_MEXARIC="SELECT * FROM " + TABLE_MEXARIC;

    private static final String QUERY_ALL_MAL="SELECT * FROM " + TABLE_MAL;

    private static final String GET_TARIX_MEDAXOL="SELECT " + COLUMN_MEDAXIL_TARIX + " FROM " + TABLE_MEDAXIL +
            " WHERE " + COLUMN_MEDAXIL_NR + " = ?";

    private static final String QUERY_MEDAXIL_ID_KREDITOR="SELECT " + COLUMN_MEDAXIL_NR + " FROM " + TABLE_MEDAXIL + " WHERE " + COLUMN_MEDAXIL_KREDITOR +
             " = ?";

    private static final String QUERY_MEDAXILITEMS="SELECT * FROM " + TABLE_MEDAXILFAKTURA + " WHERE " + COLUMN_MEDAXILFAKTURA_MEDAXILNR + " = ?";

    private static final String QUERY_MEXARICITEMS="SELECT * FROM " + TABLE_MEXARICFAKTURA + " WHERE " + COLUMN_MEXARICFAKTURA_MEXARICNR + " = ?";

    private static final String QUERY_ANBARITEMS="SELECT * FROM " + TABLE_ANBAR ;

    private static final String DELETE_MEDAXIL="DELETE FROM " + TABLE_MEDAXIL + " WHERE " +COLUMN_MEDAXIL_NR + " = ?";
    private static final String DELETE_MAL="DELETE FROM " + TABLE_MAL  + " WHERE " + COLUMN_MAL_NR + " =?";
    private static final String DELETE_MEDAXIL_FAKTURA="DELETE FROM " + TABLE_MEDAXILFAKTURA + " WHERE " +COLUMN_MEDAXILFAKTURA_MEDAXILNR + " = ?";
    private static final String DELETE_MEXARIC="DELETE FROM " + TABLE_MEXARIC + " WHERE " +COLUMN_MEXARIC_NR + " = ?";
    private static final String DELETE_MEXARIC_FAKTURA="DELETE FROM " + TABLE_MEXARICFAKTURA + " WHERE " +COLUMN_MEXARICFAKTURA_MEXARICNR + " = ?";



    public void open(){
        try {
            connection=DriverManager.getConnection(CONNECTION_STRING);
            insertKreditor=connection.prepareStatement(INSERT_KREDITOR);
            insertMal=connection.prepareStatement(INSERT_MAL);
            queryMal=connection.prepareStatement(QUERY_MAL);
            existsInAnbar= connection.prepareStatement(EXIST_IN_ANBAR);
            increaseAnbar=connection.prepareStatement(INCREASE_ANBAR);
            insertAnbar=connection.prepareStatement(INSERT_ANBAR);
            insertMedaxil = connection.prepareStatement(INSERT_MEDAXIL);
            insertMedaxilFaktura=connection.prepareStatement(INSERT_MEDAXILFAKTURA);
            insertMexaric = connection.prepareStatement(INSERT_MEXARIC);
            insertMexaricFaktura = connection.prepareStatement(INSERT_MEXARICFAKTURA);
            updateMalValue = connection.prepareStatement(UPDATE_MAL_VALUE);
            decreaseAnbar= connection.prepareStatement(DECREASE_ANBAR);
            queryAnbarItem= connection.prepareStatement(QUERY_ANBAR_ITEM);
            queryMedaxilItems=connection.prepareStatement(QUERY_MEDAXILITEMS);
            queryMexaricItems=connection.prepareStatement(QUERY_MEXARICITEMS);
            deleteMedaxil=connection.prepareStatement(DELETE_MEDAXIL);
            deleteMedaxilFaktura=connection.prepareStatement(DELETE_MEDAXIL_FAKTURA);
            deleteMal= connection.prepareStatement(DELETE_MAL);
            deleteMexaric=connection.prepareStatement(DELETE_MEXARIC);
            deleteMexaricFaktura=connection.prepareStatement(DELETE_MEXARIC_FAKTURA);
            increaseAnbarForDeletedMexaric=connection.prepareStatement(INCREASE_ANBAR_FOR_DELETED_MEXARIC);
            querymedaxilIdKreditor=connection.prepareStatement(QUERY_MEDAXIL_ID_KREDITOR);
            getTarixMedaxil=connection.prepareStatement(GET_TARIX_MEDAXOL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void close(){
        try {
            if (connection!=null){
                connection.close();
            }
            if (insertMal!=null){
                insertMal.close();
            }
            if (queryMal!=null){
                queryMal.close();
            }
            if (existsInAnbar!=null){
                existsInAnbar.close();
            }
            if (increaseAnbar!=null){
                increaseAnbar.close();
            }
            if (insertMedaxilFaktura!=null){
                insertMedaxilFaktura.close();
            }
            if (insertMedaxil!=null){
                insertMedaxil.close();
            }
            if (insertMexaric!=null){
                insertMexaric.close();
            }
            if (insertMexaricFaktura!=null){
                insertMexaricFaktura.close();
            }
            if (updateMalValue!=null){
                updateMalValue.close();
            }
            if (decreaseAnbar!=null){
                decreaseAnbar.close();
            }
            if (queryAnbarItem!= null){
                queryAnbarItem.close();
            }
            if (queryMedaxilItems!=null){
                queryMedaxilItems.close();
            }if(deleteMedaxil!=null){
                deleteMedaxil.close();
            }if (deleteMedaxilFaktura!=null){
                deleteMedaxilFaktura.close();
            }if (deleteMexaric!=null){
                deleteMexaric.close();
            }if (deleteMexaricFaktura!=null){
                deleteMexaricFaktura.close();
            }if (increaseAnbarForDeletedMexaric!=null){
                increaseAnbarForDeletedMexaric.close();
            }if (queryMexaricItems!=null){
                queryMexaricItems.close();
            }if (insertKreditor!=null){
                insertKreditor.close();
            }if (deleteMal!=null){
                deleteMal.close();
            }if (querymedaxilIdKreditor!=null){
                querymedaxilIdKreditor.close();
            }if (getTarixMedaxil!=null){
                getTarixMedaxil.close();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


    public void insertMal(String malAdi,String vahid,double giymet){
        try {
            if (getMal(malAdi)==null) {
                connection.setAutoCommit(false);
                insertMal.setString(1, malAdi);
                insertMal.setString(2, vahid);
                insertMal.setDouble(3, giymet);
                int nrAaffectedRows = insertMal.executeUpdate();
                if (nrAaffectedRows == 1) {
                    connection.commit();
                } else {
                    throw new SQLException("Failed to insert order ");
                }
            }else{
                throw  new RuntimeException("Eyni adli mali daxil ede bilmezsiniz");
            }

        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
    }

    public Mal getMal(String malAdi){
        try {
            queryMal.setString(1,malAdi);
            ResultSet resultSet= queryMal.executeQuery();
            if (resultSet.next()){
                Mal mal = new Mal();
                mal.setNr(resultSet.getInt(1));
                mal.setAd(resultSet.getString(2));
                mal.setVahid(resultSet.getString(3));
                mal.setOrtaGiymet(resultSet.getDouble(4));
                return mal;
            }
            return null;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean doesExistAnbar(String malAdi){
        boolean exists=false;
        try {
            existsInAnbar.setString(1,malAdi);
            ResultSet resultSet = existsInAnbar.executeQuery();
            if (resultSet.next()){
                int count= resultSet.getInt(1);
                exists=count>0;
            }
        }catch (SQLException e){
            e.getMessage();

        }
        return exists;
    }

    public boolean insertAnbar(String malAdi, double ceki, double mebleg){
        boolean done= false;
        try {
            connection.setAutoCommit(false);
            if (doesExistAnbar(malAdi)){
                increaseAnbar.setDouble(1,ceki);
                increaseAnbar.setDouble(2,mebleg);
                increaseAnbar.setString(3,malAdi);
                int nrAffectedRows= increaseAnbar.executeUpdate();
                if (nrAffectedRows==1){
                    connection.commit();
                    Mal mal = getMal(malAdi);
                    double median= (mal.getOrtaGiymet() + (mebleg/ceki)) /2;
                    updateMalValue.setDouble(1,median);
                    updateMalValue.setInt(2,mal.getNr());
                    updateMalValue.executeUpdate();
                    done=true;
                }
            }else{
                Mal mal = getMal(malAdi);
                insertAnbar.setString(1,mal.getAd());
                insertAnbar.setString(2,mal.getVahid());
                insertAnbar.setDouble(3,ceki);
                insertAnbar.setDouble(4,mebleg);
                int nrAffectedRows= insertAnbar.executeUpdate();
                if (nrAffectedRows==1){
                    connection.commit();
                    double median= (mal.getOrtaGiymet() + (mebleg/ceki)) /2;
                    updateMalValue.setDouble(1,median);
                    updateMalValue.setInt(2,mal.getNr());
                    updateMalValue.executeUpdate();
                    done=true;
                }

            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
        return done;
    }

    private void increaseAnbarForDeletedMexaric(String malAdi,double ceki) {
        try {
            connection.setAutoCommit(false);
            increaseAnbarForDeletedMexaric.setDouble(1, ceki);
            increaseAnbarForDeletedMexaric.setString(2, malAdi);
            int nrAffectedRows = increaseAnbarForDeletedMexaric.executeUpdate();
            if (nrAffectedRows == 1) {
                connection.commit();
            }

        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            } catch (SQLException f) {
                System.out.println("failed to rollback");
            }
        } finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            } catch (SQLException g) {
                System.out.println("couldn't set auto commit to true " + g.getMessage());
            }
        }
    }





    public void insertMedaxilFaktura(String malAdi,double ceki,double mebleg,int medaxilNr){

        try {
            connection.setAutoCommit(false);
            Mal mal =getMal(malAdi);
                insertMedaxilFaktura.setInt(1,mal.getNr());
                insertMedaxilFaktura.setString(2,mal.getAd());
                insertMedaxilFaktura.setString(3,mal.getVahid());
                insertMedaxilFaktura.setDouble(4,ceki);
                insertMedaxilFaktura.setDouble(5,mebleg);
                insertMedaxilFaktura.setDouble(6,medaxilNr);
                int nrAffectedRows= insertMedaxilFaktura.executeUpdate();
                if (nrAffectedRows==1){
                    connection.commit();
                    updateMedaxilMebleg(medaxilNr);
                    insertAnbar(malAdi,ceki,mebleg);
            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
    }
    public List<Kreditor> queryKreditor(){
        try (Statement statement= connection.createStatement();
        ResultSet resultSet= statement.executeQuery(QUERY_KREDITOR)){
            List<Kreditor> kreditorList= new ArrayList<>();
            while (resultSet.next()){
                Kreditor kreditor= new Kreditor();
                kreditor.setNr(resultSet.getInt(1));
                kreditor.setName(resultSet.getString(2));
                kreditorList.add(kreditor);
            }
            return kreditorList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void insertKreditor(String name){
        try {
            connection.setAutoCommit(false);
            insertKreditor.setString(1,name);
            int nrAffectedRows=insertKreditor.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }

    }


    public int insertMedaxil(String kreditor){
        int generetedKey=-1;
        try {
            connection.setAutoCommit(false);
            LocalDate today = LocalDate.now();
            insertMedaxil.setString(1, String.valueOf(today));
            insertMedaxil.setString(2,kreditor);
            insertMedaxil.setDouble(3,0);
            int nrAffectedRows= insertMedaxil.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
                ResultSet generatedKeys = insertMedaxil.getGeneratedKeys();
                if (generatedKeys.next()){
                    generetedKey=generatedKeys.getInt(1);
                }
            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
        return generetedKey;
    }

    private void updateMedaxilMebleg(int medaxilNr) throws SQLException {
        String sql = "UPDATE Medaxil SET Mebleg = (SELECT SUM(mebleg) FROM MedaxilFaktura WHERE MedaxilNr = ?) WHERE Nr = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, medaxilNr);
            statement.setInt(2, medaxilNr);
            statement.executeUpdate();
        }
    }


    public int insertMexaric(String tehvilAlan){
        int generetedKey=-1;
        try {
            connection.setAutoCommit(false);
            LocalDate today = LocalDate.now();
            insertMexaric.setString(1, String.valueOf(today));
            insertMexaric.setString(2,tehvilAlan);
            int nrAffectedRows= insertMexaric.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
                ResultSet generatedKeys = insertMexaric.getGeneratedKeys();
                if (generatedKeys.next()){
                    generetedKey=generatedKeys.getInt(1);
                }
            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
        return generetedKey;
    }

    public boolean insertMexaricFaktura(String malAdi,double ceki,int medaxilNr){
        boolean done= false;
        try {
            connection.setAutoCommit(false);
            Mal mal =getMal(malAdi);
            insertMexaricFaktura.setInt(1,mal.getNr());
            insertMexaricFaktura.setString(2,mal.getAd());
            insertMexaricFaktura.setString(3,mal.getVahid());
            insertMexaricFaktura.setDouble(4,ceki);
            insertMexaricFaktura.setDouble(5,medaxilNr);
            int nrAffectedRows= insertMexaricFaktura.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
                done = decreaseAnbar(malAdi,ceki);
            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
        return done;
    }


    private boolean decreaseAnbar(String item,double ceki){
        boolean done= false;
        try {
            connection.setAutoCommit(false);
            Mal mal = getMal(item);
            decreaseAnbar.setDouble(1,ceki);
            AnbarItem anbarItem = getItem(item);
            double yekunMebleg= anbarItem.getMebleg() - (ceki*mal.getOrtaGiymet());
            decreaseAnbar.setDouble(2,yekunMebleg);
            decreaseAnbar.setString(3,item);
            int nrAffectedRows= decreaseAnbar.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
                done=true;

            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
        return done;


    }
    private boolean decreaseAnbarForMedaxil(String item,double ceki,double mebleg){
        boolean done= false;
        try {
            connection.setAutoCommit(false);
            Mal mal = getMal(item);
            decreaseAnbar.setDouble(1,ceki);
            AnbarItem anbarItem = getItem(item);
            double yekunMebleg= anbarItem.getMebleg() - mebleg;
            decreaseAnbar.setDouble(2,yekunMebleg);
            decreaseAnbar.setString(3,item);
            int nrAffectedRows= decreaseAnbar.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
                done=true;

            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
        return done;


    }


    public AnbarItem getItem(String name){
        AnbarItem item= new AnbarItem();
        try {

            queryAnbarItem.setString(1,name);
            ResultSet resultSet= queryAnbarItem.executeQuery();
            while (resultSet.next()){
                item.setNr(resultSet.getInt(1));
                item.setMal(resultSet.getString(2));
                item.setVahid(resultSet.getString(3));
                item.setCeki(resultSet.getDouble(4));
                item.setMebleg(resultSet.getDouble(5));
            }
        }catch (SQLException  e){
            System.out.println(e.getMessage());
        }
        return item;

    }




    public List<User> queryUsers(){
        try (Statement statement = connection.createStatement();
        ResultSet resultSet= statement.executeQuery(QUERY_USERS)){
            List<User> users= new ArrayList<>();
            while (resultSet.next()){
                User user  = new User();
                user.setNr(resultSet.getInt(1));
                user.setAd(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                users.add(user);
            }
            return users;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Medaxil> queryMedaxil(){
        try (Statement statement = connection.createStatement();
        ResultSet resultSet= statement.executeQuery(QUERY_ALL_MEDAXIL)){
            List<Medaxil> medaxilList = new ArrayList<>();
            while (resultSet.next()){
                Medaxil medaxil=new Medaxil();
                medaxil.setNr(resultSet.getInt(1));
                medaxil.setTarix(resultSet.getString(2));
                medaxil.setKreditor(resultSet.getString(3));
                medaxil.setMebleg(resultSet.getDouble(4));
                medaxilList.add(medaxil);
            }
            return medaxilList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Mal> queryAllMal(){
        try (Statement statement=connection.createStatement();
        ResultSet resultSet=statement.executeQuery(QUERY_ALL_MAL)){
            List<Mal> malList= new ArrayList<>();
            while (resultSet.next()){
                Mal mal= new Mal();
                mal.setNr(resultSet.getInt(1));
                mal.setAd(resultSet.getString(2));
                mal.setVahid(resultSet.getString(3));
                mal.setOrtaGiymet(resultSet.getDouble(4));
                malList.add(mal);
            }
            return malList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<MedaxilFaktura> queryMedaxilItems(int nr){

        try {
            queryMedaxilItems.setInt(1,nr);
            ResultSet resultSet= queryMedaxilItems.executeQuery();
            List<MedaxilFaktura> medaxilFakturaList=  new ArrayList<>();
            while (resultSet.next()){
                MedaxilFaktura medaxilFaktura = new MedaxilFaktura();
                medaxilFaktura.setNr(resultSet.getInt(1));
                medaxilFaktura.setMalNr(resultSet.getInt(2));
                medaxilFaktura.setMal(resultSet.getString(3));
                medaxilFaktura.setVahid(resultSet.getString(4));
                medaxilFaktura.setCeki(resultSet.getDouble(5));
                medaxilFaktura.setMebleg(resultSet.getDouble(6));
                medaxilFaktura.setMedaxilNr(resultSet.getInt(7));
                medaxilFakturaList.add(medaxilFaktura);
            }
            return medaxilFakturaList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Mexaric> queryMexaric(){
        try (Statement statement = connection.createStatement();
             ResultSet resultSet= statement.executeQuery(QUERY_ALL_MEXARIC)){
            List<Mexaric> mexaricList = new ArrayList<>();
            while (resultSet.next()){
                Mexaric mexaric=new Mexaric();
                mexaric.setNr(resultSet.getInt(1));
                mexaric.setTarix(resultSet.getString(2));
                mexaric.setTehvilAlan(resultSet.getString(3));
                mexaricList.add(mexaric);
            }
            return mexaricList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public List<MexaricFaktura> queryMexaricItems(int nr){

        try {
            queryMexaricItems.setInt(1,nr);
            ResultSet resultSet= queryMexaricItems.executeQuery();
            List<MexaricFaktura> mexaricFakturaList=  new ArrayList<>();
            while (resultSet.next()){
                MexaricFaktura mexaricFaktura = new MexaricFaktura();
                mexaricFaktura.setNr(resultSet.getInt(1));
                mexaricFaktura.setMalNr(resultSet.getInt(2));
                mexaricFaktura.setMal(resultSet.getString(3));
                mexaricFaktura.setVahid(resultSet.getString(4));
                mexaricFaktura.setCeki(resultSet.getDouble(5));
                mexaricFaktura.setMexaricNr(resultSet.getInt(6));

                mexaricFakturaList.add(mexaricFaktura);
            }
            return mexaricFakturaList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<AnbarItem> queryAnbarItems(){
        try(Statement statement=connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_ANBARITEMS)) {
            List<AnbarItem> anbarItemList= new ArrayList<>();
            while (resultSet.next()){
                AnbarItem anbarItem= new AnbarItem();
                anbarItem.setNr(resultSet.getInt(1));
                anbarItem.setMal(resultSet.getString(2));
                anbarItem.setVahid(resultSet.getString(3));
                anbarItem.setCeki(resultSet.getDouble(4));
                anbarItem.setMebleg(resultSet.getDouble(5));
                anbarItemList.add(anbarItem);
            }
            return anbarItemList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void deleteMal(int malNr){
        try {
            deleteMal.setInt(1,malNr);
            deleteMal.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteMedaxil(int medaxilNr){
        try {
            deleteMedaxil.setInt(1,medaxilNr);
            int nrAffectedRows = deleteMedaxil.executeUpdate();
            if (nrAffectedRows == 1) {
                List<MedaxilFaktura> medaxilFakturaList= queryMedaxilItems(medaxilNr);
                for (MedaxilFaktura medaxilFaktura:medaxilFakturaList){
                    decreaseAnbarForMedaxil(medaxilFaktura.getMal(),medaxilFaktura.getCeki(),medaxilFaktura.getMebleg());
                }
                deleteMedaxilFaktura.setInt(1,medaxilNr);
                deleteMedaxilFaktura.executeUpdate();
            } else {
                System.out.println("Failed to delete sale.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteMexaric(int medaxilNr){
        try {
            deleteMexaric.setInt(1,medaxilNr);
            int nrAffectedRows = deleteMexaric.executeUpdate();
            if (nrAffectedRows == 1) {
                List<MexaricFaktura> mexaricFakturaList= queryMexaricItems(medaxilNr);
                for (MexaricFaktura mexaricFaktura:mexaricFakturaList){
                    increaseAnbarForDeletedMexaric(mexaricFaktura.getMal(),mexaricFaktura.getCeki());
                }
                deleteMexaricFaktura.setInt(1,medaxilNr);
                deleteMexaricFaktura.executeUpdate();
            } else {
                System.out.println("Failed to delete sale.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Integer> kreditorMedaxilNums(String kreditorName){
        try {
            querymedaxilIdKreditor.setString(1,kreditorName);
            ResultSet resultSet= querymedaxilIdKreditor.executeQuery();
            List<Integer> medaxilIds= new ArrayList<>();
            while (resultSet.next()){
                medaxilIds.add(resultSet.getInt(1));
            }
            return medaxilIds;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getMedaxilTarix(int nr){
        try {
            getTarixMedaxil.setInt(1,nr);
            ResultSet resultSet = getTarixMedaxil.executeQuery();
            return resultSet.getString(1);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }











}
