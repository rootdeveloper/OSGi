//package br.com.pwc.integracao.mail.config;
//
//import java.util.Properties;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import br.com.pwc.integracao.mail.Account;
//import br.com.pwc.nfe.integracao.mail.util.MapperToAccount;
//
//public class ConfiguracaoContaTest {
//
//	@Test
//	public void testCriacaoPropertiesConfiguracaoConta() {
//		Account account = new Account();
//		account.setDebugAuthenticador(true);
//		account.setDebugMail(true);
//		account.setPasswd("Teste");
//		account.setPort(995);
//		account.setProtocolo("pop3");
//		account.setServer("mail.dataprimer.com.br");
//		account.setSocketFactorySSL(MapperToAccount.SOCKECT_FACTORY_VALUE);
//		account.setSocketFacotorySSLPort("995");
//		account.setSsl(true);
//		account.setTimeOut(1000L);
//		account.setUser("integrador.teste@dataprimer.com.br");
//		Properties config = ConfiguracaoConta.getConfig(account);
//		Assert.assertNotNull(config);
//		Assert.assertFalse(config.isEmpty());
//	}
//	
//	@Test(expected = Exception.class)
//	public void testErroCriacaoPropertiesConfiguracaoConta() {
//		Account account = new Account();
//		account.setDebugAuthenticador(true);
//		account.setDebugMail(true);
//		account.setPasswd("Teste");
//		account.setPort(995);
//		account.setProtocolo("pop3");
//		account.setServer("mail.dataprimer.com.br");
//		account.setSocketFactorySSL(MapperToAccount.SOCKECT_FACTORY_VALUE);
//		ConfiguracaoConta.getConfig(account);
//	}
//}
