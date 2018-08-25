package br.com.pwc.cte.integracao.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.pwc.cte.integracao.mail.config.ConfigEnum;
import br.com.pwc.cte.integracao.mail.config.DirectoryConfig;
import br.com.pwc.cte.integracao.mail.config.IntegradorEmailMessageFactory;
import br.com.pwc.cte.integracao.mail.tray.TraySystem;

/**
 * Classe singleton responsável por carregar e mapear contas cadastradas.
 * 
 * @author sergio.moreira
 * @author daniel.santos
 *
 */
@Component
@Scope(value="singleton")
public class LoadAccount {
	
	private static final Logger LOGGER = Logger.getLogger(LoadAccount.class);
	
	@Autowired
	private IntegradorEmailMessageFactory messageFactory;
	
	@Autowired
	private TraySystem traySystem;
	
	private Properties configProperties;
	
	private List<Properties> listaDePropriedadesDeEmail;
	
	public LoadAccount() {}
	
	/**
	 * Construtor publico que recebe properties de configuração,
	 * chama os metodos que carregam e mapeiam as contas para inicialização dos sistema.
	 * @param valorProperties
	 */
	public LoadAccount(Properties valorProperties) {
		configProperties = valorProperties;
	}
	
	/**
	 * Método responsavel por ler config properties de sistemas e mapear as contas,
	 * e separa-las em pop e imap.
	 */
	public void loadConfig() {
		LOGGER.info(messageFactory.getMessage(MSG_INICIO_PROCESSO));
		
		LOGGER.info(messageFactory.getMessage(INFO_PROCESSO_CONTA));
		int qtdeConta;
		try {
			LOGGER.info(messageFactory.getMessage(MSG_QUANTIDADE_CONTAS));
			qtdeConta = Integer.valueOf(configProperties.getProperty(ConfigEnum.QTDE_CONTAS.getPropriedade()));
			listaDePropriedadesDeEmail = new ArrayList<Properties>();
			
			LOGGER.info(messageFactory.getMessage(INFO_PROCESSO_CONTA_VERIFICACAO));
			for (int i = 0; i < qtdeConta; i++) {
				Properties prop = new Properties();
				for (int j = 0; j < 7; j++) {
					prop.put(ConfigEnum.values()[j + 9].getPropriedade(), configProperties.getProperty(ConfigEnum.values()[j + 9].getPropriedade().concat(".").concat(String.valueOf(i))));
				}
				listaDePropriedadesDeEmail.add(prop);
			}
			LOGGER.info(messageFactory.getMessage(MSG_LOAD_CONTA_SUCESSO));
			
			DirectoryConfig.createDirectory(configProperties);
			traySystem.inicializaModoGrafico();
			
		} catch (NumberFormatException e) {
			LOGGER.error(messageFactory.getMessage(ERRO_QUANTIDADE_CONTAS), e);
		} catch (Exception e) {
			LOGGER.error(messageFactory.getMessage(ERRO_INFO_PROCESSO_CONTA_VERIFICACAO), e);
		}
		
	}
	
	public List<Properties> getListaDePropriedadesDeEmail() {
		return listaDePropriedadesDeEmail;
	}


	private static final String MSG_INICIO_PROCESSO = "msgProcessoConfigIniciado";
	private static final String INFO_PROCESSO_CONTA = "msgIniciandoMapeamento";
	private static final String MSG_QUANTIDADE_CONTAS = "msgObtendoQtdContas";
	private static final String INFO_PROCESSO_CONTA_VERIFICACAO = "msgVerificandoContas";
	private static final String MSG_LOAD_CONTA_SUCESSO = "msgContasMapeadas";
	private static final String ERRO_QUANTIDADE_CONTAS = "msgErroAoConverterQtdContas";
	private static final String ERRO_INFO_PROCESSO_CONTA_VERIFICACAO = "msgErroAoIniciarVerificacaoDeContas";
}