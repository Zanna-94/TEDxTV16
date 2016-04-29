package com.tedxtorvergatau.tedxtv16.tedxtv16.db;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.tedxtorvergatau.tedxtv16.tedxtv16.item.ItemType;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by ovidiudanielbarba on 05/03/16.
 */

@SmallTest
public class ItemDAOTest extends InstrumentationTestCase {

    private ItemDAO itemDAO;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        System.out.println("Starting ItemDAO class test");

        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();

        itemDAO = new ItemDAO(context);

    }

    @After
    public void tearDown() throws Exception {
        System.out.println("ItemDAO class test completed");

    }

    @Test
    public void testGetTableName(){
        ItemType itemType = ItemType.NEWS;

        String tableName = itemDAO.getTableName(itemType);

        assertEquals(tableName, "News");
    }

    public void testInsertItem() throws Exception {


    }

    public void testGetAllItems() throws Exception {

    }

    public void testEncodeBitmapToBase64() throws Exception {

    }

    public void testDecodeBitmapFromBase64() throws Exception {

    }
}