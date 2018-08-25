package br.com.pwc.cte.integracao.xml.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum de Retorno de importacao
 * @author Danilo Ferreira
 * @author Guilherme Botossi
 *
 */
public enum CodigoRetorno {

	SUCESSO_CADASTRO("499", "sucessoCadastro"),//Mensagem ainda não criada
	SUCESSO_IMPORTACAO("500", "sucessoImportacao"),
	//Msg Sucesso
	INSERIR_PROCCTE("100", "msgInserirProcCTe"),
	UPDATE_MENSAGEM_IMPORTADOR("201", "msgUpdateImportador"),
	
	//MSG Erro
	XML_NAO_EXISTE("501", "xmlAprovadoNaoExiste"),
	CNPJ_INVALIDO("502", "cnpjInvalido"),
	DESTINATARIO_NAO_EXISTE("503", "destNaoExiste"),
	ERRO_BUSCAR_PACOTE("504", "erroAoBuscarPacote"),
	PACOTE_NAO_ENCONTRADO("505", "pacoteNaoEncontrado"),
	ESQUEMA_NAO_ENCONTRADO("506", "esquemaNaoEncontrado"),
	ERRO_CONSULTA_NFE("507", "erroConsultaNFe"),
	XML_INVALIDO("508", "msgXmlInvalido"),
	ERRO_EXTRAIR_PROC("509", "erroExtrairProc"),
	ERRO_EXTRAIR_CANC("510", "erroExtrairCanc"),
	ERRO_EXTRAIR_EVENTO("511", "ErroExtrairEvento"),
	ERRO_BUSCAR_STATUS("512", "ErroBuscarStatus"),
	STATUS_PROIBIDO("513", "statusNaoPermitido_"),
	ERRO_NAO_CATALOGADO("514", "erroNaoCatalogado"),
	ERRO_ARQUIVO_NAO_ENCONTRADO("515", "erroArquivoNaoEncontrado"),
	PARAMETRO_EM_BRANCO("516", "parametroNaoInformado"),
	CAMPOS_OBRIGATORIOS_EM_BRANCO("517", "camposObrigatoriosEmBranco"),//Mensagem ainda não criada
	ITEM_JA_CADASTRADO("518", "itemJaCadastrado_"),//Mensagem ainda não criada
	ERRO_PARSER("519", "erroEfetuarParse"),

	XML_INVALIDO_NULO("521", "msgXMLInvalidoNulo"),
	MSG_TOMADOR_NAO_ECONTRADO("522", "msgTomadorNaoEncontrado"),
	ERRO_STATUS_INVALIDO("523", "msgErroStatusInvalido"),
	MSG_ERRO_OBTER_VERSAO_CTE("524", "msgErroObterVersaoCTe"),
	MSG_ERRO_OBTER_CODIGO_STATUS("525", "msgErroObterCodigoStatus"),
	ERRO_OBTER_CHAVE_XML_CANCELAMENTO("526", "msgErroObterChaveXMLCancelamento"),
	ERRO_XML_INEXISTENTE_BASE_CANCELAMENTO("527", "msgErroXMLNaoEncontradoCTeCancelamento_"),
	ERRO_BUSCAR_XML_BASE("528", "msgErroBuscarXMLBase"),
	ERRO_LOCALIZANDO_EMPRESA("529", "msgErroLocalizarEmpresa"),
	CERTIFICADO_INVALIDO("530", "msgCaminhoCertificadoNaoCadastrado"),
	VERSAO_MANUAL_NAO_CADASTRADO("531", "msgVersaoManualNaoCadastrado"),
	ERRO_BUSCAR_ULTIMO_MANUAL("532", "msgErroObterUltimoManual"),
	ERRO_BUSCAR_PARAMETRO_EMPRESA("533", "msgErroConsultarParametroEmpresa"),
	ERRO_VALIDAR_XML_SCHEMAS_VAZIO("534", "msgErroValidarXmlSchemasVazio"),
	ERRO_VALIDAR_XML_SCHEMAS("535", "msgErroValidarXmlSchemas"),
	ERRO_VALIDACAO_XML("536", "msgErroValidacaoXML"),
	ERRO_SCHEMA_NAO_ENCONTRADO("537", "msgErroSchemaNaoEncontrado"),
	MSG_ERRO_MAPEAR_XML("538", "msgErroMapearXml"),
	ERRO_CONSULTAR_SITUACAO_CTE("539", "msgErroConsultaSituacaoCTe"),
	ERRO_DIGEST_VALUE_DIFERE("540", "msgErroDigestValueDifere"),
	ERRO_CHAVE_ACESSO_DIFERE("541", "msgErroChaveAcessoDifere"),
	MSG_XML_INEXISTENTE_CANCELADA("530","msgXMLInexistenteCancelada"),
	ERRO_ENVIO("532", "msgErroEnvio"),
	ERRO_PARSER_XML_DTO("533", "msgErroParserXmlDTO"),
	ERRO_DESCONHECIDO("600", "msgErroDesconhecido");
	
	private static Map<String, CodigoRetorno> mensagens = new HashMap<String, CodigoRetorno>();
	private static Map<CodigoRetorno, CodigoRetorno> msgsMapa = new HashMap<CodigoRetorno, CodigoRetorno>();
	
	static {
		for(CodigoRetorno c : values()) {
			mensagens.put(c.getCodigo(), c);
			msgsMapa.put(c, c);
		}
	}
	
	private CodigoRetorno(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public static CodigoRetorno lookupByCodigo(String cod) {
		return mensagens.get(cod);
	}
	
	public String getCodigo() {
		return codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public static CodigoRetorno loockupByCodigo(String cod) {
		return mensagens.get(cod);
	}

	public static CodigoRetorno loockupByEnum(CodigoRetorno cod) {
		return msgsMapa.get(cod);
	}
	private String codigo;
	private String descricao;
}
