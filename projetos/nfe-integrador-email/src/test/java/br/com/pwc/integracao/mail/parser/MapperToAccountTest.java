//package br.com.pwc.integracao.mail.parser;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Properties;
//
//import junit.framework.Assert;
//
//import org.junit.Test;
//
//import br.com.pwc.integracao.mail.Account;
//import br.com.pwc.integracao.mail.LoadAccount;
//import br.com.pwc.nfe.integracao.mail.util.MapperToAccount;
//
//public class MapperToAccountTest {
//
//	@Test
//	public void testMapperPropertiesToAccount() throws FileNotFoundException, IOException {
//		Properties cfg = new Properties();
//		cfg.load(new FileInputStream(new File("src\\test\\resources\\ConfigTest\\emailNFe.properties")));
//		LoadAccount cfgAccount = new LoadAccount(cfg);
//		Account conta = MapperToAccount.parserToAccount(cfgAccount.getPop().get(0), cfg);
//		Assert.assertNotNull(conta);
//	}
//	
//	@Test
//	public void testDadosMapperPropertiesToAccount() throws FileNotFoundException, IOException {
//		Properties cfg = new Properties();
//		cfg.load(new FileInputStream(new File("src\\test\\resources\\ConfigTest\\emailNFe.properties")));
//		LoadAccount cfgAccount = new LoadAccount(cfg);
//		Account conta = MapperToAccount.parserToAccount(cfgAccount.getPop().get(0), cfg);
//		Assert.assertNotNull(conta);
//		Assert.assertNotNull(conta.getServer());
//		Assert.assertNotNull(conta.getPasswd());
//		Assert.assertNotNull(conta.getProtocolo());
//		Assert.assertNotNull(conta.getSocketFacotorySSLPort());
//		Assert.assertNotNull(conta.getSocketFactorySSL());
//		Assert.assertNotNull(conta.getUser());
//		Assert.assertNotNull(conta.getPort());
//		Assert.assertNotNull(conta.getTimeOut());
//		
//	}
//}
