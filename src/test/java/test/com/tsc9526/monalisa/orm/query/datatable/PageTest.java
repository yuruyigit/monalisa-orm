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
package test.com.tsc9526.monalisa.orm.query.datatable;

import org.testng.Assert;
import org.testng.annotations.Test;

import test.com.tsc9526.monalisa.orm.query.datatable.DataTableTest.TestUserAreaRank;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tsc9526.monalisa.orm.datatable.DataTable;
import com.tsc9526.monalisa.orm.datatable.Page;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper.MetaClass;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Test
public class PageTest {
	public void testPages() {
		DataTable<Object> table = new DataTable<Object>();
		 
		//创建测试数据
		MetaClass mc=ClassHelper.getMetaClass(TestUserAreaRank.class);
		for(int userId=1;userId<=6;userId++){
			Object row=new TestUserAreaRank();

			mc.getField("user").setObject(row, userId);
			mc.getField("area").setObject(row,"guangdong-"+(userId%2));
			if(userId!=3){
				mc.getField("rank").setObject(row,90+userId);
			}
			
			table.add(row);
		}
		
		Page<Object> page=table.getPage(5,0);
		Assert.assertEquals(page.getPageNo(), 1);
		Assert.assertEquals(page.getTotalPage(), 2);
		Assert.assertEquals(page.getTotalRow(), 6);
		Assert.assertEquals(page.getPageSize(),5);
		Assert.assertEquals(page.getRows(),5);
		 
		page=table.getPage(5,5);
		Assert.assertEquals(page.getPageNo(), 2);
		Assert.assertEquals(page.getTotalPage(), 2);
		Assert.assertEquals(page.getTotalRow(), 6);
		Assert.assertEquals(page.getPageSize(),5);
		Assert.assertEquals(page.getRows(),1);
	}
	
	
	public void testToJsonObj() {
		DataTable<Object> table = new DataTable<Object>();
		 
		//创建测试数据
		MetaClass mc=ClassHelper.getMetaClass(TestUserAreaRank.class);
		for(int userId=1;userId<=6;userId++){
			Object row=new TestUserAreaRank();

			mc.getField("user").setObject(row, userId);
			mc.getField("area").setObject(row,"guangdong-"+(userId%2));
			if(userId!=3){
				mc.getField("rank").setObject(row,90+userId);
			}
			
			table.add(row);
		}
		
		Page<Object> page=table.getPage(6,0);
		Assert.assertEquals(page.getPageNo(), 1);
		Assert.assertEquals(page.getTotalPage(), 1);
		Assert.assertEquals(page.getTotalRow(), 6);
		Assert.assertEquals(page.getPageSize(),6);
		Assert.assertEquals(page.getRows(),6);
		
		String json=page.toJson();
		JsonParser parser=new JsonParser();
		JsonObject root=parser.parse(json).getAsJsonObject();
		
		Assert.assertEquals(root.get("pageNo").getAsInt(),1);
		Assert.assertEquals(root.get("totalPage").getAsInt(),1);
		Assert.assertEquals(root.get("totalRow").getAsInt(),6);
		Assert.assertEquals(root.get("pageSize").getAsInt(),6);
	 	 
		JsonElement list=root.get("list");
		String target=new GsonBuilder().serializeNulls().create().toJson(list);
		Assert.assertEquals(target,table.toJson());
		 
	}
	
	
	  
}