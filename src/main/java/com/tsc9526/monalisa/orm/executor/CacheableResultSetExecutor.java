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
package com.tsc9526.monalisa.orm.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tsc9526.monalisa.tools.datatable.DataColumn;
import com.tsc9526.monalisa.tools.datatable.DataTable;
import com.tsc9526.monalisa.tools.io.MelpClose;
import com.tsc9526.monalisa.tools.string.MelpSQL;
import com.tsc9526.monalisa.tools.string.MelpTypes;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class CacheableResultSetExecutor<T>  extends HandlerRelation implements CacheableExecute<DataTable<T>>{
	private HandlerResultSet<T> resultHandler;
	
	public CacheableResultSetExecutor(HandlerResultSet<T> resultHandler){
		this.resultHandler=resultHandler;
	}
	
  
	public DataTable<T> execute(Connection conn,String sql,List<?> parameters) throws SQLException {				 
		PreparedStatement pst = null;
		ResultSet         rs  = null;		
		
		DataTable<T> result=new DataTable<T>();
		try{
			pst = conn.prepareStatement(sql);
			MelpSQL.setPreparedParameters(pst, parameters);
			
			rs=setupRelationTables(pst.executeQuery());	
			
			result.setHeaders(getHeaders(rs));
			 
			while(rs.next()){
				T r=resultHandler.createResult(rs); 
				result.add(r);					
			}
			return result;
		}finally{
			MelpClose.close(pst,rs);
		}
	} 
	
	protected List<DataColumn> getHeaders(ResultSet rs) throws SQLException {
		List<DataColumn> ls=new ArrayList<DataColumn>();
		
		ResultSetMetaData rsmd=rs.getMetaData();
		
		Map<String, Integer> xs = new HashMap<String, Integer>();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String label=rsmd.getColumnLabel(i);
			
			String name = label;
			if (name == null || name.trim().length() < 1) {
				name = rsmd.getColumnName(i);
			}

			Integer n = xs.get(name);
			if (n != null) {
				name = name + n;

				xs.put(name, n + 1);
			} else {
				xs.put(name, 1);
			}
			
			DataColumn dc=new DataColumn(name);
			int jdbcType=rsmd.getColumnType(i);
			dc.setJdbcType(jdbcType);
			dc.setTypeString(MelpTypes.getJavaType(jdbcType));
			dc.setLabel(label==null?name:label);
			
			ls.add(dc);
		}
		
		return ls;
	}
	
	 public String getCacheExtraTag() {
		 return getClass().getName()+"/"+resultHandler.getClass().getName();
	 }
}
