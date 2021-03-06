package br.com.pwc.nfe.integracao.xml.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;


public class SystemConfigurationValidator {
	
	public void chechConfigurations() throws IOException {
		FileInputStream configPropertiesAsStream = new FileInputStream(DIR_CONFIG_SYSTEM);
		Properties configProperties = new Properties();
		configProperties.load(configPropertiesAsStream);
		configPropertiesAsStream.close();
		isValid(configProperties);
	}


	private void isValid(Properties prop) {
		Set<Entry<Object,Object>> set = prop.entrySet();
		ConfigEnum[] configArray = ConfigEnum.values();
		for(Entry<Object, Object> entry : set) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			ConfigEnum config = ConfigEnum.convert(key);			
			validarTipo(config, value);
			configArray = (ConfigEnum[]) ArrayUtils.removeElement(configArray, config);
		}
		
		if (configArray.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < configArray.length; i++) {
				ConfigEnum config = configArray[i];
				sb.append(config.getKey());
				if (i < configArray.length - 1)
					sb.append(", ");
			}
			throw new IllegalArgumentException("Faltando os seguintes parametros: " + sb);
		}
	}
	
	private void validarTipo(ConfigEnum config, String value) {
		switch (config.getType()) {
			case FOLDER:
				validarPasta(value);				
				break;
			case WS_ADDR:
				validarEnderecoWS(value);
				break;
			case DELAY:
				validarInteger(value);
				break;
			case VIEW:
				validarUI(value);
				break;
			case BUFFER:
				validarPoolDeEnvio(value);
				break;
			default:
				throw new IllegalArgumentException("Tipo de configura��o desconhecida " + config.getType().name());
		}		
	}

	private void validarArquivo(String pathFile) {
		File file = new File(pathFile);
		if (file.exists())
			throw new IllegalArgumentException("O arquivo n�o existe " + pathFile);
		if (file.isFile())
			throw new IllegalArgumentException("N�o � um arquivo" + pathFile);
	}

	private void validarPasta(String folder) {
		File file = new File(folder);
		if (!file.exists())
			try {
				FileUtils.forceMkdir(file);
			} catch (IOException e) {
				throw new IllegalArgumentException("Impossivel criar a pasta indicada no Config Properties " + folder);
			}
		if (!file.isDirectory())
			throw new IllegalArgumentException("N�o � um diretorio" + folder);
	}
	
	private void validarEnderecoWS(String value) {		
		if (StringUtils.isBlank(value))
			throw new IllegalArgumentException("N�o � um endere�o de Web Service " + value);
	}
	
	private void validarPoolDeEnvio(String value) {
		if (!StringUtils.isNumeric((value)) || StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("N�o � um numero valido " + value);
		} else if (Integer.valueOf(value) > QTD_MAXIMA_THREADS || Integer.valueOf(value) <= 0) {
			throw new IllegalArgumentException("A quantidade de threadas deve ser maior que zero e menor ou igual a " +
					QTD_MAXIMA_THREADS + ". Valor configurado: " + value);
		}
	}
	
	private void validarInteger(String value) {
		if (!StringUtils.isNumeric((value)) || StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("N�o � um numero valido " + value);
		}
	}
	private void validarUI(String value){
		if(StringUtils.isBlank(value))
			throw new IllegalArgumentException("Parametro de Interface Grafica n�o � valido! " + value);
	}
	
	private static final String DIR_CONFIG_SYSTEM = "config/xmlNFe.properties";
	
	public static void main(String[] args) throws IOException {
		System.out.println("iniciando teste");
		SystemConfigurationValidator systemConfiguration = new SystemConfigurationValidator();
		systemConfiguration.chechConfigurations();
		System.out.println("Fim teste");
	}
	
	private static final int QTD_MAXIMA_THREADS = 10;
}
