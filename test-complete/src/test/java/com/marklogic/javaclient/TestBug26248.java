package com.marklogic.javaclient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.marklogic.client.document.TextDocumentManager;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.StringHandle;

public class TestBug26248 {
	DatabaseClient client ;
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		client.release();
	}

	 static class DocWriter implements Runnable {
		  static boolean isResendable = true;
		  String id;
		  String resendableDoc;
		  InputStream onetimeDoc;
		  TextDocumentManager docMgr;
		  DocWriter(DatabaseClient client, String id, String doc) throws Exception {
		   this.id = id;
		   if (isResendable)
		    this.resendableDoc = doc;
		   else
		    this.onetimeDoc = new ByteArrayInputStream(doc.getBytes("UTF-8"));
		   docMgr = client.newTextDocumentManager();
		  }
		  @Override
		  public void run() {
		   try {
		    System.out.println("sleeping "+id);
		    Thread.sleep(1000);
		   } catch (InterruptedException e) {
		   }
		   runImpl();
		  }
		  public void runImpl() {
		   System.out.println("writing "+id);
		   if (isResendable)
		    docMgr.write(id, new StringHandle(resendableDoc));
		   else
		    docMgr.write(id, new InputStreamHandle(onetimeDoc));
		   System.out.println(id+"="+docMgr.read(id, new StringHandle()).get());
		   docMgr.delete(id);
		   System.out.println("finished "+id);
		  }
		 }
	 
		
	@Test
	public void testBug26248() throws Exception {
		try{ 
		DocWriter.isResendable = false;
		client= DatabaseClientFactory.newClient(
			    "localhost", 8011, "rest-writer", "x", Authentication.DIGEST
			   );
		  DocWriter dw0 = new DocWriter(client, "/tmp/test0.txt", "The zeroth text");
		  DocWriter dw1 = new DocWriter(client, "/tmp/test1.txt", "The first text");
		  DocWriter dw2 = new DocWriter(client, "/tmp/test2.txt", "The second text");
		 
		  Thread t1 = new Thread(dw1);
		  Thread t2 = new Thread(dw2);
		 
		  t1.start();
		  t2.start();
		  dw0.runImpl();
		  Thread.sleep(2000);
		}catch(Exception e){
			throw e;
		}
	}

}
