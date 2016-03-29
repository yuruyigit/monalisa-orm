# monalisa-core: Very simple database ORM tool

      "And God said, Let there be light: and there was light."
      

# Main features:
* Introducing database takes only 1 line of code
* Dynamic SQL code generator

# Example
## Example 1
Here is a full example:
```java
    package example.monalisa;

    import com.tsc9526.monalisa.core.annotation.DB;
    import com.tsc9526.monalisa.core.query.model.Record;

    @DB(url="jdbc:mysql://127.0.0.1:3306/test" ,username="root", password="root") //Define database
    public class User extends Record{ //Dynamic table model, name is "user"
	    public static void main(String[] args) {
		    User user=new User();
		    user.setName("zzg"); 
		
		    user.set("status", 1); //set undefined fields in this class
	    	user.save(); //save to database
		
    		System.out.println(user.get("id"));  //get the user id(auto increment field), output will be 1
    	}
	
    	private String name;
    	public String getName() {
    		return name;
    	}
    	public void setName(String name) {
    		this.name = name;
    	}
    }
```
``` sql 
		CREATE TABLE `user` (
		  `id` int(11) NOT NULL AUTO_INCREMENT,
		  `name` varchar(64) NOT NULL,
		  `passwd` varchar(64) DEFAULT NULL,
		  `status` int(11) DEFAULT NULL,
		  PRIMARY KEY (`id`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

## Example 2
Here is another formal's example:

Step 1: Define database
```java
    package example.monalisa.db;

    import com.tsc9526.monalisa.core.annotation.DB;
    import com.tsc9526.monalisa.core.datasource.DBConfig;

    @DB(url="jdbc:mysql://127.0.0.1:3306/test" ,username="root", password="root") //Setup database
    public interface Test {
    	public static DBConfig DB=DBConfig.fromClass(Test.class); 
    }
```
Step 2: Run code generator(If you use the Eclipse plug-in, you can omit this step, Plug-in automatically generated code)
```java
    package example.monalisa.db;

   import com.tsc9526.monalisa.core.generator.DBGeneratorMain;

   public class ModelGenerator {
    	public static void main(String[] args) {
    		//Generate Model classes to directory: src/main/java OR src
	    	DBGeneratorMain.generateModelClass(Test.class); 
    	}
    }
```

Step 3: Use the generated model classes
```java
    package example.monalisa.db;

	import example.monalisa.db.Test;
    import example.monalisa.db.test.User;

    public class Example {
    	public static void main(String[] args) {
    		//insert
	    	new User().setName("zzg.zhou").setStatus(1).save();
	
    		//select
    		User user=User.WHERE().name.like("zzg%").status.in(1,2,3).SELECT().selectOne(); //selectPage ...
	
    		//update
    		user.setStatus(3).update();
		
    		//delete
    		user.delete();
    		User.WHERE().name.like("zzg%").delete();
    		
    		//general query
			Test.DB.select("SELECT * FOMR user WHERE name like ?","zzg%");
			Test.DB.createQuery().add("SELECT * FOMR user WHERE name like ?","zzg%").getList(User.class);
    	}
    }
```
