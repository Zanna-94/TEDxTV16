package com.tedxtorvergatau.tedxtv16.tedxtv16.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.InstrumentationTestCase;

import com.tedxtorvergatau.tedxtv16.tedxtv16.R;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.Item;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.ItemType;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.SpeakerItem;


import java.util.ArrayList;
import java.util.List;


public class ItemDAOTest extends InstrumentationTestCase {



    public void setUp() throws Exception {
        super.setUp();
        System.out.println("Starting test");

    }

    public void tearDown() throws Exception {

    }
    public void testInsertItem() throws Exception {

        ItemDAO itemDAO = new ItemDAO(this.getInstrumentation().getTargetContext().getApplicationContext());
        Bitmap bitmap = BitmapFactory.decodeResource(this.getInstrumentation().getTargetContext().getResources(), R.mipmap.ic_launcher);
        Item speaker = new SpeakerItem(1,"Ovi",bitmap,"Very Smal","www.google.com");

        itemDAO.insertItem(speaker);

        List<Item> list = itemDAO.getAllItems(ItemType.SPEAKER);
        Item expected = list.get(0);
        Bitmap expectedBitmap = expected.getPhoto();
        assertEquals(expected.getName(),"Ovi");

    }

    public void testInsertItem1() throws Exception {

        ItemDAO itemDAO = new ItemDAO(this.getInstrumentation().getTargetContext().getApplicationContext());
        Bitmap bitmap = BitmapFactory.decodeResource(this.getInstrumentation().getTargetContext().getResources(), R.mipmap.ic_launcher);
        Item speaker = new SpeakerItem(1,"Ovi",bitmap,"Very Smal","www.google.com");
        Item speaker1 = new SpeakerItem(2,"Francesco", bitmap, "Handsome","www.ciao.it");

        itemDAO.insertItem(speaker);
        itemDAO.insertItem(speaker1);

        itemDAO.clearTable(ItemType.SPEAKER);

        List<Item> list = itemDAO.getAllItems(ItemType.SPEAKER);

        assertEquals(list.size(),0);

    }

    public void testOverWriteItemList(){
        ItemDAO itemDAO = new ItemDAO(this.getInstrumentation().getTargetContext().getApplicationContext());
        Bitmap bitmap = BitmapFactory.decodeResource(this.getInstrumentation().getTargetContext().getResources(), R.mipmap.ic_launcher);
        Item speaker = new SpeakerItem(1,"Ovi",bitmap,"Very Smal","www.google.com");
        Item speaker1 = new SpeakerItem(2,"Francesco", bitmap, "Handsome","www.ciao.it");

        List<Item> list = new ArrayList<>();
        list.add(speaker);
        list.add(speaker1);

        itemDAO.overWriteItemList(list);

        list = itemDAO.getAllItems(ItemType.SPEAKER);

        assertEquals(list.size(),2);
    }

    public void testGetTableName() throws Exception {

        ItemDAO itemDAO = new ItemDAO(this.getInstrumentation().getTargetContext().getApplicationContext());
        String expectedTableName = itemDAO.getTableName(ItemType.NEWS);
        assertEquals(expectedTableName,"News");
    }
}