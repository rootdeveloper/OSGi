package br.com.pwc.cte.integracao.mail.tray;

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

import br.com.pwc.cte.integracao.mail.config.ConfigEnum;
import br.com.pwc.cte.integracao.mail.config.IntegradorEmailMessageFactory;

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
	private IntegradorEmailMessageFactory messageFactory;
	
	@Resource(name="configPrincipal")
	private Properties properties;
	
	private TrayIcon trayIcon;
	private SystemTray tray;
	
	public void inicializaModoGrafico() {
		Boolean enable = Boolean.valueOf(properties.get(ConfigEnum.INTEGRACAO_MODO_GRAFICO.getPropriedade()).toString());
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
		
		tray = java.awt.SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage(CAMINHO_ICONE);
		LOGGER.info(messageFactory.getMessage(MSG_CRIANDO_TRAY));
		LOGGER.info(messageFactory.getMessage(MSG_TRAY_CRIADO));
		
		//Aqui são os aventos que são associados ao tray para abertura das pastas.
		ActionListener solicitacao = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abreDiretorio(properties.getProperty(ConfigEnum.INTEGRACAO_DIR_IMPORTADOS.getPropriedade()));
			}
		};
		ActionListener processado = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abreDiretorio(properties.getProperty(ConfigEnum.INTEGRACAO_DIR_RETORNO_IMPORTACAO.getPropriedade()));
			}
		};
		
		ActionListener erro = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abreDiretorio(properties.getProperty(ConfigEnum.INTEGRACAO_DIR_ERRO_INVALIDOS.getPropriedade()));
			}
		};
		
		PopupMenu popup = new PopupMenu();

		MenuItem importados = new MenuItem(FOLDER_NAME_IMPORTADOS);
		MenuItem retornoImportacao = new MenuItem(FOLDER_NAME_RETORNO_IMPORTACAO);
		MenuItem invalidos = new MenuItem(FOLDER_NAME_INVALIDOS);
		
		importados.addActionListener(solicitacao);
		retornoImportacao.addActionListener(processado);
		invalidos.addActionListener(erro);
		
		popup.add(importados);
		popup.add(retornoImportacao);
		popup.add(invalidos);
		
		LOGGER.info(messageFactory.getMessage(MSG_EVENTOS_ASSOCIADOS));
		
		trayIcon = new TrayIcon(image, NOME_SISTEMA, popup);
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
	public void removeTray() {
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
	
	private static final String CAMINHO_ICONE = "img/envelope.png";
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
	
	private static final String FOLDER_NAME_IMPORTADOS = "Importados";
	private static final String FOLDER_NAME_INVALIDOS = "Invalidos";
	private static final String FOLDER_NAME_RETORNO_IMPORTACAO = "Retorno da Importação";
	}