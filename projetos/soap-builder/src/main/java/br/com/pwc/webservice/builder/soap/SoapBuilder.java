package br.com.pwc.webservice.builder.soap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.PortType;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import br.com.pwc.webservice.builder.soap.enums.ElementTypeEnum;
import br.com.pwc.webservice.builder.soap.vo.SoapBuilderRequestVO;
import br.com.pwc.webservice.builder.soap.vo.SoapBuilderResponseVO;

import com.ibm.wsdl.OperationImpl;
import com.ibm.wsdl.PartImpl;
import com.ibm.wsdl.PortImpl;
import com.ibm.wsdl.ServiceImpl;
import com.ibm.wsdl.extensions.schema.SchemaImpl;

@Component
public class SoapBuilder {
	
	

	public SoapBuilderResponseVO construirMensagem(SoapBuilderRequestVO vo) throws SoapBuilderException {
		try {
			/** Iniciando a construção do vo de resposta **/
			SoapBuilderResponseVO retorno = new SoapBuilderResponseVO();
			/** Crio uma instancia de um WSDL reader **/
			WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
			/** Leio o WSDL passado pelo VO **/
			Definition wsdl = reader.readWSDL(vo.getWsdl());
			/** Adiciono os namespaces do wsdl em um mapa **/
			addNamespaces(wsdl.getNamespaces());
			/** Adiciono o target namespace no mapa de namespace também**/
			namespaces.put("targetnamespace", wsdl.getTargetNamespace());
			/** Recupero o seu QName **/
			QName qname = wsdl.getQName();
			/** Recupero o Service partindo do QName **/
			ServiceImpl service = (ServiceImpl) wsdl.getAllServices().get(qname);
			/** Recupero a Port do Service passado pelo VO **/
			PortImpl port = (PortImpl) service.getPorts().get(vo.getPort());
			/** Recupero o address do Port para popular o LocationURI do Vo de retorno **/
			SOAPAddress address = (SOAPAddress) port.getExtensibilityElements().get(0);
			retorno.setLocationURI(address.getLocationURI());
			/** Recupero o Binding da Port **/
			Binding binding = port.getBinding();
			/** Recupero o PortType do Binding **/
			PortType portType = binding.getPortType();
			/** Itero pelas suas operações procurando o Operation passado pelo VO **/
			OperationImpl operation = null;
			for(Object op0 : portType.getOperations()) {
				OperationImpl op = (OperationImpl) op0;
				if(op.getName().equals(vo.getOperation())) {
					operation = op;
					break;
				}
			}
			/** Recupero o Input do Operation **/
			Input input = operation.getInput();
			/** Recupero a Message do Input **/
			Message message = input.getMessage();
			/** Recupero o Mapa de Parts do Message **/
			Map parts = message.getParts();
			/** Crio uma lista de OMElements para poder construir a mensagem SOAP **/
			List<OMElement> listaElements = new ArrayList<OMElement>();
			/** Crio uma factory para o SOAP 1.2**/
			SOAPFactory soapFactory = vo.getSoapVersion().getSoapFactory();
			/** Itero pelos valores do Mapa para popular a lista de OMElements **/
			for(Object o : parts.keySet()) {
				/** Faço cast do Object para PartImpl **/
				PartImpl part = (PartImpl) parts.get(o);
				/** Recupero seu QName **/
				QName elementName = part.getElementName();
				/** Recupero o seu localPart, que será o nome do Node que irei procurar **/
				String localPart = elementName.getLocalPart();
				/** Recupero o primeiro Schema da lista (FIXME sempre será o primeiro?) **/
				SchemaImpl schema = (SchemaImpl) wsdl.getTypes().getExtensibilityElements().get(0);
				/** Adiciono todos os Namespaces do schema para o mapa de namespace **/
				addNamespaces(schema.getElement().getAttributes());
				/** Recupero o primeiro Node do Schema **/
				Node firstChild = schema.getElement().getFirstChild();
				/** Recupero o Node que representa o 'element' que estou procurando (com nome igual ao localPart) **/
				Node nodePrincipal = getPrincipalNode("element", localPart, firstChild);
				/** Recupero o seu tipo para saber se é complexo **/
				String type = nodePrincipal.getAttributes().getNamedItem("type").getNodeValue();
				if(isComplexPrefix(type)) {
					nodePrincipal = getPrincipalNode("complexType", localPart, firstChild);
				}
				/** Crio o OMElement, partindo do Node e o adiciono na lista de OMElements **/
				OMElement element = getElement(nodePrincipal, soapFactory);
				listaElements.add(element);
			}
			/** Crio um OMNamespace do targetNamespace, recuperado do WSDL **/
			OMNamespace omNamespace = soapFactory.createOMNamespace(namespaces.get("targetnamespace"), "xyz");
			/** Como ainda trabalhamos somente com um parâmetro, recupero o primeiro item (FIXME Corrigir o código quando houver mais de um parâmetro) **/
			OMElement createOMElement = listaElements.get(0);
			/** Coloco o namespace neste elemento, mas como eu colocarei no SOAPEnvelope, ele só ficará com o prefix **/
			createOMElement.setNamespaceWithNoFindInCurrentScope(omNamespace);
			/** Crio um SOAPEnvelope default da factory **/
			SOAPEnvelope envelope = soapFactory.getDefaultEnvelope();
			/** Declaro o OMNamespace criado acima **/
			envelope.declareNamespace(omNamespace);
			/** Adiciono o OMElement criado acima no SOAPEnvelope **/
			envelope.getBody().addChild(createOMElement);
			/** Seto o soap text no retorno **/
			retorno.setMensagem(populaStringComParametros(envelope.toString(), vo.getAtributos()));
			return retorno;
		} catch(Exception e) {
			System.out.println("Erro ao tentar consumir com os seguintes parâmetros: " + vo);
			throw new SoapBuilderException(e);
		}
	}
	
