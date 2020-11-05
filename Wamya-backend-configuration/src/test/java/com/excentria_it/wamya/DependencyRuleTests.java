package com.excentria_it.wamya;

import com.excentria_it.wamya.archunit.HexagonalArchitecture;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import org.junit.jupiter.api.Test;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

class DependencyRuleTests {

	@Test
	void validateRegistrationContextArchitecture() {
		HexagonalArchitecture.boundedContext("com.excentria_it.wamya")

				.withDomainLayer("domain")

				.withAdaptersLayer("adapter")
				.incoming("web")
				.outgoing("persistence").outgoing("messaging").outgoing("b2b.rest")
				.and()

				.withApplicationLayer("application")
				.services("service")
				.incomingPorts("port.in")
				.outgoingPorts("port.out")
				.and()

				.withConfiguration("configuration")
				.check(new ClassFileImporter()
						.importPackages("com.excentria_it.wamya.."));
	}

	@Test
	void testPackageDependencies() {
		noClasses()
				.that()
				.resideInAPackage("com.excentria_it.wamya.domain..")
				.should()
				.dependOnClassesThat()
				.resideInAnyPackage("com.excentria_it.wamya.application..")
				.check(new ClassFileImporter()
						.importPackages("com.excentria_it.wamya.."));
	}

}
