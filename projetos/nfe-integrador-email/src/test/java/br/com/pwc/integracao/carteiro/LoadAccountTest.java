//package br.com.pwc.integracao.carteiro;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Properties;
//
//import junit.framework.Assert;
//
//import org.junit.Test;
//
//import br.com.pwc.integracao.mail.LoadAccount;
//import br.com.pwc.integracao.mail.config.EnumConfig;
//
//public class LoadAccountTest {
//	
//	@Test
//	public void testLoadAccount() throws FileNotFoundException, IOException {
//		Properties prop = new Properties();
//		prop.load(new FileReader(new File("config/emailNFe.properties")));
//		LoadAccount load = new LoadAccount(prop);
//		Assert.assertNotNull(load.getPop());
//	}
//	
//	@Test(expected = Exception.class)
//	public void testErroLoadAccount() throws FileNotFoundException, IOException {
//		Properties prp = new Properties();
//		prp.load(new FileInputStream(new File("src\\test\\resources\\ConfigTest\\emailNFeInv.properties")));
//		LoadAccount load = new LoadAccount(new Properties());
//		Assert.assertNull(load.getPop());
//	}
//	
//	@Test
//	public void testDadosObrigatoriosDaContaNoConfig() throws FileNotFoundException, IOException {
//		Properties prop = new Properties();
//		prop.load(new FileReader(new File("config/emailNFe.properties")));
//		LoadAccount load = new LoadAccount(prop);
//		Assert.assertNotNull(load.getPop());
//		for(int i = 0; i < Integer.valueOf(prop.get(EnumConfig.QTDE_CONTAS.getPropriedade()).toString()); i++) {
//			for(int j = 0;j < 7; j++) {
//				Assert.assertNotNull(prop.get(EnumConfig.values()[i + 9].getPropriedade().concat(".").concat(String.valueOf(i))));
//			}
//		}
//	}
//	
//	@Test
//	public void testConfiguracaoDiretorios() throws FileNotFoundException, IOException {
//		Properties prop = new Properties();
//		prop.load(new FileReader(new File("config/emailNFe.properties")));
//		LoadAccount load = new LoadAccount(prop);
//		Assert.assertNotNull(load.getPop());
//		for(int i = 0; i < 1; i++) {
//			for(int j = 0;j < 3; j++) {
//				Assert.assertNotNull(prop.get(EnumConfig.values()[i + 2].getPropriedade()));
//			}
//		}
//	}
//}