	/**
	 * Itera pelos Nodes que ficam no mesmo nível do passado (NextSibling),
	 * para encontrar o Node que seja do tipo e nome passado por parâmetro
	 * 
	 * @param tipo do Node procurado (ComplexType / element)
	 * @param nome do Node
	 * @param elemento primeiro Node do Array de Nodes a ser pesquisado
	 * @return {@link Node} Com as informações passadas
	 */
	private Node getPrincipalNode(String tipo, String nome, Node elemento) {
		/** Se o element é nulo, não consegui encontrar o Node correto **/
		if(elemento == null) {
			return null;
		} else {
			/** Se o element é do tipo e tem o nome informado, eu retorno ele **/
			if(tipo.equals(elemento.getLocalName())
				&& elemento.getAttributes().getNamedItem("name").getNodeValue().equals(nome)) {
				return elemento;
			}
		}
		/** Caso nenhuma das situações acimas sejam concretizadas, eu recomeço o método com o próximo Node **/
		return getPrincipalNode(tipo, nome, elemento.getNextSibling());
	}
	
	/**
	 * Crio um OMElement partindo do Node, caso ele seja do tipo complexo, eu retorno ele e todos os seus 'filhos'
	 * @param node com as informações necessárias
	 * @param soapFactory factory do tipo de Soap (1.1 / 1.2)
	 * @return {@link OMElement} elemento criado com as informações do Node
	 */
	private OMElement getElement(Node node, SOAPFactory soapFactory) {
		/** Verifico se o elemento é Simples ou Complexo **/
		ElementTypeEnum type = lookupFromTypeNameToTypeElementEnum(node.getLocalName());
		if(type != null) {
			/** Crio um OMElement da factory **/
			OMElement element = soapFactory.createOMElement(node.getAttributes().getNamedItem("name").getNodeValue(), null);
			if(type == ElementTypeEnum.SIMPLE_ELEMENT) {
				/** Se o element é Simples, adiciono no texto um caracter especial para que depois eu o substitua pelos parâmetros passados **/
				element.setText("§");
				return element;
			} else {
				/** Se o element é Complexo, eu itero pelos seus filhos e crio um OMElement para cada **/
				Node proximoFilho = node.getFirstChild();
				while(proximoFilho != null) {
					/** Verifico o seu tipo **/
					String localName = proximoFilho.getLocalName();
					if(localName == null) {
						/** Se o seu tipo é nulo, continuo a iteração com o próximo Node **/
						proximoFilho = proximoFilho.getNextSibling();
					} else if(localName.equals("sequence")) {
						/** Se o seu tipo for sequence, continuo a iteração com o seu primeiro filho **/
						proximoFilho = proximoFilho.getFirstChild();
					} else {
						/** Se o seu tipo for outro qualquer, crio um OMElement dele e o adiciono no 'pai' **/
						element.addChild(getElement(proximoFilho, soapFactory));
						/** Continuo a iteração com o próximo Node **/
						proximoFilho = proximoFilho.getNextSibling();
					}
				}
				return element;
			}
		}
		return null;
	}
	
