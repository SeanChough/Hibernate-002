import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//测试类，使用JUnit进行单元测试
public class StudentsTest {

	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;
	
	@Before
	public void init(){
		//创建配置对象
		Configuration config = new Configuration().configure();
		//创建服务注册对象
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
		//创建会话工厂对象
		sessionFactory = config.buildSessionFactory(serviceRegistry);
		//创建会话对象
		session = sessionFactory.openSession();
		//开启事务
		transaction = session.beginTransaction();
	}
	
	@After
	public void destroy(){
		transaction.commit();//提交事务
		session.close();//关闭会话
		sessionFactory.close();//关闭会话工厂
	}
	
	//插入记录：save
	@Test
	public void testSaveStudent(){
		Students s = new Students();
//		s.setSid(2);
		s.setName("Tom");
		s.setGender("男");
		try {
			s.setBirthday(new SimpleDateFormat("yyyy年MM月dd日").parse("1994年06月13日"));//日期时间格式的转换
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Address address = new Address();
		address.setPostcode("235433");
		address.setPhone("13456789078");
		address.setAddress("上海市闵行区");
		s.setAddress(address);//这里对应的是后来的Address类型的address属性
		
		//若不使用事务，设置session的自动提交模式
		//生成Work实现类的匿名实例对象
		/*session.doWork(new Work(){

			public void execute(Connection connection) throws SQLException {
				// TODO Auto-generated method stub
				connection.setAutoCommit(true);
			}
			
		});*/
		session.save(s);
		//强制刷新
//		session.flush();
	}
	
	//测试Hibernate中的Blob的对象类型
	@Test
	public void testWriteBlob(){
		Students s = new Students();
		s.setName("Tom");
		s.setGender("男");
		try {
			s.setBirthday(new SimpleDateFormat("yyyy年MM月dd日").parse("1994年06月13日"));//日期时间格式的转换
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.setAddress("上海市闵行区");//这里对应的是最开始的时候String类型的address属性
		/*
		 * 下面要准备写入Blob对象类型
		 */
		//先获得照片文件（注意目录的问题）
		File f = new File(".\\head.jpg");//D:\Workspace_EclipseKepler\Hibernate-002\head.jpg
//		File f= new File("..\\had.jpg");//输出的目录是：D:\Workspace_EclipseKepler\head.jpg
		/*try {//测试是否正确找到文件 【getCanonicalPath() getAbsolutePath()】
			System.out.println(f.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		InputStream input = null;
		try {
			//获得照片的文件输出流：字节流
			input = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Blob image = null;
		try {
			//创建一个Blob对象
			 image = Hibernate.getLobCreator(session).createBlob(input, input.available());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.setPicture(image);
		session.save(s);
	}
	
	//测试读取Blob类型的数据，验证是否存入成功
	@Test
	public void testReadBlob(){
		Students s = (Students) session.get(Students.class, 1);
		//获得Blob对象
		Blob image = s.getPicture();
		//获得照片的输入流
		InputStream input = null;
		try {
			input = image.getBinaryStream();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//创建一个输出文件：读取出来图片信息并保存到同目录下生成dest.jpg的图片
		File f = new File(".\\dest.jpg");
		//获得输出流
		OutputStream output = null;
		try {
			output = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//创建缓存区，用来写入到文件中
		byte[] buff = null;
		try {
			buff = new byte[input.available()];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			input.read(buff);//从输入流input中读取数据并存储到byte数组buff中
			output.write(buff);//将byte数组中的数据写入到输出流output中
			//关闭流
			input.close();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 以下为测试Hibernate的URD操作
	 */
	//查询单条记录：get
	//注意get与load 的区别
	@Test
	public void testGetStudents(){
		Students s = (Students) session.get(Students.class, 1);
		System.out.println(s);
	}
	//查询单条记录：load
	@Test
	public void testLoadStudents(){
		Students s = (Students) session.load(Students.class, 1);
		System.out.println(s);
	}
	//更新记录：update
	@Test
	public void testUpdateStudents(){
		Students s = (Students) session.get(Students.class, 1);
		s.setGender("女");
		session.update(s);
	}
	//删除记录：delete
	@Test
	public void testDeleteStudents(){
		Students s = (Students) session.get(Students.class, 1);
		session.delete(s);
	}
}
