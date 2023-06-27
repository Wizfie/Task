import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class Sync {
    public static String balanceGlobal;
    public static String nameGlobal;
    public static String upcNoGlobal;


    public static void item_transfer() throws Exception {

//        HmacSHA256 _hmac = new HmacSHA256();
        int currentPage = 1;
        boolean isDataAvailable = true;

        DBConnect db = new DBConnect();
        db.local_connect();

        while (isDataAvailable) {
            try {
                URL _item_transfer = new URL("https://4fwvbo.pvt1.accurate.id/accurate/api/item-transfer/list.do" + "?fields=id,number,numericField10,warehouse,referenceWarehouse," + "dateField1,dateField2,charField5,charField1,charField4," + "charField6,charField9,charField8,approvalStatus,description,standardProductCost," + "charField2&sp.page=" + currentPage);

                String read;
                HttpURLConnection _urlConnection = (HttpURLConnection) _item_transfer.openConnection();
                _urlConnection.setDoOutput(true);
                _urlConnection.setRequestMethod("GET");
                _urlConnection.setRequestProperty("Authorization", "Bearer 7bc058e0-5a4f-4f9e-8f7b-b6b46f100c13");
                _urlConnection.setRequestProperty("X-Session-ID", "74669dc2-eded-4b6e-ae78-fe35dd7975c8");

                int codeResponse = _urlConnection.getResponseCode();
                // checking whether the connection has been established or not
                if (codeResponse == HttpURLConnection.HTTP_OK) {
                    // reading the response from the server
                    InputStreamReader isrObj = new InputStreamReader(_urlConnection.getInputStream());
                    BufferedReader bf = new BufferedReader(isrObj);
                    // to store the response from the servers
                    StringBuilder responseStr = new StringBuilder();
                    while ((read = bf.readLine()) != null) {
                        responseStr.append(read);
                    }
                    // closing the BufferedReader
                    bf.close();
                    // disconnecting the connection
                    _urlConnection.disconnect();
                    // print the response
                    JSONObject jsonObj = new JSONObject(responseStr.toString());
                    JSONArray jsonArray = (JSONArray) jsonObj.get("d");
                    System.out.println("Data Pemindahan Barang: ");

                    JSONArray arr = jsonObj.getJSONArray("d");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);


                        // Main properties
                        String approvalStatus = obj.optString("approvalStatus");
                        String description = obj.optString("description");
                        String number = obj.optString("number");
                        String id = obj.optString("id");
                        item_transfer_detail(db, id);

                        // Warehouse
                        JSONObject warehouse = obj.getJSONObject("warehouse");
                        String wareName = warehouse.optString("name");
                        String wareId = warehouse.optString("id");
                        String wareLocationId = warehouse.optString("locationId");
                        String warePic = warehouse.optString("pic");
                        String wareDescription = warehouse.optString("description");
                        String wareOptlock = warehouse.optString("optLock");

                        // referenceWarehouse
                        JSONObject referenceWarehouse = obj.getJSONObject("referenceWarehouse");
                        String refName = referenceWarehouse.optString("name");
                        String refId = referenceWarehouse.optString("id");
                        String refLocationId = referenceWarehouse.optString("locationId");
                        String refPic = referenceWarehouse.optString("pic");
                        String refDescription = referenceWarehouse.optString("description");
                        String refOptlock = referenceWarehouse.optString("optLock");

                        // Insert to Database

//                        Cek Duplikat

                        String checkDuplicateSql = "SELECT COUNT(*) FROM tb_item_transfer WHERE id = ?";
                        PreparedStatement checkDuplicateStatement = db.local_conn.prepareStatement(checkDuplicateSql);
                        checkDuplicateStatement.setString(1, id);
                        ResultSet resultSet = checkDuplicateStatement.executeQuery();
                        resultSet.next();
                        int count = resultSet.getInt(1);

                        if (count > 0) {
                            System.out.println(" Item Transfer Exists --Skip");
                        } else {
//                            Insert
                            String Sql = "INSERT INTO tb_item_transfer " + "(id,approvalStatus,number,description," + "wareName,wareId,wareLocationId,warePic,wareDescription,wareOptlock" + ",refName,refId,refLocationId,refPic,refDescription,refOptlock)" + " VALUE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                            PreparedStatement statement = db.local_conn.prepareStatement(Sql);
                            String[] values = {id, approvalStatus, number, description, wareName, wareId, wareLocationId, warePic, wareDescription, wareOptlock, refName, refId, refLocationId, refPic, refDescription, refOptlock};

                            for (int j = 0; j < values.length; j++) {
                                statement.setString(j + 1, values[j]);
                            }
                            statement.executeUpdate();
                            System.out.println("Insert Item Transfer");


                            statement.close();
                        }
                    }

                    // Periksa jika data kosong
                    if (jsonArray.length() == 0) {
                        System.out.println("DATA Habis");
                        isDataAvailable = false;
                    } else {
                        currentPage++;
                        System.out.println("Pindah Page " + currentPage);
                    }

                } else {
                    System.out.println("GET Request item transfer not work");
                }
                _urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        db.local_closeconnection();

    }

    private static void item_transfer_detail(DBConnect db, String id) {
        try {
            URL item_transfer_detail = new URL("https://4fwvbo.pvt1.accurate.id/accurate/api/item-transfer/detail.do?id=" + id);

            String read;
            HttpURLConnection _urlConnection = (HttpURLConnection) item_transfer_detail.openConnection();
            _urlConnection.setDoOutput(true);
            _urlConnection.setRequestMethod("GET");
            _urlConnection.setRequestProperty("Authorization", "Bearer 7bc058e0-5a4f-4f9e-8f7b-b6b46f100c13");
            _urlConnection.setRequestProperty("X-Session-ID", "74669dc2-eded-4b6e-ae78-fe35dd7975c8");

            int codeResponse = _urlConnection.getResponseCode();
            // checking whether the connection has been established or not
            if (codeResponse == HttpURLConnection.HTTP_OK) {
                // reading the response from the server
                InputStreamReader isrObj = new InputStreamReader(_urlConnection.getInputStream());
                BufferedReader bf = new BufferedReader(isrObj);
                // to store the response from the servers
                StringBuilder responseStr = new StringBuilder();
                while ((read = bf.readLine()) != null) {
                    responseStr.append(read);
                }
                // closing the BufferedReader
                bf.close();
                // disconnecting the connection
                _urlConnection.disconnect();
                // print the response
                JSONObject jsonObj = new JSONObject(responseStr.toString());
                JSONObject obj = (JSONObject) jsonObj.get("d");
//                System.out.println(obj);

                JSONArray detailItemArray = obj.getJSONArray("detailItem");
                for (int i = 0; i < detailItemArray.length(); i++) {
                    JSONObject detailItemObj = detailItemArray.getJSONObject(i);
                    JSONObject itemObj = detailItemObj.getJSONObject("item");

                    // Data Detail
                    id = obj.optString("id");
                    String number = obj.optString("number");
                    String upcNo = itemObj.optString("upcNo");
                    String no = itemObj.optString("no");
                    String name = itemObj.getString("name");
                    String quantity = String.valueOf(detailItemObj.getLong("quantity"));

//                    System.out.println("Data Detail ");
//                    System.out.println(id + " | " + number + " | " + upcNo + " | " + no + " | "+ name + " | " + quantity);

//                    Check if data already exists

                    String checkDuplicateSql = "SELECT no FROM tb_item_transfer_detail WHERE id = ? AND no = ?";
                    PreparedStatement checkDuplicateStatement = db.local_conn.prepareStatement(checkDuplicateSql);
                    checkDuplicateStatement.setString(1, id);
                    checkDuplicateStatement.setString(2, no);
                    ResultSet resultSet = checkDuplicateStatement.executeQuery();

                    if (resultSet.next()) {
                        System.out.println("Item Transfer Detail exists, skip");
                    } else {

                        String Sql = "INSERT INTO tb_item_transfer_detail (id,number,upcNo,no,name,quantity) VALUE (?,?,?,?,?,?)";
                        PreparedStatement statement = db.local_conn.prepareStatement(Sql);
                        statement.setString(1, id);
                        statement.setString(2, number);
                        statement.setString(3, upcNo);
                        statement.setString(4, no);
                        statement.setString(5, name);
                        statement.setString(6, quantity);
                        statement.executeUpdate();

                        System.out.println("Item Transfer Detail Insert");
                    }
                }


            } else {
                System.out.println("Get Detail Not Working");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void item_list() throws Exception {

        DBConnect db = new DBConnect();
        db.local_connect();

        int currentPage = 1;
        boolean isDataAvailable = true;

        while (isDataAvailable) {


            try {
                URL _item_list = new URL("  https://4fwvbo.pvt1.accurate.id/accurate/api/item/list.do?" + "fields=id,name,upcNo,no,availableToSell,itemType,quantity&sp.page=" + currentPage);

                String read;
                HttpURLConnection _urlConnection = (HttpURLConnection) _item_list.openConnection();
                _urlConnection.setDoOutput(true);
                _urlConnection.setRequestMethod("GET");
                _urlConnection.setRequestProperty("Authorization", "Bearer 7bc058e0-5a4f-4f9e-8f7b-b6b46f100c13");
                _urlConnection.setRequestProperty("X-Session-ID", "74669dc2-eded-4b6e-ae78-fe35dd7975c8");

                int codeResponse = _urlConnection.getResponseCode();
                // checking whether the connection has been established or not
                if (codeResponse == HttpURLConnection.HTTP_OK) {
                    // reading the response from the server
                    InputStreamReader isrObj = new InputStreamReader(_urlConnection.getInputStream());
                    BufferedReader bf = new BufferedReader(isrObj);
                    // to store the response from the servers
                    StringBuilder responseStr = new StringBuilder();
                    while ((read = bf.readLine()) != null) {
                        responseStr.append(read);
                    }
                    // closing the BufferedReader
                    bf.close();
                    // disconnecting the connection
                    _urlConnection.disconnect();
                    // print the response
                    JSONObject jsonObj = new JSONObject(responseStr.toString());
                    JSONArray jsonArray = (JSONArray) jsonObj.get("d");
//                System.out.println("Data : \n" + jsonArray);

                    JSONArray arr = jsonObj.getJSONArray("d");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
//                        System.out.println("DATA " +arr);

                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        String upcNo = obj.optString("upcNo");
                        String no = obj.optString("no");
                        String itemType = obj.optString("itemType");
                        String quantity = String.valueOf(obj.getLong("quantity"));
                        String availableToSell = String.valueOf(obj.getInt("availableToSell"));
                        item_detail(id);
//                        System.out.println(id);
                        nameGlobal = name;
                        upcNoGlobal = upcNo;


//                        Cek Duplikat
                        String checkDuplicateSql = "SELECT COUNT(*) FROM tb_item_list WHERE id = ?";
                        PreparedStatement checkDuplicateStatement = db.local_conn.prepareStatement(checkDuplicateSql);
                        checkDuplicateStatement.setString(1, id);
                        ResultSet resultSet = checkDuplicateStatement.executeQuery();
                        resultSet.next();
                        int count = resultSet.getInt(1);

                        if (count > 0) {
                            System.out.println(" Item list Exist --Skip");

                        } else {
//                          Insert to Database
                            String sql = "INSERT INTO tb_item_list (id,name,upcNo,no,itemType,balanceUnit,quantity,availableToSell)" + "VALUE (?,?,?,?,?,?,?,?)";

                            PreparedStatement statement = db.local_conn.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, name);
                            statement.setString(3, upcNo);
                            statement.setString(4, no);
                            statement.setString(5, itemType);
                            statement.setString(6, balanceGlobal);
                            statement.setString(7, quantity);
                            statement.setString(8, availableToSell);
                            statement.executeUpdate();
                            System.out.println("Insert Item List");
                            statement.close();
                        }
                    }
                    // Periksa jika data kosong
                    if (jsonArray.length() == 0) {
                        System.out.println("DATA Habis");
                        isDataAvailable = false;
                    } else {
                        currentPage++;
                        System.out.println("Pindah Page list" + currentPage);
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        db.local_closeconnection();

    }

    static ArrayList<Integer> resultList = new ArrayList<>();

    private static void item_detail(String id) throws Exception {

        DBConnect db = new DBConnect();
        db.local_connect();

        try {
            URL item_detail = new URL("https://4fwvbo.pvt1.accurate.id/accurate/api/item/detail.do?id=" + id);

            String read;
            HttpURLConnection _urlConnection = (HttpURLConnection) item_detail.openConnection();
            _urlConnection.setDoOutput(true);
            _urlConnection.setRequestMethod("GET");
            _urlConnection.setRequestProperty("Authorization", "Bearer 7bc058e0-5a4f-4f9e-8f7b-b6b46f100c13");
            _urlConnection.setRequestProperty("X-Session-ID", "74669dc2-eded-4b6e-ae78-fe35dd7975c8");

            int codeResponse = _urlConnection.getResponseCode();
            // checking whether the connection has been established or not
            if (codeResponse == HttpURLConnection.HTTP_OK) {
                // reading the response from the server
                InputStreamReader isrObj = new InputStreamReader(_urlConnection.getInputStream());
                BufferedReader bf = new BufferedReader(isrObj);
                // to store the response from the servers
                StringBuilder responseStr = new StringBuilder();
                while ((read = bf.readLine()) != null) {
                    responseStr.append(read);
                }
                // closing the BufferedReader
                bf.close();
                // disconnecting the connection
                _urlConnection.disconnect();
                // print the response
                JSONObject jsonObj = new JSONObject(responseStr.toString());
                JSONObject obj = (JSONObject) jsonObj.get("d");

                JSONArray detail_List = obj.getJSONArray("detailWarehouseData");
                for (int i = 0; i < detail_List.length(); i++) {
                    JSONObject data = detail_List.getJSONObject(i);

                    // Data Detail
                    String balanceUnit = data.optString("balanceUnit");
                    if (balanceUnit.startsWith("0")) {
                        balanceUnit = balanceUnit.substring(1);
                    }
                    balanceGlobal = balanceUnit;
                    String nameWh = data.optString("warehouseName");
                    double balance = data.getDouble("balance");
                    String pic = data.optString("pic");
                    String description = data.optString("description");
                    int idWh = data.getInt("id");


//                    System.out.println(balance);

//                    System.out.println("TEST" +" | "+ idWh +" | "+nameWh+" | "+balance+" | "+pic+" | "+description);
//
//                  Check if data already exists
                    String checkDuplicateSql = "SELECT idWh FROM tb_item_detail WHERE id = ? AND idWh = ?";
                    PreparedStatement checkDuplicateStatement = db.local_conn.prepareStatement(checkDuplicateSql);
                    checkDuplicateStatement.setString(1, id);
                    checkDuplicateStatement.setString(2, String.valueOf(idWh));
                    ResultSet resultSet = checkDuplicateStatement.executeQuery();

                    if (resultSet.next()) {
                        System.out.println("Item Detail exists, skip");
                    } else {
//                      Insert to tb_item_detail

                        String sql = "INSERT INTO tb_item_detail " + "(id,name,upcNo,idWh,nameWh,balance,pic,description)" + "VALUES (?,?,?,?,?,?,?,?)";

                        PreparedStatement statement = db.local_conn.prepareStatement(sql);

                        statement.setString(1, id);
                        statement.setString(2, nameGlobal);
                        statement.setString(3, upcNoGlobal);
                        statement.setString(4, String.valueOf(idWh));
                        statement.setString(5, nameWh);
                        statement.setDouble(6, balance);
                        statement.setString(7, pic);
                        statement.setString(8, description);


                        if (balance != 0.0) {
                            statement.executeUpdate();
                            System.out.println("item Detail Insert");
                        }
                    }

                }


            } else {
                System.out.println("Get Detail list  Not Working");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        db.local_closeconnection();


    }

    public static void item_list_stock() throws Exception {
        DBConnect db = new DBConnect();
        db.local_connect();
        int currentPage = 0;
        boolean isRun = true;

        String sql = "SELECT DISTINCT idWh FROM tb_item_detail ";
        ResultSet resultSet = db.local_stmt.executeQuery(sql);
        while (resultSet.next()) {
            int idWh = resultSet.getInt("idWh");
            resultList.add(idWh);
//            resultSet.close();
        }
        for (int idWh : resultList) {
            // Lakukan operasi atau tampilkan nilai idWh
            isRun = true;

            while (isRun)
                try {
                    URL item_list_stock = new URL("https://4fwvbo.pvt1.accurate.id/accurate"
                            + "/api/item/list-stock.do?warehouseId=" + idWh + "&sp.page=" + currentPage);

                    String read;
                    HttpURLConnection _urlConnection = (HttpURLConnection) item_list_stock.openConnection();
                    _urlConnection.setDoOutput(true);
                    _urlConnection.setRequestMethod("GET");
                    _urlConnection.setRequestProperty("Authorization", "Bearer 7bc058e0-5a4f-4f9e-8f7b-b6b46f100c13");
                    _urlConnection.setRequestProperty("X-Session-ID", "74669dc2-eded-4b6e-ae78-fe35dd7975c8");

                    int codeResponse = _urlConnection.getResponseCode();
                    // Memeriksa apakah koneksi berhasil dibuat atau tidak
                    if (codeResponse == HttpURLConnection.HTTP_OK) {
                        // Membaca respons dari server
                        InputStreamReader isrObj = new InputStreamReader(_urlConnection.getInputStream());
                        BufferedReader bf = new BufferedReader(isrObj);
                        // Menyimpan respons dari server
                        StringBuilder responseStr = new StringBuilder();
                        while ((read = bf.readLine()) != null) {
                            responseStr.append(read);
                        }
                        // Menutup BufferedReader
                        bf.close();
                        // Memutus koneksi
                        _urlConnection.disconnect();
                        // Mencetak respons

                        JSONObject jsonObj = new JSONObject(responseStr.toString());
                        JSONArray jsonArray = (JSONArray) jsonObj.get("d");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);

                            String id = data.optString("id");
                            String name = data.optString("name");
                            String no = data.optString("no");
                            String upcNo = data.optString("upcNo");
                            String quantity = data.optString("quantity");

                            String checkQuery = "SELECT idWh FROM tb_item_list_stock WHERE id = ? AND idWh = ?";
                            PreparedStatement checkStatement = db.local_conn2.prepareStatement(checkQuery);
                            checkStatement.setString(1, id);
                            checkStatement.setString(2, String.valueOf(idWh));
                            ResultSet resultSet1 = checkStatement.executeQuery();

                            if (!resultSet1.next()) {
                                String insertQuery = "INSERT INTO tb_item_list_stock (id, no, upcNo, idWh, name, quantity) VALUES (?, ?, ?, ?, ?, ?)";
                                PreparedStatement statement = db.local_conn2.prepareStatement(insertQuery);
                                statement.setString(1, id);
                                statement.setString(2, no);
                                statement.setString(3, upcNo);
                                statement.setString(4, String.valueOf(idWh));
                                statement.setString(5, name);
                                statement.setString(6, quantity);
                                System.out.println("Write  " + idWh);
                                statement.executeUpdate();
                                System.out.println("Insert Stock List");
                                statement.close();
                            } else {
                                // Data dengan idWh yang sama sudah ada
                                System.out.println("Data dengan idWh sudah ada, ");
                            }
                        }

                        if (jsonArray.length() == 0) {
                            System.out.println("Pengambilan data selesai");
                            currentPage = 0;
                            isRun = false;

//
                        } else {
                            currentPage++;
                            System.out.println("Halaman: " + currentPage);


                        }
                    } else {
                        System.out.println("Pengambilan daftar stok tidak berhasil");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    resultSet.next();
                }
        }
        db.local_closeconnection();
    }


    public static void Warehouse_list() throws Exception {
        DBConnect db = new DBConnect();
        db.local_connect();

        boolean isRun = true;
        int currentPage = 1;


        while (isRun)
            try {
                URL Warehouse_list = new URL("https://4fwvbo.pvt1.accurate.id/accurate/api/warehouse/list.do?&sp.page=" + currentPage);
                String read;
                HttpURLConnection _urlConnection = (HttpURLConnection) Warehouse_list.openConnection();
                _urlConnection.setDoOutput(true);
                _urlConnection.setRequestMethod("GET");
                _urlConnection.setRequestProperty("Authorization", "Bearer 7bc058e0-5a4f-4f9e-8f7b-b6b46f100c13");
                _urlConnection.setRequestProperty("X-Session-ID", "74669dc2-eded-4b6e-ae78-fe35dd7975c8");

                int codeResponse = _urlConnection.getResponseCode();
                // checking whether the connection has been established or not
                if (codeResponse == HttpURLConnection.HTTP_OK) {
                    // reading the response from the server
                    InputStreamReader isrObj = new InputStreamReader(_urlConnection.getInputStream());
                    BufferedReader bf = new BufferedReader(isrObj);
                    // to store the response from the servers
                    StringBuilder responseStr = new StringBuilder();
                    while ((read = bf.readLine()) != null) {
                        responseStr.append(read);
                    }
                    // closing the BufferedReader
                    bf.close();
                    // disconnecting the connection
                    _urlConnection.disconnect();
                    // print the response
                    JSONObject jsonObj = new JSONObject(responseStr.toString());
                    JSONArray jsonArray = (JSONArray) jsonObj.get("d");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.optJSONObject(i);

                        int id = data.optInt("id");
                        String name = data.optString("name");
                        String pic = data.optString("pic");
                        String description = data.optString("description");
                        Warehouse_detail(id);

//                        System.out.println(id + " - " + name + " - " + pic + " - " + description);

                        String checkQuery = "SELECT id FROM tb_warehouse_list WHERE id = ?";
                        PreparedStatement checkStatement = db.local_conn.prepareStatement(checkQuery);
                        checkStatement.setInt(1, id);
                        ResultSet resultSet = checkStatement.executeQuery();

                        if (!resultSet.next()) {
                            String sql = "INSERT INTO tb_warehouse_list (id,name,pic,description) VALUE (?,?,?,?)";
                            PreparedStatement statement = db.local_conn2.prepareStatement(sql);
                            statement.setInt(1, id);
                            statement.setString(2, name);
                            statement.setString(3, pic);
                            statement.setString(4, description);
                            statement.executeUpdate();
                            System.out.println("Insert Wh list");
                        } else {
                            System.out.println("data sudah ada");
                        }
                    }
                    if (jsonArray.length() == 0) {
                        System.out.println("Pengambilan data selesai");
                        currentPage = 0;
                        isRun = false;

//
                    } else {
                        currentPage++;
                        System.out.println("Halaman: " + currentPage);
                    }
                } else {
                    System.out.println("Get Fail");
                }
            } catch (ProtocolException e) {
                throw new RuntimeException(e);

            }
        db.local_closeconnection();
    }


    public static void Warehouse_detail(int id) throws Exception {
        DBConnect db = new DBConnect();
        db.local_connect();


            try {
                URL Warehouse_detail = new URL("https://4fwvbo.pvt1.accurate.id/accurate/api/warehouse/detail.do?id="+ id);
                String read;
                HttpURLConnection _urlConnection = (HttpURLConnection) Warehouse_detail.openConnection();
                _urlConnection.setDoOutput(true);
                _urlConnection.setRequestMethod("GET");
                _urlConnection.setRequestProperty("Authorization", "Bearer 7bc058e0-5a4f-4f9e-8f7b-b6b46f100c13");
                _urlConnection.setRequestProperty("X-Session-ID", "74669dc2-eded-4b6e-ae78-fe35dd7975c8");

                int codeResponse = _urlConnection.getResponseCode();
                // checking whether the connection has been established or not
                if (codeResponse == HttpURLConnection.HTTP_OK) {
                    // reading the response from the server
                    InputStreamReader isrObj = new InputStreamReader(_urlConnection.getInputStream());
                    BufferedReader bf = new BufferedReader(isrObj);
                    // to store the response from the servers
                    StringBuilder responseStr = new StringBuilder();
                    while ((read = bf.readLine()) != null) {
                        responseStr.append(read);
                    }
                    // closing the BufferedReader
                    bf.close();
                    // disconnecting the connection
                    _urlConnection.disconnect();
                    // print the response
                    JSONObject jsonObj = new JSONObject(responseStr.toString());
                    JSONObject data = (JSONObject) jsonObj.get("d");

//                        int id = data.optInt("id");
                        String name = data.optString("name");
                        String pic = data.optString("pic");
                        String street = data.optString("street");
                        String city = data.optString("city");
                        String province = data.optString("province");
                        String country = data.optString("country");
                        String description = data.optString("description");

//                        System.out.println(id + " - " + name + " - " + pic + " - "+street+ " - "+city+ " - "+province+ " - "+country+" - " + description);

                        String checkQuery = "SELECT id FROM tb_warehouse_detail WHERE id = ?";
                        PreparedStatement checkStatement = db.local_conn.prepareStatement(checkQuery);
                        checkStatement.setInt(1, id);
                        ResultSet resultSet = checkStatement.executeQuery();

                        if (!resultSet.next()) {
                            String sql = "INSERT INTO tb_warehouse_detail (id,name,pic,street,city,province,country,description) VALUE (?,?,?,?,?,?,?,?)";
                            PreparedStatement statement = db.local_conn2.prepareStatement(sql);
                            statement.setInt(1, id);
                            statement.setString(2, name);
                            statement.setString(3, pic);
                            statement.setString(4, street);
                            statement.setString(5, city);
                            statement.setString(6, province);
                            statement.setString(7, country);
                            statement.setString(8, description);
                            statement.executeUpdate();
                            System.out.println("Insert Wh detail");
                        } else {
                            System.out.println("data sudah ada");
                        }

                } else {
                    System.out.println("Get Fail");
                }
            } catch (ProtocolException e) {
                throw new RuntimeException(e);

            }
        db.local_closeconnection();
    }
}