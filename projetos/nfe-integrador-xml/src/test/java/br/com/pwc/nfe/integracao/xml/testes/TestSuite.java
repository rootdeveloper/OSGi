package br.com.pwc.nfe.integracao.xml.testes;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	IntegradorTest.class,
	RunJobTaskTest.class,
	ActivatorTest.class
})
public class TestSuite {

}
