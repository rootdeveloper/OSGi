package br.com.pwc.cte.integracao.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.pwc.cte.integracao.mail.config.ConfigEnum;
import br.com.pwc.cte.integracao.mail.config.IntegradorEmailMessageFactory;
import br.com.pwc.email.reader.Carteiro;
import br.com.pwc.email.reader.EmailReaderException;
import br.com.pwc.email.reader.PropriedadesEmailVO;

/**
 * Classe responavel pela tarefa de integração.
 * 
 * @author sergio.moreira
 * @author daniel.santos
 *
 */
@Component
public class RunJobTask {

	private static Logger LOGGER = Logger.getLogger(RunJobTask.class);
	
	@Autowired
	private LoadAccount loadAccount;
	
	@Autowired
	private PropriedadesEmailVO propriedadesEmailVO;
	
	@Autowired
	private Carteiro carteiro;
	
	@Autowired
	private Integrador integrador;
	
	@Autowired
	private IntegradorEmailMessageFactory messageFactory;
	
	@Resource(name = "configPrincipal")
	private Properties configProperties;
	
	private List<String> listaDeXmls = new ArrayList<String>();
	
	/**
	 * Método que o processo de integração.
	 */
	public void execute() {
		LOGGER.info(messageFactory.getMessage(MSG_INICIANDO_INTEGRACAO));
		LOGGER.info(messageFactory.getMessage(MSG_OBTENDO_CONFIG_CONTAS));
		
		List<Properties> listaDePropriedadesDeEmail = loadAccount.getListaDePropriedadesDeEmail();
		
		Boolean debug = Boolean.valueOf(configProperties.getProperty(ConfigEnum.CONTA_DEBUG_PROCESSO.getPropriedade()).toString());
		
		Boolean debugAthenticator = Boolean.valueOf(configProperties.getProperty(ConfigEnum.CONTA_DEBUG_AUTHENTICATOR.getPropriedade()).toString());
		
		String nomeDaPastaEnviados = configProperties.getProperty(ConfigEnum.EMAIL_NAME_FOLDER.getPropriedade());
		
		PropriedadesEmailVO propriedadesEmailVO = populaPropriedadesEmailVO(debug, debugAthenticator, nomeDaPastaEnviados, listaDePropriedadesDeEmail);
		
		LOGGER.info(messageFactory.getMessage(MSG_CONEXAO_NAS_CONTAS));
		Map<String, String> mapaDeXmlsEncontrados;
		
		try {
			mapaDeXmlsEncontrados = carteiro.recuperaXmls(propriedadesEmailVO);
			listaDeXmls.addAll(mapaDeXmlsEncontrados.values());
			for(String xml: listaDeXmls) {
				integrador.enviaSolicitacao(xml);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		listaDeXmls.clear();
		
		//obter xml
		//enviar para portal
		//popular pasta conforme o retorno
	}
	
	private PropriedadesEmailVO populaPropriedadesEmailVO(Boolean debug, Boolean debugAuthenticator, String nomePastaEnviados, 
			List<Properties> listaDePropriedadesDeEmail) {
		
		propriedadesEmailVO.setDebug(debug);
		propriedadesEmailVO.setDebugAuthenticator(debugAuthenticator);
		propriedadesEmailVO.setNomePastaEnviados(nomePastaEnviados);
		propriedadesEmailVO.setListasDePropriedades(listaDePropriedadesDeEmail);
		
		return propriedadesEmailVO;
	}
	
	private static final String MSG_INICIANDO_INTEGRACAO = "msgIniciandoIntegracao";
	private static final String MSG_OBTENDO_CONFIG_CONTAS = "msgObtendoConfigContas";
	private static final String MSG_CONEXAO_NAS_CONTAS = "msgSolicitandoConexao";
	private static final String MSG_ERRO_COMUNICACAO_MENSAGERIA = "msgErroComunicacao";
	
}
