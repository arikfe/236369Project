package com.technion.project;

import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.technion.project.model.EvacuationEvent;
import com.technion.project.model.Report;

@EnableWebMvc
@Configuration
@ComponentScan(
{ "com.technion.project.*" })
@EnableTransactionManagement
@Import(
{ UserSecurityConfig.class })
public class AppConfig extends WebMvcConfigurerAdapter
{

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler("/CSS/**").addResourceLocations("/CSS/");
		registry.addResourceHandler("/JS/**").addResourceLocations("/JS/");
		registry.addResourceHandler("/css/**").addResourceLocations("/CSS/");
		registry.addResourceHandler("/js/**").addResourceLocations("/JS/");
		registry.addResourceHandler("/img/**").addResourceLocations("/IMG/");
		registry.addResourceHandler("/IMG/**").addResourceLocations("/IMG/");
	}

	@Override
	public void configureDefaultServletHandling(
			final DefaultServletHandlerConfigurer configurer)
	{
		configurer.enable();
		final Session openSession = sessionFactory().openSession();
		final FullTextSession fullTextSession = Search
				.getFullTextSession(openSession);
		try
		{
			fullTextSession.createIndexer(Report.class).startAndWait();
			fullTextSession.createIndexer(EvacuationEvent.class).startAndWait();
		} catch (final InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		openSession.close();
	}

	@Bean
	public SessionFactory sessionFactory()
	{
		LocalSessionFactoryBuilder builder;
		try
		{
			builder = new LocalSessionFactoryBuilder(dataSource());
			builder.scanPackages("com.technion.project.model").addProperties(
					getHibernateProperties());
			return builder.buildSessionFactory();
		} catch (final Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private Properties getHibernateProperties()
	{
		final Properties prop = new Properties();
		prop.put("hibernate.format_sql", "true");
		prop.put("hibernate.show_sql", "true");
		prop.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		prop.put("hibernate.search.default.directory_provider", "filesystem");
		prop.put("hibernate.search.default.indexBase", "lucene/indexes/");
		// prop.put("hibernate.search.default.indexBase",
		// "D:\\Dropbox\\Eclipse\\Java-Web\\236369Project\\index");
		return prop;
	}

	@Bean(name = "dataSource")
	public BasicDataSource dataSource()
	{

		final BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/project");
		ds.setUsername("root");
		ds.setPassword("Course236369rulez");
		return ds;
	}

	@Bean
	public HibernateTransactionManager txManager()
	{
		return new HibernateTransactionManager(sessionFactory());
	}

	@Bean
	public InternalResourceViewResolver viewResolver()
	{
		final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/pages/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean
	public MultipartResolver multipartResolver()
	{
		return new CommonsMultipartResolver();
	}
}