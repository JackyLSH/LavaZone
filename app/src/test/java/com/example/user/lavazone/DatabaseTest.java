package com.example.user.lavazone;

import org.junit.Test;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DatabaseTest {
    @Test
    public void getItemImg_test() throws Exception {
        Database db = new Database();
        List<Integer> it = db.getItemImg(1);
        for (int i = 0; i<it.size(); i++){
            System.out.println(it.get(i));
        }
    }

    @Test
    public void getStoreImg_test() throws Exception {
        Database db = new Database();
        List<Integer> it = db.getStoreImg(1);
        for (int i = 0; i<it.size(); i++){
            System.out.println(it.get(i));
        }
    }

    @Test
    public void searchItem_test() throws Exception {
        Database db = new Database();
        String[] key = {"table","blue"};
        List<Item> it = db.searchItem(key);
        for (int i = 0; i<it.size(); i++){
            System.out.println(it.get(i).item_id);
            System.out.println(it.get(i).name);
            System.out.println(it.get(i).price);
            System.out.println(it.get(i).store_id);
            System.out.println(it.get(i).post_date);
            System.out.println(it.get(i).item_description);
        }
    }

    @Test
    public void getItemDetailbyID_test() throws Exception{
        Database db = new Database();
        Item it = db.getItemDetailByID(1);
        System.out.println(it.item_id);
        System.out.println(it.name);
        System.out.println(it.price);
        System.out.println(it.store_id);
        System.out.println(it.post_date);
        System.out.println(it.item_description);
    }

    @Test
    public void sentOrderRecord_test() throws Exception {
        Database db = new Database();
        int[] matrix = {2,2};
        boolean it = db.sentOrderRecord("sudo","hk polyu","abc@email.com",99998888,matrix);
        if (it){
            System.out.println("Successfully upload order");
        } else {
            System.out.println("Failed to upload order");
        }
    }

    @Test
    public void getRecentStoreItem_test() throws Exception {
        Database db = new Database();
        List<Item> it = db.getRecentStoreItem(1);
        System.out.println(it.get(0).name);
    }


}