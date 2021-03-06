/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package test.com.tsc9526.monalisa.orm.model;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tsc9526.monalisa.tools.string.MelpString;

import test.com.tsc9526.monalisa.orm.dialect.mysql.mysqldb.TestRecord;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class ModelTest {
	public void testCut() {
		TestRecord record =  new TestRecord();
		record.setName(MelpString.repeat("1", 128));
		Assert.assertEquals(record.getName().length(),128);
	 
		
		record.setName(MelpString.repeat("1", 129));
		record.cut();
		Assert.assertEquals(record.getName().length(),128);
		Assert.assertTrue(record.getName().endsWith(" ..."));
	}
	
	
	public void testUpdateByWhere() {
		TestRecord.DELETE().delete("name=? AND title=?", "where-name"  ,"where-title");
		TestRecord.DELETE().delete("name=? AND title=?", "where-name-1","where-title-1");
		
		TestRecord record =  new TestRecord();
		record.defaults().setName("where-name").setTitle("where-title");
		Assert.assertEquals(record.save(),1);
		
		TestRecord record1 =  new TestRecord();
		record1.defaults().setName("where-name-1").setTitle("where-title-1");
		Assert.assertEquals(record1.save(),1);
		
		record.setUpdateBy("mis-xxx");
		int r = TestRecord.UPDATE(record).update("name=? AND title=?", "where-name","where-title");
		Assert.assertEquals(r,1);
		Assert.assertEquals(new TestRecord(record.getRecordId()).load().getUpdateBy(),"mis-xxx");
		
		TestRecord x1=new TestRecord(record1.getRecordId()).load();
		Assert.assertEquals(x1.getName(),"where-name-1");
		Assert.assertEquals(x1.getTitle(),"where-title-1");
	}
}
