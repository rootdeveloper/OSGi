package br.com.pwc.cte.integracao.xml.tray;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.pwc.cte.integracao.xml.config.ConfigEnum;
import br.com.pwc.cte.integracao.xml.config.IntegradorXmlMessageFactory;

/**
 * Classe responsavel por criar icone de execução na barra do SO - SystemTray
 * 
 * @author sergio
 * @author daniel.santos
 * 
 * */
@Component
@Scope(value="singleton")
public class TraySystem {
	
	private static final Logger LOGGER = Logger.getLogger(TraySystem.class);
	
	@Autowired
	private IntegradorXmlMessageFactory messageFactory;
	
	@Resource(name="configPrincipal")
	private Properties properties;
	
	private static TrayIcon trayIcon;
	private static SystemTray tray = java.awt.SystemTray.getSystemTray();;
	
	public void loadTray() {
		Boolean enable = Boolean.valueOf(properties.get(ConfigEnum.INTEGRACAO_MODO_GRAFICO.getKey()).toString());
		if(enable) {
			LOGGER.info(messageFactory.getMessage(MSG_ENABLE_INICIO_MODO_GRAFICO));
			createTray();
		} else {
			LOGGER.info(messageFactory.getMessage(MSG_DISABLE_INICIO_MODO_GRAFICO));
		}
	}
	/**
	 * Método responsavel por criar o tray,adicionar um icone, adicionar a bandeja do SO e adicionar eventos.
	 * 
	 **/
	private void createTray(){
		
		
		Image image = Toolkit.getDefaultToolkit().getImage(CAMINHO_ICONE);
		LOGGER.info(messageFactory.getMessage(MSG_CRIANDO_TRAY));
		LOGGER.info(messageFactory.getMessage(MSG_TRAY_CRIADO));
		
		//Aqui são os aventos que são associados ao tray para abertura das pastas.
		ActionListener envio = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abreDiretorio(properties.getProperty(ConfigEnum.INTEGRACAO_DIR_ENVIO.getKey()));
			}
		};
		ActionListener importados = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abreDiretorio(properties.getProperty(ConfigEnum.INTEGRACAO_DIR_IMPORTADOS.getKey()));
			}
		};
		ActionListener erros = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abreDiretorio(properties.getProperty(ConfigEnum.INTEGRACAO_DIR_ERROS.getKey()));
			}
		};
		ActionListener invalidos = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abreDiretorio(properties.getProperty(ConfigEnum.INTEGRACAO_DIR_INVALIDOS.getKey()));
			}
		};
		ActionListener retornos = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abreDiretorio(properties.getProperty(ConfigEnum.INTEGRACAO_DIR_RETORNOS.getKey()));
			}
		};
		

		MenuItem envioMenu = new MenuItem(FOLDER_NAME_ENVIO);
		MenuItem importadosMenu = new MenuItem(FOLDER_NAME_IMPORTADOS);
		MenuItem errosMenu = new MenuItem(FOLDER_NAME_ERROS);
		MenuItem invalidosMenu = new MenuItem(FOLDER_NAME_INVALIDOS);
		MenuItem retornosMenu = new MenuItem(FOLDER_NAME_RETORNOS);
		
		envioMenu.addActionListener(envio);
		importadosMenu.addActionListener(importados);
		errosMenu.addActionListener(erros);
		invalidosMenu.addActionListener(invalidos);
		retornosMenu.addActionListener(retornos);
		
		PopupMenu popup = new PopupMenu();
		popup.add(envioMenu);
		popup.add(importadosMenu);
		popup.add(errosMenu);
		popup.add(invalidosMenu);
		popup.add(retornosMenu);
		
		LOGGER.info(messageFactory.getMessage(MSG_EVENTOS_ASSOCIADOS));
		
		trayIcon = new TrayIcon(image, messageFactory.getMessage(NOME_SISTEMA), popup);
		trayIcon.setImageAutoSize(true);
		
		try {
			LOGGER.info(messageFactory.getMessage(MSG_ADICIONANDO_TRAY));
			tray.add(trayIcon);
			trayIcon.displayMessage(messageFactory.getMessage(NOME_SISTEMA), 
					messageFactory.getMessage(MSG_SISTEMA_MINIMIZADO), MessageType.INFO);
			LOGGER.info(messageFactory.getMessage(MSG_TRAY_ADICIONADO));
		} catch (AWTException e) {
			LOGGER.error(messageFactory.getMessage(MSG_ICONE_NAO_IMPLEMENTADO));
		}
	}
	
	/**
	 *Metodo responsavel por remover o tray da barra de tarefas do SO. 
	 *
	 **/
	public static void removeTray() {
		tray.remove(trayIcon);
	}
	/**
	 * Método responsavel por abrir diretório.
	 * 
	 * */
	private void abreDiretorio(String path){
		try {
			Desktop.getDesktop().open(new File(path.trim()).getAbsoluteFile());
		} catch (IOException e) {
			LOGGER.error(messageFactory.getMessage(MSG_ERRO_AO_ABRIR_DIRETORIO), e);
		}
	}
	
	private static final String CAMINHO_ICONE = "img/pasta.png";
	private static final String NOME_SISTEMA = "msgNomeSistema";
	private static final String MSG_EVENTOS_ASSOCIADOS = "msgEventosAssociados";
	private static final String MSG_ENABLE_INICIO_MODO_GRAFICO = "msgModoGraficoHabilitado";
	private static final String MSG_DISABLE_INICIO_MODO_GRAFICO = "msgModoGraficoNaoHabilitado";
	private static final String MSG_CRIANDO_TRAY = "msgCriandoTray";
	private static final String MSG_TRAY_CRIADO = "msgAssociandoEventos";
	private static final String MSG_ADICIONANDO_TRAY = "msgAdicionandoTray";
	private static final String MSG_SISTEMA_MINIMIZADO = "msgTrayMinimizado";
	private static final String MSG_TRAY_ADICIONADO = "msgTrayAdicionado";
	private static final String MSG_ICONE_NAO_IMPLEMENTADO = "msgIconeNaoImplementado";
	private static final String MSG_ERRO_AO_ABRIR_DIRETORIO = "msgErroAbrirDiretorio";
	
	private static final String FOLDER_NAME_ENVIO = "Envio";
	private static final String FOLDER_NAME_IMPORTADOS = "Importados CT-e";
	private static final String FOLDER_NAME_ERROS = "Erros CT-e";
	private static final String FOLDER_NAME_INVALIDOS = "Inválidos";
	private static final String FOLDER_NAME_RETORNOS = "Retornos CT-e";
	}