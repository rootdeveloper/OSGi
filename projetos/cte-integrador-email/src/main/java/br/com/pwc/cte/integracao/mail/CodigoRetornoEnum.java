package br.com.pwc.cte.integracao.mail;

/**
 * Enum de Retorno de importacao
 * @author daniel.santos
 *
 */
public enum CodigoRetornoEnum {

	SUCESSO_CADASTRO("499", "sucessoCadastro"),//Mensagem ainda não criada
	SUCESSO_IMPORTACAO("500", "sucessoImportacao"),
	XML_NAO_EXISTE("501", "xmlAprovadoNaoExiste"),
	IE_NAO_EXISTE("502", "ieNaoExiste"),
	CNPJ_INVALIDO("503", "cnpjInvalido"),
	DESTINATERIO_NAO_EXISTE("504", "destNaoExiste"),
	ERRO_BUSCAR_PACOTE("505", "erroAoBuscarPacote"),
	PACOTE_NAO_ENCONTRADO("506", "pacoteNaoEncontrado"),
	ESQUEMA_NAO_ENCONTRADO("507", "esquemaNaoEncontrado"),
	ERRO_CONSULTA_NFE("508", "erroConsultaNFe"),
	XML_INVALIDO("509", "msgXmlInvalido"),
	ERRO_EXTRAIR_PROC("510", "erroExtrairProc"),
	ERRO_EXTRAR_CANC("511", "erroExtrairCanc"),
	ERRO_EXTAIR_EVENTO("512", "ErroExtrairEvento"),
	ERRO_BUSCAR_STATUS("513", "ErroBuscarStatus"),
	STATUS_PROIBIDO("514", "statusNaoPermitido_"),
	ERRO_NAO_CATALOGADO("515", "erroNaoCatalogado"),
	ERRO_ARQUIVO_NAO_ENCONTRADO("516", "erroArquivoNaoEncontrado"),
	PARAMETRO_EM_BRANCO("517", "parametroNaoInformado"),
	CAMPOS_OBRIGATRIOS_EM_BRANCO("518", "camposObrigatoriosEmBranco"),//Mensagem ainda não criada
	ITEM_JA_CADASTRADO("519", "itemJaCadastrado_");//Mensagem ainda não criada
	
	
	private CodigoRetornoEnum(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public String getDescricao() {
		return descricao;
	}

	private String codigo;
	private String descricao;
}