	/**
	 * Converto o tipo para o TypeElementEnum
	 * @param tipo do Node
	 * @return {@link ElementTypeEnum} enum que representa o tipo ou null caso não exista tenha
	 */
	private ElementTypeEnum lookupFromTypeNameToTypeElementEnum(String tipo) {
		if("element".equals(tipo)) {
			return ElementTypeEnum.SIMPLE_ELEMENT;
		} else if("complexType".equals(tipo)) {
			return ElementTypeEnum.COMPLEX_ELEMENT;
		} else {
			return null;
		}
	}
	
	/**
	 * Substitui todos os § pelos parâmetros da lista passada
	 * @param texto que contem os §
	 * @param parametros lista com os parâmetros
	 * @return {@link String} texto formatado
	 */
	private String populaStringComParametros(String texto, List<String> parametros) {
		if(parametros != null && !parametros.isEmpty()) {
			for(String string : parametros) {
				texto = texto.replaceFirst("§", string);
			}
		}
		return texto;
	}
	
	/**
	 * Adiciona os namespaces do NamedNodeMap para o Mapa da classe
	 * @param namedNode com os namespaces
	 */
	private void addNamespaces(NamedNodeMap namedNode) {
		int qtd = namedNode.getLength();
		if(qtd > 0) {
			for(int i = 0; i < qtd; i++) {
				Node item = namedNode.item(i);
				String nodeName = namedNode.item(i).getNodeName();
				/** Como eu os recupero por string, removo verifico e removo o xmlns: do inicio  **/
				if(nodeName.startsWith("xmlns")) {
					namespaces.put(nodeName.substring(6), item.getNodeValue());
				}
			}
		}
	}
	
	/**
	 * Adiciona os namespaces do Mapa passado para o Mapa da classe
	 * @param mapNamespace Mapa com os namespaces
	 */
	private void addNamespaces(Map mapNamespace) {
		for(Object o : mapNamespace.keySet()) {
			namespaces.put(o.toString(), mapNamespace.get(o).toString());
		}
	}
	
	/**
	 * Verifica se o prefixo é de tipo complexo
	 * (FIXME Como não consigo verificar se é do tipo complexo, verifico se não é http://www.w3.org/2001/XMLSchema)
	 * 
	 * @param prefix
	 * @return
	 */
	private boolean isComplexPrefix(String prefix) {
		prefix = prefix.substring(0, prefix.indexOf(':'));
		String namespace = namespaces.get(prefix);
		if(namespace != null && namespace.equals("http://www.w3.org/2001/XMLSchema")) {
			return false;
		} else {
			return true;
		}
	}
	
	private Map<String, String> namespaces = new HashMap<String, String>();

}
