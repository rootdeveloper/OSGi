package br.com.pwc.nfe.integracao.xml;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.pwc.nfe.integracao.xml.config.ConfigEnum;
import br.com.pwc.nfe.integracao.xml.config.IntegradorXmlMessageFactory;
import br.com.pwc.nfe.integracao.xml.util.FileManager;
import br.com.pwc.nfe.integracao.xml.util.FilenameFilterImpl;
import br.com.pwc.nfe.integracao.xml.util.XmlInspector;

/**
 * Classe responavel pela tarefa de integração.
 * 
 * @author sergio.moreira
 * @author daniel.santos
 * 
 */
@Scope(value="singleton")
@Component
public class RunJobTask {

	private static Logger LOGGER = Logger.getLogger(RunJobTask.class);

	@Autowired
	private Integrador integrador;

	@Autowired
	private XmlInspector inspector;

	@Autowired
	private IntegradorXmlMessageFactory messageFactory;

	@Resource(name = "configPrincipal")
	private Properties configProperties;
	
	@Autowired
	private FileManager fileManager;
	
	private Thread[] arrayDeThreads;
	
	private FileReader[] arrayDeLeitores;
	
	private int QTD_THREADS_ENVIO;
	
	private int fim;
	
	private int inicio;
	
	private int totalDeArquivos;
	
	private int sobra;
	
	private int qtdDeArquivosParaCadaThread;
	
	private File pastaDeEnvio;
	
	private File pastaErroDeComunicacao;
	
	private static ExecutorService executorService;
	
	@PostConstruct
	public void init() {
		
		QTD_THREADS_ENVIO = Integer.valueOf(configProperties.getProperty(ConfigEnum.INTEGRACAO_QTD_MSG_BUFFER.getKey()));
		
		executorService = Executors.newFixedThreadPool(QTD_THREADS_ENVIO);
		
		arrayDeLeitores = new FileReader[QTD_THREADS_ENVIO];
		
        for (int i = 0; i < QTD_THREADS_ENVIO; i++) {
        	arrayDeLeitores[i] = new FileReader(messageFactory, inspector, integrador, fileManager, configProperties);
        }
        
        // restaura arquivos das pastas de envio e falha na comunicação
        fileManager.configuraPastasDoSistema();
	}
	
	public void execute() {
		
		for(int j = 0; j < QTD_THREADS_ENVIO; j++) {
			
			executorService.execute(arrayDeLeitores[j]);
		}
	}
	
	
	
	
	/**
	 * Método que executa o processo de integração.
	 */
	public void executeOld() {
		
		// Movo os arquivos de erro de comunicação para a pasta de envio
		File[] arquivos = pastaErroDeComunicacao.listFiles();
		fileManager.moveAllFiles(arquivos, pastaDeEnvio);
		
		LOGGER.info(messageFactory.getMessage(LISTANDO_ARQUIVOS_DE_ENVIO));
		File[] files = pastaDeEnvio.listFiles(new FilenameFilterImpl(SUFFIX));
		
		totalDeArquivos = files.length;
		
		if(totalDeArquivos > 0) {
			
			LOGGER.info(messageFactory.getMessage(MSG_INICIANDO_INTEGRACAO));
			
			// Divido o total de arquivos pela quantidade de threads de envio 
			// para saber com quantos arquivos cada thread irá trabalhar 
			qtdDeArquivosParaCadaThread = totalDeArquivos / QTD_THREADS_ENVIO;
			
			// A divisão anterior pode não ser exata então irá restar
			// arquivos para serem enviados por outra thread
			sobra = totalDeArquivos % QTD_THREADS_ENVIO;
			
			if(qtdDeArquivosParaCadaThread > 0) {
				
				// cria o array de threads e o array de leitores
				arrayDeThreads = new Thread[QTD_THREADS_ENVIO];
				arrayDeLeitores = new FileReader[QTD_THREADS_ENVIO];
				
				// variáveis usadas para controlar
				// o intervalo de arquivos da pasta de envio
				inicio = 0;
				fim = qtdDeArquivosParaCadaThread;
				
				// monta os arrays de arquivos para serem enviados
				for(int i = 0; i < QTD_THREADS_ENVIO; i++) {
					
					ConcurrentHashMap<String, File> mapaDeArquivos = new ConcurrentHashMap<String, File>();
					
					// percorre o intervalo de arquivos que será colocado em cada mapa
					for(int j = inicio; j < fim; j++) {
						
						File newFile = new File(files[j].getAbsolutePath() + SUFFIX);
						files[j].renameTo(newFile);
						
						// Se o arquivo é inválido move para pasta de inválidos
						if (!inspector.isValid(newFile)) {
							File destino = fileManager.file(configProperties.getProperty(
									ConfigEnum.INTEGRACAO_DIR_INVALIDOS.getKey()), newFile.getName());
							
							LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_INVALIDO) + " " + newFile.getName());
							fileManager.moveFile(newFile, destino);
						
						} else {
							
							mapaDeArquivos.put(newFile.getName(), newFile);
						}
					}
					
					if(mapaDeArquivos.size() > 0) {
						// passa o mapa para o leitor executar
//						arrayDeLeitores[i] = new FileReader(
//								mapaDeArquivos, messageFactory, inspector, integrador, fileManager, configProperties);
						arrayDeThreads[i] = new Thread(arrayDeLeitores[i]);
						arrayDeThreads[i].setName(NAME_OF_THREADS + (i+1));
						arrayDeThreads[i].start();
					}
					// Ajusta o inicio e fim do próximo intervalo de arquivos
					inicio = fim;
					fim = fim + qtdDeArquivosParaCadaThread;
				}
			}
			
