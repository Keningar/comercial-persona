package ec.telconet.persona.datasource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import ec.telconet.microservicio.dependencia.datasource.Configdb;

/**
 * Clase utilizada para configurar los datasources Oracle
 * 
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@Configuration
public class DbOracleConfig {
	@Autowired
	private Configdb configdb;
	
	@Value("${ruta.parameterslocal}")
	private String rutaParametersLocal;
	
	@Value("${host.parameter}")
	private String hostParameters;
	
	@Value("${spring.datasource.hikari.idle-timeout}")
	private int idleTimeout;
	
	@Value("${spring.datasource.hikari.connection-timeout}")
	private int connectionTimeout;
	
	@Value("${spring.datasource.hikari.maximum-pool-size}")
	private int maxPoolSize;
	
	@Value("${spring.datasource.hikari.minimum-idle}")
	private int minPoolSize;
	
	@Value("${spring.datasource.hikari.max-lifetime}")
	private int maxLifetime;
	
	private final Map<String, String> hibernateProperties() {
		Map<String, String> hibernateProperties = new LinkedHashMap<String, String>();
		hibernateProperties.put("hibernate.connection.release_mode", "auto");
		return hibernateProperties;
	}
	
	// INICIO CONFIGURACIÓN COMERCIAL
	@Primary
	@Bean(name = "dsComercial")
	public DataSource dsComercial(@Qualifier("dsComercialProperties") HikariConfig dataSourceConfig) {
		return new HikariDataSource(dataSourceConfig);
	}
	
	@Primary
	@Bean(name = "dsComercialProperties")
	public HikariConfig dsComercialConfig() throws Exception {
		ArrayList<String> listaConexiones = new ArrayList<String>();
		listaConexiones.add("comercial");
		HikariConfig dataSourceConfig = configdb.getConfig(listaConexiones, hostParameters, rutaParametersLocal);
		dataSourceConfig.setPoolName("dsComercial");
		dataSourceConfig.setConnectionTimeout(connectionTimeout);
		dataSourceConfig.setIdleTimeout(idleTimeout);
		dataSourceConfig.setMaximumPoolSize(maxPoolSize);
		dataSourceConfig.setMinimumIdle(minPoolSize);
		dataSourceConfig.setMaxLifetime(maxLifetime);
		dataSourceConfig.setValidationTimeout(10000);
		dataSourceConfig.setConnectionTestQuery("SELECT 1 FROM DUAL");
		dataSourceConfig.setConnectionInitSql("SELECT 1 FROM DUAL");
		dataSourceConfig.addDataSourceProperty("oracle.jdbc.timezoneAsRegion", "false");
		return dataSourceConfig;
	}
	
	@Primary
	@Bean(name = "jdbcComercial")
	@Autowired
	public JdbcTemplate jdbcComercialTemplate(@Qualifier("dsComercial") DataSource dsComercial) {
		return new JdbcTemplate(dsComercial);
	}
	
	@Primary
	@Bean(name = "ComercialEMFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryComercial(EntityManagerFactoryBuilder builder,
			@Qualifier("dsComercial") DataSource dsComercial) {
		return builder.dataSource(dsComercial).properties(hibernateProperties())
				.packages("ec.telconet.microservicios.dependencias.esquema.comercial.entity").persistenceUnit("dbComercial").build();
	}
	
	@Primary
	@Bean(name = "ComercialTM")
	public PlatformTransactionManager transactionManagerComercial(@Qualifier("ComercialEMFactory") EntityManagerFactory ComercialEMFactory) {
		return new JpaTransactionManager(ComercialEMFactory);
	}
	// FIN CONFIGURACIÓN COMERCIAL
	
	// INICIO CONFIGURACIÓN GENERAL
		@Bean(name = "dsGeneral")
		public DataSource dsGeneral(@Qualifier("dsGeneralProperties") HikariConfig dataSourceConfig) {
			return new HikariDataSource(dataSourceConfig);
		}

		@Bean(name = "dsGeneralProperties")
		public HikariConfig dsGeneralConfig() throws Exception {
			ArrayList<String> listaConexiones = new ArrayList<>();
			listaConexiones.add("general");
			HikariConfig dataSourceConfig = configdb.getConfig(listaConexiones, hostParameters, rutaParametersLocal);
			dataSourceConfig.setPoolName("dsGeneral");
			dataSourceConfig.setConnectionTimeout(connectionTimeout);
			dataSourceConfig.setIdleTimeout(idleTimeout);
			dataSourceConfig.setMaximumPoolSize(maxPoolSize);
			dataSourceConfig.setMinimumIdle(minPoolSize);
			dataSourceConfig.setMaxLifetime(maxLifetime);
			dataSourceConfig.setValidationTimeout(10000);
			dataSourceConfig.setConnectionTestQuery("SELECT 1 FROM DUAL");
			dataSourceConfig.setConnectionInitSql("SELECT 1 FROM DUAL");
			dataSourceConfig.addDataSourceProperty("oracle.jdbc.timezoneAsRegion", "false");
			return dataSourceConfig;
		}

		@Bean(name = "jdbcGeneral")
		@Autowired
		public JdbcTemplate jdbcGeneralTemplate(@Qualifier("dsGeneral") DataSource dsGeneral) {
			return new JdbcTemplate(dsGeneral);
		}

		@Bean(name = "GeneralEMFactory")
		public LocalContainerEntityManagerFactoryBean entityManagerFactoryGeneral(EntityManagerFactoryBuilder builder,
		                                                                          @Qualifier("dsGeneral") DataSource dsGeneral) {
			return builder.dataSource(dsGeneral).properties(hibernateProperties())
					.packages("ec.telconet.microservicios.dependencias.esquema.general.entity").persistenceUnit("dbGeneral").build();
		}

		@Bean(name = "GeneralTM")
		public PlatformTransactionManager transactionManagerGeneral(@Qualifier("GeneralEMFactory") EntityManagerFactory GeneralEMFactory) {
			return new JpaTransactionManager(GeneralEMFactory);
		}
		// FIN CONFIGURACIÓN GENERAL
		

		   // INICIO CONFIGURACIÓN DOCUMENTO
		  	@Bean(name = "dsDocumento")
		  	public DataSource dsDocumento(@Qualifier("dsDocumentoProperties") HikariConfig dataSourceConfig) {
		  		return new HikariDataSource(dataSourceConfig);
		  	}

		  	@Bean(name = "dsDocumentoProperties")
		  	public HikariConfig dsDocumentoConfig() throws Exception {
		  		ArrayList<String> listaConexiones = new ArrayList<>();
		  		listaConexiones.add("documento");
		  		HikariConfig dataSourceConfig = configdb.getConfig(listaConexiones, hostParameters, rutaParametersLocal);
		  		dataSourceConfig.setPoolName("dsDocumento");
		  		dataSourceConfig.setConnectionTimeout(connectionTimeout);
		  		dataSourceConfig.setIdleTimeout(idleTimeout);
		  		dataSourceConfig.setMaximumPoolSize(maxPoolSize);
		  		dataSourceConfig.setMinimumIdle(minPoolSize);
		  		dataSourceConfig.setMaxLifetime(maxLifetime);
		  		dataSourceConfig.setValidationTimeout(10000);
		  		dataSourceConfig.setConnectionTestQuery("SELECT 1 FROM DUAL");
		  		dataSourceConfig.setConnectionInitSql("SELECT 1 FROM DUAL");
		  		dataSourceConfig.addDataSourceProperty("oracle.jdbc.timezoneAsRegion", "false");
		  		return dataSourceConfig;
		  	}

		  	@Bean(name = "jdbcDocumento")
		  	@Autowired
		  	public JdbcTemplate jdbcDocumentoTemplate(@Qualifier("dsDocumento") DataSource dsDocumento) {
		  		return new JdbcTemplate(dsDocumento);
		  	}

		  	@Bean(name = "DocumentoEMFactory")
		  	public LocalContainerEntityManagerFactoryBean entityManagerFactoryDocumento(EntityManagerFactoryBuilder builder,
		  			@Qualifier("dsDocumento") DataSource dsDocumento) {
		  		return builder.dataSource(dsDocumento)
		  				.packages("ec.telconet.microservicios.dependencias.esquema.documento.entity")
		  				.persistenceUnit("dbDocumento").build();
		  	}

		  	@Bean(name = "DocumentoTM")
		  	public PlatformTransactionManager transactionManagerDocumento(
		  			@Qualifier("DocumentoEMFactory") EntityManagerFactory DocumentoEMFactory) {
		  		return new JpaTransactionManager(DocumentoEMFactory);
		  	}
		  	// FIN CONFIGURACIÓN DOCUMENTO
}
