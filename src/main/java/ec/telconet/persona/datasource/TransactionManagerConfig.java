package ec.telconet.persona.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Clase utilizada para configurar los transaction manager de todas las bases
 * 
 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
 * @version 1.0
 * @since 02/03/2020
 */
@Configuration
public class TransactionManagerConfig {
	/**
	 * Método que agrupa los PlatformTransactionManager configurados en el microservicio, sirve para los rollback de las
	 * transacciones llamadas desde los db-repositorio
	 * 
	 * @author Marlon Plúas <mailto:mpluas@telconet.ec>
	 * @version 1.0
	 * @since 02/03/2020
	 * 
	 * @param ComercialTM Schema Comercial
	 * @return telconetTransactionManager
	 */
	@Bean(name = "telconetTransactionManager")
	public ChainedTransactionManager telconetTransactionManager(@Qualifier("ComercialTM") PlatformTransactionManager ComercialTM) {
		return new ChainedTransactionManager(ComercialTM);
	}
}