			// processa os arquivos que restaram da divisão
			if(sobra > 0) {
				
				ConcurrentHashMap<String, File> mapaDeArquivos = new ConcurrentHashMap<String, File>();
				
				//o laço é decrescente porque quero os ultimos arquivos do array de files
				for(int k = totalDeArquivos; k > qtdDeArquivosParaCadaThread * QTD_THREADS_ENVIO; k--) {
					
					File newFile = new File(files[k-1].getAbsolutePath() + SUFFIX);
					files[k-1].renameTo(newFile);
					
					// Se o arquivo é inválido movo para pasta de inválidos
					if (!inspector.isValid(newFile)) {
						
						File destino = fileManager.file(configProperties.getProperty(
								ConfigEnum.INTEGRACAO_DIR_INVALIDOS.getKey()), newFile.getName());
						
						LOGGER.info(messageFactory.getMessage(MSG_MOVENDO_PARA_INVALIDO) + " " + newFile.getName());
						fileManager.moveFile(newFile, destino);
						
					} else {
						
						// Adiciona o sufixo no nome para não ser listado novamente
						mapaDeArquivos.put(newFile.getName(), newFile);
					}
				}
				
				if(mapaDeArquivos.size() > 0) {
//					Thread thread = new Thread(new FileReader(
//							mapaDeArquivos, messageFactory, inspector, 
//							integrador, fileManager, configProperties));
//					thread.setName(NAME_OF_THREADS + (QTD_THREADS_ENVIO + 1));
//					thread.start();
				}
			}
			
		} else {
			
			LOGGER.info(messageFactory.getMessage(MSG_NAO_EXISTEM_MENSAGENS));
		}
		
	}
	
	public static ExecutorService getExecutorService() {
		return executorService;
	}

	private final static String SUFFIX = "~";
	private static final String NAME_OF_THREADS = "FileReader ";
	private static final String MSG_INICIANDO_INTEGRACAO = "msgIniciandoIntegracao";
	private static final String LISTANDO_ARQUIVOS_DE_ENVIO = "msgListandoArquivos";
	private static final String MSG_NAO_EXISTEM_MENSAGENS = "msgNaoExistemMensagens";
	private static final String MSG_MOVENDO_PARA_INVALIDO = "msgMovendoXmlParaInvalidos";

}
