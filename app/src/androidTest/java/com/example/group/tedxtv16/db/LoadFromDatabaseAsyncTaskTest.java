package com.example.group.tedxtv16.db;

import android.content.Context;
import android.test.InstrumentationTestCase;

import junit.framework.TestCase;


/**
 * Created by ovidiudanielbarba on 07/03/16.
 */
public class LoadFromDatabaseAsyncTaskTest extends InstrumentationTestCase {


    public void testDoInBackground() throws Exception {
        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();

        LoadFromDatabaseAsyncTask asyncTask = new LoadFromDatabaseAsyncTask(context);
    }

}